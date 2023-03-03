package business.entities;

/**
 * party entity
 */
public class Party {
    private Character character;
    private int hitPoint;
    private int maxHitPoint; // initial hp value

    /**
     *
     * @param character character object
     * @param hitPoint hit point
     */
    public Party(Character character, int hitPoint) {
        this.character = character;
        this.hitPoint = hitPoint;
    }

    /**
     * character getter
     * @return character
     */
    public Character getCharacter() {
        return character;
    }

    /**
     * hit point getter
     * @return hit point
     */
    public int getHitPoint() {
        return hitPoint;
    }
}
