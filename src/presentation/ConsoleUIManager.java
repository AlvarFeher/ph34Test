package presentation;


import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * represents the visualisation part of the program, is the direct interface with the user. where the user will read the messages and has to input things if required
 * @author youssef Bat, Alvaro Feher
 */
public class ConsoleUIManager {
    /**
     * Scanner used to scan user inputs
     */
    private final Scanner scanner;
    /**
     * Default constructor that creates a scanner object
     */
    public ConsoleUIManager() {
        scanner = new Scanner(System.in);
    }


    private static final String WELCOME_MESSAGE = "The tavern keeper looks at you and says: \n\"Welcome adventurer! How can I help you?\"\n\n";
    private static final String MAIN_MENU_ADVENTURE_DISABLED =
            """
            \t1) Character creation
            \t2) List characters
            \t3) Create an adventure
            \t4) Start an adventure (disabled: create 3 characters first)
            \t5) Exit
                                               
                    """;

    private static final String MAIN_MENU_ADVENTURE_NOT_DISABLED =
            """
            \t1) Character creation
            \t2) List characters
            \t3) Create an adventure
            \t4) Start an adventure
            \t5) Exit
                                               
                    """;
    private static final String ENTER_OPTION = "Your answer: ";

    private static final String ENTER_ANSWER = "-> Answer: ";

    private static final String WELCOME =
            """
            Welcome to Simple LSRPG.
            
            """;

    private static final String LOAD = "Loading data...";

    /**
     * shows the logo of the game
     */
    public void showLogo() {
        System.out.println(LogoView.LOGO);
        System.out.print(WELCOME);
    }

    /**
     * print data loading
     */
    public void loadData() {
        System.out.println(LOAD);
    }

    public StorageMenuOptions showStorageMenuOptions(){
        do{
            System.out.println("""
                    Do you want to use your local or cloud data?
                        1) Local data
                        2) Cloud data
                        """);
            System.out.print(ENTER_ANSWER);
            try{
                int option = Integer.parseInt(scanner.nextLine());
                switch(option){
                    case 1: return StorageMenuOptions.LOCAL;
                    case 2: return StorageMenuOptions.CLOUD;
                    default:
                        System.out.println(MessageView.OUT_OF_INTERVAL_BOUND);
                }
            }catch (NumberFormatException e){
                System.out.println(MessageView.INTEGER_EXCEPTION);
            }
        }while(true);
    }

    /**
     * shows the first main menu
     * @return the option the user would run
     * @param characterCount the number of characters in the system
     */
    public MainMenuOptions showMainMenu(int characterCount) {
        System.out.print(WELCOME_MESSAGE);
        do {
            if(characterCount < 3){
                System.out.println(MAIN_MENU_ADVENTURE_DISABLED);
            }
            else{
                System.out.println(MAIN_MENU_ADVENTURE_NOT_DISABLED);
            }
            System.out.print(ENTER_OPTION);
            try {
                int option = Integer.parseInt(scanner.nextLine());
                switch (option) {
                    case 1: return MainMenuOptions.CHARACTER_CREATION;
                    case 2: return MainMenuOptions.LIST_CHARACTERS;
                    case 3: return MainMenuOptions.CREATE_ADVENTURE;
                    case 4: return MainMenuOptions.START_ADVENTURE;
                    case 5: return MainMenuOptions.EXIT;
                    default: System.out.println(MessageView.NOT_VALID_OPTION_MENU);
                }
            }
            catch (NumberFormatException e) {
                System.out.println(MessageView.INTEGER_EXCEPTION);
            }
        } while (true);
    }

    /**
     * shows the exit message
     */
    public void exit() {
        System.out.println(MessageView.EXIT);
    }

    /**
     * shows the user a message, and gets a string value based on that message
     * @param message the message that the user will interact with by entering a string
     * @return user input as a string
     */
    private String getStringValue(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    /**
     * shows the user a message, and gets an integer value
     * @param message the message that the user will interact with by entering a string
     * @return user input as an integer
     */
    private int getIntValue(String message) {
        do {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException nfe) {
                System.out.println(MessageView.INTEGER_EXCEPTION);
            }
        } while (true);
    }

