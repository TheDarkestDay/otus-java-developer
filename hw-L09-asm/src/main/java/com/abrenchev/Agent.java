package com.abrenchev;

import org.objectweb.asm.*;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

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

                String currentMethodName;

                public MethodAnnotationScanner(String methodName, MethodVisitor mv) {
                    super(Opcodes.ASM8, mv);
                    currentMethodName = methodName;
                }

                @Override
                public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                    if (desc.equals(LOG_ANNOTATION_DESC)) {
                        logAnnotationIsPresent = true;
                    }

                    return super.visitAnnotation(desc, visible);
                }

                @Override
                public void visitParameter(String name, int access) {

                }

                @Override
                public void visitCode() {
                    if (logAnnotationIsPresent) {
                        mv.visitCode();

                        Handle handle = new Handle(
                                H_INVOKESTATIC,
                                Type.getInternalName(java.lang.invoke.StringConcatFactory.class),
                                "makeConcatWithConstants",
                                MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class, Object[].class).toMethodDescriptorString(),
                                false);

                        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                        mv.visitVarInsn(Opcodes.ALOAD, 1);
                        mv.visitVarInsn(Opcodes.ALOAD, 2);
                        mv.visitInvokeDynamicInsn("makeConcatWithConstants", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", handle, "logged param:\u0001 and \u0001");

                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                    }
                }

                @Override
                public void visitEnd() {
                    logAnnotationIsPresent = false;

                    mv.visitEnd();
                }
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
                return new MethodAnnotationScanner(name, mv);
            }
        };

        classReader.accept(classVisitor, Opcodes.ASM8);

        return classWriter.toByteArray();
    }
}
