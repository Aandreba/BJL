package Extras;
import java.io.*;

public class Serialize {
    public static <T extends Serializable> void serialize (T object, String path) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(path);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);

        out.writeObject(object);
        out.close();
        fileOut.close();
    }

    public static <T extends Serializable> T deserialize (String path) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(path);
        ObjectInputStream in = new ObjectInputStream(fileIn);

        T object = (T) in.readObject();
        in.close();
        fileIn.close();

        return object;
    }
}
