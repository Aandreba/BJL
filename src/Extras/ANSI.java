package Extras;

import java.awt.*;

public class ANSI {
    final private static String RESET = "\033[0m";

    public static String bold (String text) {
        return "\033[1m"+text+RESET;
    }

    public static String italic (String text) {
        return "\033[3m"+text+RESET;
    }

    public static String underline (String text) {
        return "\033[4m"+text+RESET;
    }

    public static String reversed (String text) {
        return "\033[7m"+text+RESET;
    }

    public static String crossed (String text) {
        return "\033[9m"+text+RESET;
    }

    public static String color (String text, Color color) {
        return "\033[38;2;"+color.getRed()+";"+color.getGreen()+";"+color.getBlue()+"m"+text+RESET;
    }

    public static String backgroundColor (String text, Color color) {
        return "\033[48;2;"+color.getRed()+";"+color.getGreen()+";"+color.getBlue()+"m"+text+RESET;
    }

    public static String underlineColor (String text, Color color) {
        return "\033[58;2;"+color.getRed()+";"+color.getGreen()+";"+color.getBlue()+"m"+text+RESET;
    }
}
