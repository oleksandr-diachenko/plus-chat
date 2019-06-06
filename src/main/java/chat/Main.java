package chat;

/**
 * @author Alexander Diachenko
 */
public class Main {

    private static String appContextLocation = "applicationContext.xml";

    public static void main(String[] args) {
        PlusChatFX.main(args, appContextLocation);
    }

    public static void setAppContextLocation(String appContextLocation) {
        Main.appContextLocation = appContextLocation;
    }
}
