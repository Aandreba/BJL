import Extras.JSON.JSON;
import Extras.Request.HTTP;
import Extras.Request.HTTPResponse;
import Extras.System.SystemInfo;
import OpenGL.GameObject;
import OpenGL.Primitives.Cube;
import OpenGL.Window;
import Units.Time;

import java.awt.*;

public class MacTest {
    public static void main (String[] args) throws Exception {
        String text = "{\n" +
                "  \"departamento\":8,\n" +
                "  \"nombredepto\":\"Ventas\",\n" +
                "  \"director\": \"Juan Rodríguez\",\n" +
                "  \"empleados\":[\n" +
                "    {\n" +
                "      \"nombre\":\"Pedro\",\n" +
                "      \"apellido\":\"Fernández\"\n" +
                "    },{\n" +
                "      \"nombre\":\"Jacinto\",\n" +
                "      \"apellido\":\"Benavente\"\n" +
                "    } \n" +
                "  ]\n" +
                "}";

        JSON json = new JSON(text);
        System.out.println(json);
    }
}
