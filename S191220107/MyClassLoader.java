package S191220107;

import java.io.*;
import java.util.*;

public class MyClassLoader extends ClassLoader {
    public byte[] getBytesFromClassName(String name) {
        return mClassBytes.get(name);
    }

    private Map<String, byte[]> mClassBytes;

    MyClassLoader() {
        mClassBytes = new HashMap<String, byte[]>();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = loadClassFromFile(name);
        mClassBytes.put(name, bytes);
        return this.defineClass(name, bytes, 0, bytes.length);

    }

    private byte[] loadClassFromFile(String fileName) {
        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream(fileName.replace('.', File.separatorChar) + ".class");
        byte[] buffer;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int nextValue = 0;
        try {
            while ((nextValue = inputStream.read()) != -1) {
                byteStream.write(nextValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer = byteStream.toByteArray();
        return buffer;
    }

}
