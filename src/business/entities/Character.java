package business.entities;


import com.google.gson.InstanceCreator;
import com.google.gson.annotations.SerializedName;



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

    public int doAction(){return 0;}


    /**
     *
     * @param param1 used as a flag to check if healing is needed during combat
     * @param param2 used as a flag to check how many monsters are alive during combat
     * @return
     */
    public  int doAction(int param1, int param2){return 0;}

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


}