    /**
     * shows the user a message, and gets its name input value
     * @return user input name as a string
     */
    public String getUserName() {
        System.out.println("\nTavern keeper: \"Oh, so you are new to this land.\"\n\"What’s your name?\"\n");
        String name;
        do {
            name = getStringValue("-> Enter your name: ");
            if (name.isEmpty()) {
                System.out.println(MessageView.EMPTY_STRING);
            }
        } while(name.isEmpty());
        return name;
    }

    /**
     * shows the user a message, and gets its player_name input value
     * @param name character's name
     * @return user's input nickname as a player
     */
    public String getPlayerName(String name) {
        System.out.println("\nTavern keeper: \"Hello, "+ name +", be welcome.\"\n" +
                "\"And now, if I may break the fourth wall, who is your Player?\"\n");
        String player;
        do {
            player = getStringValue("-> Enter the player’s name: ");
            if (player.isEmpty()) {
                System.out.println(MessageView.EMPTY_STRING);
            }
        } while(player.isEmpty());
        return player;
    }

    /**
     * asks the user for the level of the player
     * @return player's level
     */
    public int getPlayerLevel() {
        int level;
        System.out.println("""

                Tavern keeper: \"I see, I see...\"
                \"Now, are you an experienced adventurer?
                """);
        do {
            level = getIntValue("-> Enter the character’s level [1..10]: ");
            if (level > 10 || level < 0) {
                System.out.println(MessageView.OUT_OF_INTERVAL_BOUND);
            }
        } while (level > 10 || level < 0);
        return level;
    }

    /**
     * message showing that the system is currently generating the character's stats
     * @param level player's level
     */
    public void generateStats(int level) {
        System.out.println("\nTavern keeper: \"Oh, so you are level "+level+"!\"\n" +
                "\"Great, let me get a closer look at you...\"\n");
        System.out.println("Generating your stats...\n");
    }

    /**
     * asks the user to input the player name to look for
     * @return player's name
     */
    public String getPlayerNameToFilter() {
        System.out.println("""

                Tavern keeper: \"Lads! They want to see you!\"
                \"Who piques your interest?\"
                """);
        return getStringValue("-> Enter the name of the Player to filter: ");
    }

    /**
     * shows if the databases are successfully loaded
     * @param loaded is monster database correctly loaded
     */
    public void showLoadingError(boolean loaded) {
        if (loaded) {
            System.out.println(MessageView.LOADING_JSON_SUCCESS);
        }
        else {
            System.out.println(MessageView.LOADING_MONSTER_ERROR);
        }
    }

    /**
     * gets the statistics of a character
     * @param stat the statistics type
     * @param rollDice random number generated
     * @param rollDice1 random number number generated
     * @return the total of both random numbers
     */
    public int showRoleDice(String stat, int rollDice, int rollDice1) {
        int total = rollDice + rollDice1;
        System.out.println(stat + ":\tYou rolled " + total + " (" + rollDice + " and " + rollDice1 + ").");
        return total;
    }


    /**
     * show the 3 statistics types generated of a character
     * @param body body statistics
     * @param mind mind statistics
     * @param spirit spirit statistics
     */
    public void showStats(int body, int mind, int spirit) {
        System.out.println();
        System.out.println("Your stats are:\n" +
                " - Body: "+statToString(body)+"\n" +
                " - Mind: "+statToString(mind)+"\n" +
                " - Spirit: "+statToString(spirit)+"");
        System.out.println();
    }

    public String askInitialClass(){
        String init_class;
        init_class = getStringValue("-> Enter the character’s initial class [Adventurer, Cleric, Wizard]:");
        init_class = init_class.toLowerCase();
        switch (init_class) {
            case "adventurer" : return "adventurer";
            case "cleric" : return "cleric";
            case "wizard" : return "wizard";
            default:
                System.out.println(MessageView.NOT_VALID_OPTION_MENU);
                askInitialClass();
        }
        return null;
    }

    /**
     * shows the error that the name contains a digit
     */
    public void showCharacterNameNotValid() {
        System.out.println(MessageView.DIGIT_IN_NAME_ERROR);
    }

    /**
     * shows the status after the end of character creation
     * @param name name of the character
     * @param is_created is the character created or not
     */
    public void showCharacterCreationStatus(String name, int is_created) {
        if (is_created == 1) {
            System.out.println("\nThe new character "+name+" has been created\n");
        }
        if (is_created == 0) {
            System.out.println(MessageView.CHARACTER_NOT_UNIQUE);
        }
    }

