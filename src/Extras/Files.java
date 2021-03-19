package Extras;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Files {
    public static ByteBuffer loadFile (File file) throws Exception {
        if (!file.isFile()) {
            throw new Exception("Value provided isn't file");
        }

        FileInputStream fis = new FileInputStream(file);
        return ByteBuffer.wrap(fis.readAllBytes());
    }

    public static String loadFile (File file, Charset charset) throws Exception {
        if (!file.isFile()) {
            throw new Exception("Value provided isn't file");
        }

        FileInputStream fis = new FileInputStream(file);
        return new String (fis.readAllBytes(), charset);
    }

    public static String loadFile (String path, Charset charset) throws Exception {
        return loadFile(new File(path), charset);
    }

    public static String loadFile (String path) throws Exception {
        return loadFile(path, StandardCharsets.UTF_8);
    }
}
