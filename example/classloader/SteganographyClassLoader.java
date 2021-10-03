package example.classloader;

import java.io.IOException;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;

import example.encoder.SteganographyEncoder;

import java.awt.image.BufferedImage;

public class SteganographyClassLoader extends ClassLoader {

    private URL url;
    private File file;

    public SteganographyClassLoader(URL imageURL) {
        this(imageURL, null);
    }

    public SteganographyClassLoader(File file) {
        this(null, file);
    }

    public SteganographyClassLoader(URL imageURL, File file) {
        super();
        this.url = imageURL;
        this.file = file;
    }

    public SteganographyClassLoader(URL imageURL, File file, ClassLoader parent) {
        super(parent);
        this.url = imageURL;
        this.file = file;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        try {
            BufferedImage img;
            if (url != null)
                img = ImageIO.read(url);
            else
                img = ImageIO.read(file);

            SteganographyEncoder encoder = new SteganographyEncoder(img);
            byte[] bytes = encoder.decodeByteArray();
            return this.defineClass(name, bytes, 0, bytes.length);

        } catch (IOException e) {
            throw new ClassNotFoundException();
        }

    }

}