    /**
     * get the player to filter
     * @param listCharacters a list of characters names filtered
     * @return the position of the player in the list
     */
    public int filterPlayer(List<String> listCharacters) {
        if (listCharacters.isEmpty()) {
            System.out.println(MessageView.NO_CHARACTER_TO_LIST);
            return 0;
        }
        System.out.println("\nYou watch as all adventurers get up from their chairs and approach you.\n");
        for (int i=1;i<=listCharacters.size();i++) {
            System.out.println("\t" + i + ". " + listCharacters.get(i-1));
        }
        System.out.println("\n\t0. Back\n");
        int num;
        do {
            num = getIntValue("Who would you like to meet [0.."+ listCharacters.size()+"]:");
            if (num < 0 || num > listCharacters.size()) {
                System.out.println(MessageView.OUT_OF_INTERVAL_BOUND);
            }
        } while (num < 0 || num > listCharacters.size());
        return num;
    }

    /**
     * shows the status after the end of character deletion
     * @param character_to_delete the name of the character to delete
     */
    public void showCharacterDeletionStatus(String character_to_delete) {
        System.out.println("\nTavern keeper: \"I’m sorry kiddo, but you have to leave.\"\n" +
                    "Character "+character_to_delete+" left the Guild.");

    }

    /**
     * shows full information about a specific character and gets the name of the character to delete
     * @param name character's name
     * @param player character's player name
     * @param level character's level
     * @param xp character's xp
     * @param body character's body
     * @param mind character's mind
     * @param spirit character's spirit
     * @return the name of the character to delete
     */
    public String showFullCharacterInfo(String name, String player, int level, int xp, int body, int mind, int spirit, String char_class) {
        System.out.println("\nTavern keeper: \"Hey "+name+" get here; the boss wants to see you!\"\n");
        System.out.println("* Name:   " + name +
                "\n* Player: " + player +
                "\n* Class:  " + char_class +
                "\n* Level:  " + level +
                "\n* XP:  " + xp +
                "\n* Body:   " + statToString(body) +
                "\n* Mind:   "  + statToString(mind) +
                "\n* Spirit: " + statToString(spirit) + "\n");
        System.out.println("[Enter name to delete, or press enter to cancel]");
        String str;
        do{
            str = getStringValue("Do you want to delete "+name+"? ");
            if (!Objects.equals(str, name) && !str.isEmpty()) {
                System.out.println(MessageView.CHARACTER_NOT_MATCHING);
            }
        } while (!Objects.equals(str, name) && !str.isEmpty());
        return str;
    }

    /**
     * converts a number to a string by adding their signs to the left
     * @param stat a number
     * @return the number with its sign
     */
    private String statToString(int stat) {
        if (stat > 0) {
            return "+" + stat;
        }
        return String.valueOf(stat);
    }


    // ****************************

    //          ADVENTURE

    // ****************************

    /**
     * asks for the name of the adventure
     * @return the adventure's name
     */
    public String getAdventureName() {
        System.out.println("\nTavern keeper: \"Planning an adventure? Good luck with that!\"\n");
        String name;
        do {
            name = getStringValue("-> Name your adventure: ");
            if (name.isEmpty()) {
                System.out.println(MessageView.EMPTY_STRING);
            }
        } while(name.isEmpty());
        return name;
    }

    /**
     * asks for the num of encounters of an adventure
     * @param name the adventure's name
     * @return the adventure's encountering number
     */
    public int getEncounteringNum(String name) {
        int num;
        int count = 0;
        System.out.println("\nTavern keeper: \"You plan to undertake "+ name +", really?\"\n" +
                "\"How long will that take?\"\n");
        do{
            num = getIntValue("-> How many encounters do you want [1..4]: ");
            if (count >= 2) {
                System.out.println(MessageView.THREE_ATTEMPTS_FAILED);
                return -1;
            }
            if (num < 0 || num > 4) {
                System.out.println(MessageView.OUT_OF_INTERVAL_BOUND);
                count++;
            }
        }while (num < 0 || num > 4);
        System.out.println("Tavern keeper:" +num+ " encounters? That is too much for me...");
        return num;
    }


