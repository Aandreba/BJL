package Extras;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Files {
    public static ByteBuffer loadStream (InputStream stream) throws IOException {
        return ByteBuffer.wrap(stream.readAllBytes());
    }

    public static ByteBuffer loadFile (File file) throws IOException {
        if (!file.isFile()) {
            throw new IOException("Value provided isn't file");
        }

        FileInputStream fis = new FileInputStream(file);
        return loadStream(fis);
    }

    public static String loadFile (File file, Charset charset) throws IOException {
        if (!file.isFile()) {
            throw new IOException("Value provided isn't file");
        }

        FileInputStream fis = new FileInputStream(file);
        return new String (fis.readAllBytes(), charset);
    }

    public static String loadFile (String path, Charset charset) throws IOException {
        return loadFile(new File(path), charset);
    }

    public static String loadFile (String path) throws IOException {
        return loadFile(path, StandardCharsets.UTF_8);
    }

    public static String loadResource (String path) throws IOException {
        InputStream is = Files.class.getResourceAsStream(path);
        return new String (is.readAllBytes(), StandardCharsets.UTF_8);
    }
}
