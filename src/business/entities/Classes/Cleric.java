package business.entities.Classes;

import business.entities.Character;
import business.entities.Party;
import persistence.CharacterDAO;
import persistence.JSON.CharacterJsonDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cleric extends Character {
    /**
     * constructor
     *
     * @param name      character's name
     * @param player    player name
     * @param xp        character's xp
     * @param body      character's body statistics
     * @param mind      character's mind statistics
     * @param spirit    character's spirit statistics
     * @param charClass character's class
     */
    public Cleric(String name, String player, int xp, int body, int mind, int spirit, String charClass) {
        super(name, player, xp, body, mind, spirit, charClass);
    }

    /**
     * Cleric's don't use this overridden function from Character.
     * @return returns 0
     */
    @Override
    public int doAction() {
        return 0;
    }

    /**
     * Function overridden from Character. It returns either healing or damage depending on the need of healing
     * @param healingNeeded Checks if healing is need in combat
     * @param param2 Unused parameter
     * @return Either damage or healing value depending on healing
     */
    @Override
    public int doAction(int healingNeeded, int param2) {
        if(healingNeeded == 1){
            return prayerOfHealing(getMind());
        } else {
            return notOnMyWatch(getSpirit());
        }
    }

    /**
     * Generates heal value of Prayer Of Healing action
     * @param mind Cleric's mind value
     * @return Healing amount
     */
    public int prayerOfHealing(int mind){
        return (int)Math.floor(Math.random() * (10) + 1) + mind;
    }

    /**
     * Generates damage by Not On My Watch attack
     * @param spirit Cleric's spirit value
     * @return Damage amount of attack
     */
    public int notOnMyWatch(int spirit){
        return (int)Math.floor(Math.random() * (4) + 1) + spirit;
    }

    /**
     * Function overridden from Character class. Updates party with effects of the Preparation Stage action of a Cleric.
     * It works as Prayer of Good Luck. Adds 1 mind to everyone but itself
     * @param party List of characters from the Party
     * @param charName Name of the character
     * @param dao CharacterDao used to assign character class
     * @return Updated party affected by the preparation stage action of a Cleric
     */

    @Override
    public List<Party> preparationStageAction(List<Party> party, String charName, CharacterDAO dao) {
        List<Party> newParty = new ArrayList<>();
        for(Party c: party){
            if(!Objects.equals(c.getCharacter(dao).getName(), charName)){
                newParty.add(new Party(new Character(c.getCharacter(dao).getName(),c.getCharacter(dao).getPlayer(),c.getCharacter(dao).getXp(),c.getCharacter(dao).getBody(),c.getCharacter(dao).getMind()+1,c.getCharacter(dao).getSpirit(),c.getCharacter(dao).getCharClass()),c.getHitPoint(),dao,0));
            }else
                newParty.add(c);
        }
        return newParty;
    }

    /**
     * Function overridden from Character class. Updates party with effects of the Short Rest Stage action of a Cleric.
     * It works as Prayer of Healing.
     * @param parties List of characters from the Party
     * @param charName Name of the character
     * @param dao CharacterDao used to assign character class
     * @return Updated party affected by the short rest stage action of an cleric
     */
    public List<Party> shortRestAction(List<Party> parties, String charName, CharacterDAO dao){
        List<Party> newParty = new ArrayList<>();
        for(Party c: parties){
            int rand =(int)Math.floor(Math.random() * (10) + 1);
            if(Objects.equals(c.getCharacter(dao).getName(), charName)){
                Character ca = new Character(c.getCharacter(dao).getName(),c.getCharacter(dao).getPlayer(),c.getCharacter(dao).getXp(),c.getCharacter(dao).getBody(),c.getCharacter(dao).getMind(),c.getCharacter(dao).getSpirit(),c.getCharacter(dao).getCharClass());
                newParty.add(new Party(ca,c.getHitPoint()+rand,dao,0));
            }else
                newParty.add(c);
        }
        return newParty;
    }

}