    /**
     * whows a status of which encounter the adventure is at
     * @param currentEncounter the current encounter position
     * @param encounterNum the total number of encounters
     */
    public void showMonstersMsg(int currentEncounter, int encounterNum){
        System.out.println();
        System.out.println("* Encounter "+currentEncounter+"/"+encounterNum);
        System.out.println("* Monsters in encounter");
    }

    /**
     * prints the monster in an encounter
     * @param i position
     * @param monsterName name of the monster
     * @param monsterAmount the amount of that monster in that occurrence
     */
    public void printMonsterInEncounter(int i, String monsterName, int monsterAmount){
        System.out.println("\t" + ++i + ". "+monsterName+" ("+monsterAmount+")");
    }

    /**
     * shows the menu of monsters
     * @return the option chosen from the menu
     */
    public AdventureMenuOptions showMonstersMenu(){
        int num;
        do{
            System.out.println("\n1. Add monster\n2. Remove monster\n3. Continue");
                num = getIntValue("\n-> Enter an option [1..3]: ");
                switch (num){
                    case 1: return AdventureMenuOptions.ADD_MONSTER;
                    case 2: return AdventureMenuOptions.DELETE_MONSTER;
                    case 3: return AdventureMenuOptions.CONTINUE;
                    default:
                        System.out.println(MessageView.OUT_OF_INTERVAL_BOUND);
                }
        }while (true);
    }

    /**
     * print empty
     */
    public void printEmpty(){
        System.out.println("\t#Empty");
    }

    /**
     * shows the monsters with their challenges
     * @param monsters a list of monster's names
     * @param challenge a list of the monster's challenges
     */
    public void showAvailableMonsters(List<String> monsters, List<String> challenge){
        for (int i=1;i<=monsters.size();i++) {
            System.out.println(i+". "+monsters.get(i-1)+" ("+challenge.get(i-1)+")");
        }
    }

    /**
     * ask for the position of the monster to pick
     * @param monstersNum the number of monsters we have
     * @return the position of the monster in the interval given
     */
    public int getMonsterFromMenu( int monstersNum){
        int monster;
        do {
            System.out.println();
            monster = getIntValue(" -> Choose a monster to add [1.."+monstersNum+"]: ");
            if (monster < 1 || monster > monstersNum) {
                System.out.println(MessageView.OUT_OF_INTERVAL_BOUND);
            }
        } while(monster < 1 || monster > monstersNum);
        return monster;
    }

    /**
     * asks for a positive number of monsters to add in an encounter
     * @param monsterName the name of the monster
     * @return the number of monsters to add
     */
    public int getNumMonstersToAdd(String monsterName){
        int n;
        do {
            n = getIntValue(" -> How many " + monsterName + "(s) do you want to add? ");
            if (n < 0) {
                System.out.println("\nError! Please choose a positive number\n");
            }
        } while (n < 0);
        return n;
    }

    /**
     * asks for the position of the monster to delete in an encounter of an adventure
     * @param size the number of filtered monsters in an encounter
     * @return the position of the monster to delete
     */
    public int getNumOfMonsterToDelete(int size){
        if (size == 0) {
            return Integer.MIN_VALUE;
        }
        int monster;
        do {
            monster = getIntValue("-> Which monster do you want to delete? ");
            if (monster < 1 || monster > size) {
                System.out.println();
                System.out.println(MessageView.OUT_OF_INTERVAL_BOUND + "Please choose an option between 1 and " + size);
                System.out.println();
            }
        } while(monster < 1 || monster > size);
        return monster;
    }

    //**************************
    //    START ADVENTURE
    //**************************

    /**
     * shows intro message to show that the user is in start adventure option
     * @param size the number of adventures in the system
     */
    public void startAdventureMsg(int size) {
        if (size > 0) {
            System.out.println("""
                                    
                    Tavern keeper: \"So, you are looking to go on an adventure?\"
                    \"Where do you fancy going?\"
                    """);
            System.out.println("Available adventures: ");
        }
        if (size == 0) {
            System.out.println(MessageView.NO_ADVENTURE_TO_LIST);
        }
        if (size == -1){
            System.out.println(MessageView.NOT_ENOUGH_CHARACTERS);
        }
    }


    /**
     * shows one of the available adventures
     * @param i a position
     * @param adventures the name of the adventure
     */
    public void showAvailableAdventures(int i, String adventures){
        System.out.println("\t" + ++i + ". " + adventures);
    }

