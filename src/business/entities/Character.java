package business.entities;

/**
 * character entity
 */
public class Character {

    private String name;
    private String player;
    private int xp;
    private int body;
    private int mind;
    private int spirit;
    private String charClass;

    /**
     * constructor
     * @param name character's name
     * @param player player name
     * @param xp character's xp
     * @param body character's body statistics
     * @param mind character's mind statistics
     * @param spirit character's spitit statistics
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

