package S191220107;

import java.io.*;
import java.util.*;
import example.encoder.SteganographyEncoder;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

public class MyClassLoader extends ClassLoader {
    public byte[] getBytesFromClassName(String name) throws Exception {
        if (mClassBytes.containsKey(name))
            return mClassBytes.get(name);
        else {
            this.findClass(name);
            return mClassBytes.get(name);
        }
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

    public static void main(String[] args) throws Exception {
        File f = new File("./S191220107/pics/original.png");
        File f1 = new File("./S191220107/pics/bubble.png");
        File f2 = new File("./S191220107/pics/heap.png");
        f1.createNewFile();
        f2.createNewFile();
        BufferedImage bufferedImage;
        bufferedImage = ImageIO.read(f);

        SteganographyEncoder encoder = new SteganographyEncoder(bufferedImage);
        MyClassLoader loader = new MyClassLoader();
        BufferedImage bi1 = encoder.encodeByteArray(loader.getBytesFromClassName("S191220107.sorter.HeapSorter"));
        BufferedImage bi2 = encoder.encodeByteArray(loader.getBytesFromClassName("S191220107.sorter.QuickSorter"));

        ImageIO.write(bi1, "png", f1);
        ImageIO.write(bi2, "png", f2);
    }

}
