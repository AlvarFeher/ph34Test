package presentation;

/**
 * shows logo of the program
 * @author youssef Bat, Alvaro feher
 */
public enum LogoView {

    LOGO("""
               ____                 __       __    ____ ___   ___   _____
              / __/(_)__ _   ___   / /___   / /   / __// _ \\ / _ \\ / ___/
             _\\ \\ / //  ' \\ / _ \\ / // -_) / /__ _\\ \\ / , _// ___// (_ /
            /___//_//_/_/_// .__//_/ \\__/ /____//___//_/|_|/_/    \\___/
                          /_/
            """);

    /**
     * the message to be shown
     */
    private final String message;

    /**
     * default Constructor
     * @param message message to be shown in console
     */
    LogoView(String message) {
        this.message = message;
    }

    /**
     * the message to be shown
     * @return the message
     */
    @Override
    public String toString() {
        return message;
    }
}