    /**
     * asks the user to choose a valid adventure to play
     * @param size the total number of adventure in the system
     * @return the position of the adventure
     */
    public int chooseAdventure(int size){
        int adventure;
        do {
            System.out.println();
            adventure = getIntValue("-> Choose an adventure: ");
            if (adventure < 1 || adventure > size) {
                if (size == 1) {
                    System.out.println("Error! The only valid option is 1");
                }
                else {
                    System.out.println(MessageView.OUT_OF_INTERVAL_BOUND + "Please choose an option between 1 and " + size);
                }
            }
        } while(adventure < 1 || adventure > size);
        return adventure;

    }

    /**
     * shows the party information
     * @param party the names of the characters in the party
     * @param maxAmount the total number of characters supposed to be in the party
     * @param currentAmount the current number of characters in the party
     */
    public void showCurrentParty(String[] party, int maxAmount, int currentAmount){
        System.out.println("\n\n------------------------------");
        System.out.println("Your party ("+currentAmount+" / "+maxAmount+"):");
        for (int i = 1; i <= maxAmount; i++) {
            System.out.println("\t" + i+". "+party[i-1]);
        }
        System.out.println("------------------------------");
    }

    /**
     * asks the user for the number of characters to be in an adventure
     * @param currentAdventure the name of the adventure
     * @param characterCount the total number of characters available in the system
     * @return the number of characters to be in an adventure
     */
    public int chooseNumOfCharactersAdventure(String currentAdventure, int characterCount){
        System.out.println("\nTavern keeper: \""+currentAdventure+"\" it is!\nAnd how many people shall join you? ");
        int n;
        do {
            System.out.println();
            n = getIntValue("-> Choose a number of characters [3..5]: ");
            if (n < 3 || n > 5) {
                System.out.println(MessageView.OUT_OF_INTERVAL_BOUND);
            }
            if (n > characterCount && (n >= 3 && n <= 5)) {
                System.out.println(MessageView.EXCEPTION_MAX_VAL);
            }
        } while(n < 3 || n > 5);
        System.out.println("\nTavern keeper: \"Great, "+n+" it is.\"");
        System.out.println("\"Who amongst this lads shall join you?\"");
        return n;
    }

    /**
     * show all the available characters
     * @param characters list of character names
     */
    public void showAvailableCharacters(List<String> characters){
        System.out.println("\nAvailable characters:");
        int i=1;
        for(String c: characters){
            System.out.println("\t" + i +". "+c);
            i++;
        }
    }

    /**
     * asks the user to choose a character by index
     * @param characterToChooseIndex the current party to choose
     * @param size the total number of characters
     * @return the index of character
     */
    public int chooseCharacterForParty(int characterToChooseIndex, int size){
        int character;
        do {
            System.out.println();
            character = getIntValue("\n-> Choose character "+characterToChooseIndex+" in your party: ");
            if (character < 1 || character > size) {
                System.out.println(MessageView.OUT_OF_INTERVAL_BOUND);
            }
        } while(character < 1 || character > size);
        return character;
    }

    /**
     * shows the user that the adventure is in which encounter
     * @param index current encounter
     */
    public void showEncounterMsg(int index){
        System.out.println();
        System.out.println("---------------------\nStarting Encounter "+ ++index+":");
    }

    /**
     * shows preparation stage message
     */
    public void printPreparationStageTitle(){
        System.out.println("""
                ---------------------


                -------------------------
                *** Preparation stage ***
                -------------------------""");
    }

    /**
     * shows each party how much it increases in their self motivation
     * @param party all the party names
     */
    public void printSelfMotivation(String[] party){
        for(String c: party){
            if(!Objects.equals(c, "")){
                System.out.println(c+" uses Self-Motivated. Their Spirit increases in +1.");
            }
        }
    }

    /**
     * shows short rest stage message
     */
    public void printRestStageTitle(){
        System.out.println("""

                ------------------------
                *** Short rest stage ***
                ------------------------""");
    }

    /**
     * shows an error message to the user, saying that the name entered is not unique
     */
    public void showAdventureIsNotUnique() {
        System.out.println();
        System.out.println(MessageView.ADVENTURE_NOT_UNIQUE);
    }

    /**
     * shows error that only one boss monster can be added
     */
    public void showMonsterIsBoss() {
        System.out.println();
        System.out.println("Sorry, but only one Boss monster can be added");
        System.out.println();
    }

