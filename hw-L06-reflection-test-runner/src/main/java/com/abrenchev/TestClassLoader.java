package com.abrenchev;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class TestClassLoader extends ClassLoader {
    public TestClassLoader(ClassLoader parentLoader) {
        super(parentLoader);
    }

    public List<Class> loadClass() {
        List<Class> result = new ArrayList<>();

        File testDir = new File(System.getProperty("user.dir") + "/hw-L06-reflection-test-runner/src/test/java/com/abrenchev/testing");
        for (File testFile : testDir.listFiles()) {
            try {
                URL testFilePath = testFile.toURI().toURL();
                System.out.println(testFilePath);
                URLConnection connection = testFilePath.openConnection();
                InputStream input = connection.getInputStream();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int data = input.read();

                while (data != -1){
                    buffer.write(data);
                    data = input.read();
                }

                input.close();

                byte[] classData = buffer.toByteArray();

                Class<?> testClass = defineClass("test", classData, 0, classData.length);
                result.add(testClass);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        return result;
    }
}
