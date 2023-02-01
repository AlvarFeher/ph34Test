package presentation;

/**
 * contains all the messages shown to the user.
 * @author Youssef Bat, Alvaro Feher
 */
public enum MessageView {
    /**
     * shows when the user enters a non valid option
     */
    NOT_VALID_OPTION_MENU ("\nError, the entered option is not a valid option. Please choose one of the options shown in the menu\n"),
    /**
     * shows when the user enters a non int value
     */
    INTEGER_EXCEPTION ("\nError, You have to enter an integer\n"),
    /**
     * shows the exit message
     */
    EXIT("\nTavern keeper: “Are you leaving already? See you soon, adventurer.\""),
    /**
     * shows when the monsters json file is not available
     */
    LOADING_MONSTER_ERROR("Error: The monsters.json file can’t be accessed."),
    /**
     * shows when all json files are available
     */
    LOADING_JSON_SUCCESS("Data was successfully loaded.\n"),
    /**
     * shows when entering a number within the character's name
     */
    DIGIT_IN_NAME_ERROR("\nError! Names containing numbers or special characters are not accepted"),
    /**
     * shows when entering a value that is not part of the interval specified
     */
    OUT_OF_INTERVAL_BOUND("\nError! the value entered is out of the interval bound\n"),
    /**
     * shows when the character attempted to create is not unique
     */
    CHARACTER_NOT_UNIQUE("Error! No character has been created as the name is used (It is not unique)\n"),
    /**
     * shows when the name entered doesn't match what needs to be entered
     */
    CHARACTER_NOT_MATCHING("\nError! the name entered does not match the name to enter\n"),
    /**
     * shows when there is no character to list
     */
    NO_CHARACTER_TO_LIST("\nLooks like there is no character with this part of player name\n"),
    /**
     * shows when the adventure attempted to create is not unique
     */
    ADVENTURE_NOT_UNIQUE("Error! No adventure has been created as its name is already used (It is not unique)\n"),
    /**
     * shows when there is no adventure to list
     */
    NO_ADVENTURE_TO_LIST("\nLooks like there is no adventure to list\n"),
    /**
     * shows when the user should enter a non-empty string, but he enters nothing
     */
    EMPTY_STRING("\nError! Please Enter a non-empty value\n"),
    /**
     * shows when the user enters a wrong interval value for 3 times
     */
    THREE_ATTEMPTS_FAILED("Looks like you keep entering only wrong values. We will get you back to the menu."),
    /**
     * shows when there is less than 3 characters in the system, and the user attempts to start an adventure
     */
    NOT_ENOUGH_CHARACTERS("Sorry, you can't start an adventure unless 3 characters are created at least"),
    /**
     * shows when there is no monsters in the encounter
     */
    NO_MONSTERS_IN_ENCOUNTER("Error! there is no monsters in this encounter, therefore you can't access the delete option."),
    /**
     * shows when trying to pick a character (party) that has already been choosen before
     */
    CHARACTER_TAKEN("Error! No character can be chosen more than once"),
    /**
     * shows when trying to choose the party of an adventure, and the character numbers are less than 5
     */
    EXCEPTION_MAX_VAL("Sorry, but in this case we don't accept this value, as the number of characters is less that the value you have chosen");

    /**
     * the message to be shown
     */
    private final String message;

    /**
     * default Constructor
     * @param message message to be shown in console
     */
    MessageView(String message) {
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
