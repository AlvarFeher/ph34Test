package business.entities;

import business.CharacterManager;
import persistence.CharacterDAO;
import persistence.JSON.CharacterJsonDAO;

/**
 * Party entity. A member of a party, not the party as a set of characters.
 */
public class Party {
    private Character character;
    private int hitPoint;


    /**
     * constructor
     * @param character character object
     * @param hitPoint hit point
     */
    public Party(Character character, int hitPoint, CharacterDAO dao, int shield) {
        this.character = dao.assignClass(character.getName(), character.getPlayer(), character.getXp(),character.getBody(), character.getMind(), character.getSpirit(), character.getCharClass(), shield);
        this.hitPoint = hitPoint;
    }

    /**
     * character getter
     * @return character
     */
    public Character getCharacter(CharacterDAO dao) {
        return dao.assignClass(character.getName(),character.getPlayer(),character.getXp(),character.getBody(),character.getMind(),character.getSpirit(),character.getCharClass(),0);
    }

    /**
     * hit point getter
     * @return hit point
     */
    public int getHitPoint() {
        return hitPoint;
    }
}
