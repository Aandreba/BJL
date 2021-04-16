package System;

import Extras.Files;
import Units.ByteSize;
import java.awt.*;
import java.nio.charset.StandardCharsets;

public class SystemInfo {
    final private static Runtime runtime = Runtime.getRuntime();
    final private static Toolkit toolkit = Toolkit.getDefaultToolkit();

    final public static int THREADS = runtime.availableProcessors();
    final public static ByteSize MEMORY = new ByteSize(runtime.maxMemory());

    final public static OS OS = getOS();
    final public static CPU CPU = new CPU();

    public static int getScreenWidth () {
        return toolkit.getScreenSize().width;
    }
    public static int getScreenHeight () {
        return toolkit.getScreenSize().height;
    }

    public static String execute (String command) throws Exception {
        return new String(Files.loadStream(runtime.exec(command).getInputStream()).array(), StandardCharsets.UTF_8).strip();
    }

    private static OS getOS () {
        String name = System.getProperty("os.name").toLowerCase();

        if (name.contains("win")) {
            return OS.Windows;
        } else if (name.contains("mac")) {
            return OS.macOS;
        } else if (name.contains("nix") || name.contains("nux") || name.contains("aix")) {
            return OS.Linux;
        } else if (name.contains("sunos")) {
            return OS.Solaris;
        }

        return OS.Unknown;
    }
}
