package business.entities;

import persistence.JSON.CharacterJsonDAO;

/**
 * party entity, a member of a party
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
    public Party(Character character, int hitPoint, CharacterJsonDAO dao) {
        this.character = dao.assignClass(character.getName(), character.getPlayer(), character.getXp(),character.getBody(), character.getMind(), character.getSpirit(), character.getCharClass());
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
