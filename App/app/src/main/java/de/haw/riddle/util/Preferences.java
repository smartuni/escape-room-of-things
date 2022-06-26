package de.haw.riddle.util;

public final class Preferences {
    private Preferences() {
        throw new AssertionError("nope");
    }

    public static final String IS_ADMIN = "isAdmin";
    public static final String IP_ADDRESS = "IP";
    public static final String PORT = "PORT";
    public static final String TOKEN = "token";

}
