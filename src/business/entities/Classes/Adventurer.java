package business.entities.Classes;

import business.entities.Character;
import business.entities.Party;
import persistence.CharacterDAO;
import persistence.JSON.CharacterJsonDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Adventurer extends Character {
    /**
     * constructor
     *
     * @param name      character's name
     * @param player    player name
     * @param xp        character's xp
     * @param body      character's body statistics
     * @param mind      character's mind statistics
     * @param spirit    character's spitit statistics
     * @param charClass character's class
     */
    public Adventurer(String name, String player, int xp, int body, int mind, int spirit, String charClass) {
        super(name, player, xp, body, mind, spirit, charClass);
    }

    /**
     * This function returns the damage value of the Attack action of an Adventurer.
     * It works as the Sword Slash
     * @return damage by Adventurer's attack: d6 + body
     */
    @Override
    public int doAction() {
        return (int)Math.floor(Math.random() * (6) + 1) + getBody();
    } // sword slash

    /**
     * Adventurers don't use this overridden function from Character.
     * @param param1 unused
     * @param param2 unused
     * @return returns 0
     */
    @Override
    public int doAction(int param1, int param2) {
        return 0;
    }

    /**
     * Function overridden from Character class. Updates party with effects of the Preparation Stage action of an Adventurer.
     * It works as Self-Motivated. Adds 1 spirit to itself
     * @param party List of characters from the Party
     * @param charName Name of the character
     * @param dao CharacterDao used to assign character class
     * @return Updated party affected by the preparation stage action of an adventurer
     */
    @Override
    public List<Party> preparationStageAction(List<Party> party, String charName, CharacterDAO dao) {
        List<Party> newParty = new ArrayList<>();
        for(Party c: party){
            if(Objects.equals(c.getCharacter(dao).getName(), charName)){
                newParty.add(new Party(new Character(c.getCharacter(dao).getName(),c.getCharacter(dao).getPlayer(),c.getCharacter(dao).getXp(),c.getCharacter(dao).getBody(),c.getCharacter(dao).getMind(),c.getCharacter(dao).getSpirit()+1,c.getCharacter(dao).getCharClass()),c.getHitPoint(),dao,0));
            }else
                newParty.add(c);
        }
        return newParty;
    }

    /**
     * Function overridden from Character class. Updates party with effects of the Short Rest Stage action of an Adventurer.
     * It works as Bandage Time. Adds 1 spirit to itself
     * @param parties List of characters from the Party
     * @param charName Name of the character
     * @param dao CharacterDao used to assign character class
     * @return Updated party affected by the short rest stage action of an adventurer
     */
    @Override
    public List<Party> shortRestAction(List<Party> parties, String charName, CharacterDAO dao){
        List<Party> newParty = new ArrayList<>();
        for(Party c: parties){
            int rand =(int)Math.floor(Math.random() * (8) + 1);
            if(Objects.equals(c.getCharacter(dao).getName(), charName)){
                Character ca = new Character(c.getCharacter(dao).getName(),c.getCharacter(dao).getPlayer(),c.getCharacter(dao).getXp(),c.getCharacter(dao).getBody(),c.getCharacter(dao).getMind(),c.getCharacter(dao).getSpirit(),c.getCharacter(dao).getCharClass());
                newParty.add(new Party(ca,c.getHitPoint()+rand,dao,0));
            }else
                newParty.add(c);
        }
        return newParty;
    }
}
