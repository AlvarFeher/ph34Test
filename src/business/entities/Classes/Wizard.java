package business.entities.Classes;

import business.entities.Character;
import business.entities.Party;
import persistence.CharacterDAO;
import persistence.JSON.CharacterJsonDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Wizard extends Character {
    private int shield;

    /**
     * constructor
     *
     * @param name      character's name
     * @param player    player name
     * @param xp        character's xp
     * @param body      character's body statistics
     * @param mind      character's mind statistics
     * @param spirit    character's spirit statistics
     * @param shield    wizard's shield statistics
     * @param charClass character's class
     */
    public Wizard(String name, String player, int xp, int body, int mind, int spirit, String charClass, int shield) {
        super(name, player, xp, body, mind, spirit, charClass);
        this.shield = shield;
    }

    /**
     * get shield value of Wizard
     * @return shield
     */
    public int getShield() {
        return this.shield;
    }

    /**
     * set shield value of Wizard
     *
     */
    public void setShield(int shield) {
        this.shield = shield;
    }


    /**
     * Function overridden from Character. It returns either healing or damage depending on the need of healing
     * @param param1 Unused parameter
     * @param aliveMonsters amount of monsters currently alive
     * @return Either global or individual attack value depending on current alive monsters
     */
    @Override
    public int doAction(int param1, int aliveMonsters) {
        if(aliveMonsters < 3){
            return arcaneMissile(getMind());
        }else
            return fireballAttack(getMind());
    }

    /**
     * Paladin's don't use this overridden function from Character.
     * @return returns 0
     */
    @Override
    public int doAction() {
        return 0;
    }

    /**
     * Generates damage value from Arcane Missile attack
     * @param mind Wizard's mind value
     * @return damage by attack
     */
    public int arcaneMissile(int mind){
        return (int)Math.floor(Math.random() * (6) + 1) + mind;
    }

    /**
     * Generates damage value from Fireball attack
     * @param mind Wizard's mind value
     * @return damage by attack
     */
    public int fireballAttack(int mind){
        return (int)Math.floor(Math.random() * (4) + 1) + mind;
    }

    /**
     * Adds shield value to Wizard
     * @param party Current Party in the adventure
     * @param charName Wizard's name
     * @param dao CharacterDao used to assign character class
     * @return Returns new party affected by the effects of the preparation stage action
     */
    @Override
    public List<Party> preparationStageAction(List<Party> party, String charName, CharacterDAO dao) {
        List<Party> newParty = new ArrayList<>();
        int mind;
        int level ;
        for(Party c: party){
            if(Objects.equals(c.getCharacter(dao).getName(), charName)){
                mind = c.getCharacter(dao).getMind();
                level = (c.getCharacter(dao).getXp()/100) +1;
                setShield(((int)Math.floor(Math.random() * (6) + 1) + mind)*level);
                Character ca = new Wizard(c.getCharacter(dao).getName(),c.getCharacter(dao).getPlayer(),c.getCharacter(dao).getXp(),c.getCharacter(dao).getBody(),c.getCharacter(dao).getMind(),c.getCharacter(dao).getSpirit(),c.getCharacter(dao).getCharClass(),this.shield);
                newParty.add(new Party(ca,c.getHitPoint(),dao,shield));
            }else
                newParty.add(c);

        }

        return newParty;
    }

    /**
     * Wizard's don't use this overridden function from Character.
     * @return returns the same List of Party without modification
     */
    @Override
    public List<Party> shortRestAction(List<Party> parties, String charName, CharacterDAO dao) {
        return parties;
    }
}
