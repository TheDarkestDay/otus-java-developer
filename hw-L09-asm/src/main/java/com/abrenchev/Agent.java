package com.abrenchev;

import org.objectweb.asm.*;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.security.ProtectionDomain;

import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;

public class Agent {
    private static final String LOG_ANNOTATION_DESC = "Lcom/abrenchev/annotations/Log;";

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
                return addLogging(classfileBuffer);
            }
        });
    }

    private static byte[] addLogging(byte[] originalClass) {
        ClassReader classReader = new ClassReader(originalClass);
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM8, classWriter) {
            class MethodAnnotationScanner extends MethodVisitor {
                boolean logAnnotationIsPresent = false;

                final String currentMethodName;
                final String currentMethodDesc;

                public MethodAnnotationScanner(String methodName, MethodVisitor mv, String methodDesc) {
                    super(Opcodes.ASM8, mv);
                    currentMethodName = methodName;
                    currentMethodDesc = methodDesc;
                }

                @Override
                public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                    if (desc.equals(LOG_ANNOTATION_DESC)) {
                        logAnnotationIsPresent = true;
                    }

                    return mv.visitAnnotation(desc, visible);
                }

                @Override
                public void visitCode() {
                    if (logAnnotationIsPresent) {
                        mv.visitCode();

                        Type[] argumentTypes = Type.getArgumentTypes(currentMethodDesc);

                        Handle handle = new Handle(
                                H_INVOKESTATIC,
                                Type.getInternalName(java.lang.invoke.StringConcatFactory.class),
                                "makeConcatWithConstants",
                                MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class, Object[].class).toMethodDescriptorString(),
                                false);

                        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                        insertValuesIntoStack(argumentTypes);
                        mv.visitInvokeDynamicInsn("makeConcatWithConstants", "(" + getConcatArgumentsSignature(argumentTypes) + ")Ljava/lang/String;", handle, getConcatRecipe(argumentTypes.length));

                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                    }
                }

                @Override
                public void visitEnd() {
                    logAnnotationIsPresent = false;

                    mv.visitEnd();
                }

                private void insertValuesIntoStack(Type[] argumentTypes) {
                    for (int i = 0; i < argumentTypes.length; i++) {
                        int valueIndex = i + 1;
                        switch (argumentTypes[i].getDescriptor()) {
                            case "I":
                                mv.visitVarInsn(Opcodes.ILOAD, valueIndex);
                                break;
                            case "D":
                                mv.visitVarInsn(Opcodes.DLOAD, valueIndex);
                                break;
                            default:
                                mv.visitVarInsn(Opcodes.ALOAD, valueIndex);
                        }
                    }
                }

                private String getConcatArgumentsSignature(Type[] types) {
                    StringBuilder stringBuilder = new StringBuilder();

                    for (Type type : types) {
                        stringBuilder.append(type.getDescriptor());
                    }

                    return stringBuilder.toString();
                }

                private String getConcatRecipe(int argumentsCount) {
                    return currentMethodName + " called with params: " + "\u0001, ".repeat(Math.max(0, argumentsCount));
                }
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);

                return new MethodAnnotationScanner(name, mv, desc);
            }
        };

        classReader.accept(classVisitor, Opcodes.ASM8);

        return classWriter.toByteArray();
    }
}
