package de.haw.riddle.net.led;

public class LedStatus {

    private final String value;
    private LedStatus(String value) {
        this.value = value;
    }

    public static LedStatus activate(){
        return new LedStatus("1");
    }

    public static LedStatus deactivate(){
        return new LedStatus("0");
    }

    public String getValue() {
        return value;
    }
}
