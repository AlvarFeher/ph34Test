package business.entities;


import com.google.gson.InstanceCreator;
import com.google.gson.annotations.SerializedName;
import persistence.CharacterDAO;
import persistence.JSON.CharacterJsonDAO;

import java.util.List;


/**
 * character entity
 */
public class Character  {

    //TODO: Make this abstract
    private String name;
    private String player;
    private int xp;
    private int body;
    private int mind;
    private int spirit;
    @SerializedName(value="charClass", alternate="class")
    private String charClass;

    /**
     * constructor
     * @param name character's name
     * @param player player name
     * @param xp character's xp
     * @param body character's body statistics
     * @param mind character's mind statistics
     * @param spirit character's spirit statistics
     */
    public Character(String name, String player, int xp, int body, int mind, int spirit, String charClass) {
        this.name = name;
        this.player = player;
        this.xp = xp;
        this.body = body;
        this.mind = mind;
        this.spirit = spirit;
        this.charClass = charClass;
    }


    /** Version with no parameters for Adventurer, Warrior and Champion.
     * Function used by character classes that extend Character. Action used during combat for either healing or damaging.
     * @return returns 0 by default
     */
    public int doAction(){return 0;}


    /**
     * Version with parameters for Cleric, Paladin and Wizard
     * @param param1 used as a flag to check if healing is needed during combat
     * @param param2 used as a flag to check how many monsters are alive during combat
     * @return returns 0 by default
     */
    public  int doAction(int param1, int param2){return 0;}


    /**
     * Function to be extended by character classes.
     * Generates new party affected by the actions during preparation stage.
     * @param parties current Party
     * @param charName name of character performing such action
     * @param dao characterDao used to assign class to characters
     * @return returns null by default
     */
    public List<Party> preparationStageAction(List<Party> parties, String charName, CharacterDAO dao){return null; }

    /**
     * Function to be extended by character classes.
     * Generates new party affected by the actions during short rest stage.
     * @param parties Current Party
     * @param charName Name of character performing such action
     * @param dao CharacterDao used to assign class to characters
     * @return Returns null by default
     */
    public List<Party> shortRestAction(List<Party> parties, String charName, CharacterDAO dao){return null; }

    /**
     * name getter
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * player's name getter
     * @return player's name
     */
    public String getPlayer() {
        return player;
    }

    /**
     * xp getter
     * @return xp
     */
    public int getXp() {
        return xp;
    }

    /**
     * body getter
     * @return body
     */
    public int getBody() {
        return body;
    }

    /**
     * mind getter
     * @return mind
     */
    public int getMind() {
        return mind;
    }

    /**
     * spirit getter
     * @return spirit
     */
    public int getSpirit() {
        return spirit;
    }

    /**
     * class getter
     * @return class
     */
    public String getCharClass() {
        return charClass;
    }

    /**
     * get value for paladin print
     * @return
     */
    public int getTestPrepStage() { return 0; }

    /**
     * gets shield from wizard
     * @return
     */
    public int getShield(){return 0; }
}