    /**
     * shows that we have no monsters in the encounter and we access the delete option, an exception is triggered
     */
    public void showNoMonsterInEncounter() {
        System.out.println();
        System.out.println(MessageView.NO_MONSTERS_IN_ENCOUNTER);
        System.out.println();
    }

    /**
     * shows that the adventure has been created successfully
     * @param adventure_name the name of the adventure
     */
    public void showAdventureCreated(String adventure_name) {
        System.out.println();
        System.out.println("Tavern keeper: \"Great plan lad! I hope you won’t die!\"\n" +
                "The new adventure "+ adventure_name+" has been created.");
    }

    /**
     * shows that a character has been token before
     */
    public void characterHasBeenChoosen() {
        System.out.println();
        System.out.println(MessageView.CHARACTER_TAKEN);
        System.out.println();
    }

    /**
     * shows that the adventure will be starting
     * @param currentAdventure the name of the adventure
     */
    public void showEndOfFillingPartyInAdventure(String currentAdventure) {
        System.out.println("\nTavern keeper: \"Great, good luck on your adventure lads!\"\n" +
                "The \"" + currentAdventure + " \" will start soon...");
    }

    /**
     * shows the monster name with its occurrence in the encounter
     * @param monster the name of the monster
     * @param occurrenceMonsterInEncounter how many monsters with the monster name
     */
    public void showMonsterInEncounter(String monster, int occurrenceMonsterInEncounter) {
        System.out.println("\t- " + occurrenceMonsterInEncounter + "x " + monster);
    }

    /**
     * shows the combatants sorted by their initial value
     * @param combatants_name the name of all the combatants
     * @param combatants_init_value the initiative values of all the combatants
     */
    public void showRollingInitiative(List<String> combatants_name, List<Integer> combatants_init_value) {
        System.out.println("\nRolling initiative...");
        for (int i=0;i< combatants_init_value.size();i++) {
            System.out.println("\t- " + combatants_init_value.get(i) + " \t" + combatants_name.get(i));
        }
    }

    /**
     * shows the combat stage initialization
     */
    public void startCombatStage() {
        System.out.println("""
                
                --------------------
                *** Combat stage ***
                --------------------""");
    }

    /**
     * shows the hit points of each of the parties in a specific round
     * @param i the round
     * @param partyNames the names of all the characters in the encounter
     * @param hit_points the actual hit points of all the characters in the encounter
     * @param max_hit_points the total hit points of all the characters in the encounter
     */
    public void showRoundCombatStage(int i, String[] partyNames, List<Integer> hit_points, List<Integer> max_hit_points) {
        System.out.println("Round "+ ++i +":\nParty:");
        for (int j=0;j<partyNames.length;j++) {
            System.out.print("\t- "+ partyNames[j] + " \t" + hit_points.get(j) + " / "+max_hit_points.get(j)+" hit points\n");
        }
    }


    /**
     *
     * @param monster_or_party the combatant
     * @param s the combatant attacking
     * @param s2 the combatant being attacked
     * @param rollDiced if the attack is a fail or a hit or a critical hit
     * @param damage the damage that the combatant being attacked will get
     */
    public void showAttackAction(int monster_or_party, String s, String s2, int rollDiced, int damage, String damageType) {
        //monster attacks party
        if (monster_or_party == 0) {
            String new_string;
            if (s2.contains(" falls unconscious")) {
                new_string = s2.replace(" falls unconscious", "");
                System.out.println(s + " attacks " + new_string + ".");
                if (damage != Integer.MIN_VALUE) {
                    switch (rollDiced) {
                        case 0 -> System.out.println("Fails and deals 0 physical damage.");
                        case 1 -> System.out.println("Hits and deals " + damage + " physical damage.\n");
                        case 2 -> System.out.println("Critical hit and deals " + damage + " physical damage.\n");
                    }
                }
                System.out.println(s2 + "\n");
            }
            else {
                System.out.println(s + " attacks " + s2 + ".");
                switch (rollDiced) {
                    case 0 -> System.out.println("Fails and deals 0 physical damage.\n");
                    case 1 -> System.out.println("Hits and deals " + damage + " physical damage.\n");
                    case 2 -> System.out.println("Critical hit and deals " + damage + " physical damage.\n");
                }
            }
        }

        //party attacks monster
        if (monster_or_party == 1) {
            if (s2.contains(" dies")) {
                System.out.println(s + " attacks " + s2.replace(" dies" , "") + ".");
                if (damage != Integer.MIN_VALUE) {
                    switch (rollDiced) {
                        case 0 -> System.out.println("Fails and deals "+damageType+" damage.");
                        case 1 -> System.out.println("Hits and deals " + damage + " "+damageType+" damage.\n");
                        case 2 -> System.out.println("Critical hit and deals " + damage +" "+damageType+" damage.\n");
                    }
                }
                System.out.println(s2 + "\n");
            }
            else{
                if (s2.isEmpty()) {
                    return;
                }
                System.out.println(s + " attacks " + s2 + " with Sword slash.");
                switch (rollDiced) {
                    case 0 -> System.out.println("Fails and deals"+damageType+" damage.");
                    case 1 -> System.out.println("Hits and deals " + damage + " "+damageType+" damage.\n");
                    case 2 -> System.out.println("Critical hit and deals" + damage +" "+damageType+" damage.\n");
                }
            }
        }
    }

