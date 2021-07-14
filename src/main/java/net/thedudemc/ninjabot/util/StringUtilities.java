package net.thedudemc.ninjabot.util;

public class StringUtilities {

    public static String stripName(String name) {
        return name.replaceAll("[^a-zA-Z0-9]", "");
    }
}