    /**
     * shows that a round in a combat stage is ended
     * @param round round of a combat stage
     */
    public void showEndOfRound(int round) {
        System.out.println("\nEnd of round "+round+".");
    }

    /**
     * shows that all the enemies are defeated
     */
    public void allEnemiesAreDefeated() {
        System.out.println("All enemies are defeated.");

    }


    /**
     * show the status in short rest stage with the xp gains.
     * @param party_names the names of thr characters in an adventure
     * @param xp_gained the xp that the character will gain
     * @param lvl_increase a list showing if a character has leveled up or not
     */
    public void showXpGaining(String[] party_names, int xp_gained, List<Integer> lvl_increase) {
        for (int i=0;i<party_names.length;i++) {
            System.out.print(party_names[i] + " gains " + xp_gained + " xp. ");
            if (lvl_increase.get(i) > 0) {
                System.out.print(party_names[i] + " levels up. They are now lvl " + lvl_increase.get(i) + "!");
            }
            System.out.println();
        }
    }

    /**
     * show the status in short rest stage with the healing time.
     * @param party_names  the names of thr characters in an adventure
     * @param bandage_time the amount of hit points healed
     * @param is_healed list showing if a character is conscious or not
     */
    public void showHealingTime(String[] party_names, int[] bandage_time, List<Integer> is_healed) {
        System.out.println();
        for (int i=0;i<party_names.length;i++) {
            if (is_healed.get(i) == 1) {
                System.out.println(party_names[i] + " uses Bandage time. Heals " + bandage_time[i] + " hit points.");
            }
            else {
                System.out.println(party_names[i] + " is unconscious.");
            }
        }
        System.out.println("\n");
    }

    /**
     * shows the TPU message
     */
    public void showTPU() {
        System.out.println("""

                Tavern keeper: \"Lad, wake up. Yes, your party fell unconscious.\"
                \"Don’t worry, you are safe back at the Tavern.\"
                """);
    }

    /**
     * shows that the adventure has ended with the defeat of all the monsters
     * @param currentAdventure name of the adventure
     */
    public void showPartyCompleteAdventure(String currentAdventure) {
        System.out.println("\nCongratulations, your party completed \""+currentAdventure+"\"\n");
    }


    public void showFireballAttack(int damage, List<String> monsterNames, String attackerName){
        System.out.print(attackerName+ " attacks ");
        for (int i =0; i < monsterNames.size()-1; i++){
            System.out.print(monsterNames.get(i)+", ");
        }
        System.out.println("and "+monsterNames.get(monsterNames.size()-1)+" with Fireball.");
        System.out.println("Hits and deals "+damage+" magical damage.");
    }

    public void showClericHeal(String clericName, int heal, String target){
        System.out.println(clericName + " uses Prayer of Healing. Heals "+heal+" hit points to "+target);
    }

    public void showPaladinHeal(String paladinName, int heal, String[] targets){
        System.out.print(paladinName+ " uses prayer of Mass Healing. Heals "+heal+" hit points to ");
        for (int i =0; i <targets.length-1; i++){
            System.out.print(targets[i]+", ");
        }
        System.out.println("and "+targets[targets.length-1]);
    }




}

