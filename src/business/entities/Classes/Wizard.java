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
     * @param spirit    character's spitit statistics
     * @param charClass
     */
    public Wizard(String name, String player, int xp, int body, int mind, int spirit, String charClass, int shield) {
        super(name, player, xp, body, mind, spirit, charClass);
        this.shield = shield;
    }

    public int getShield() {
        return shield;
    }

    public void setShield(int shield) {
        this.shield = shield;
    }

    @Override
    public int doAction(int param1, int aliveMonsters) {
        if(aliveMonsters < 3){
            return arcaneMissile(getMind());
        }else
            return fireballAttack(getMind());
    }

    @Override
    public int doAction() {
        return 0;
    }

    public int arcaneMissile(int mind){
        return (int)Math.floor(Math.random() * (6) + 1) + mind;
    }

    public int fireballAttack(int mind){
        return (int)Math.floor(Math.random() * (4) + 1) + mind;
    }

    // SHIELD WTF !!!
    // FIXME: add shield points and make shield work during combat
    @Override
    public List<Party> preparationStageAction(List<Party> party, String charName, CharacterDAO dao) {
        List<Party> newParty = new ArrayList<>();
        int mind;
        int level ;
        for(Party c: party){
            if(Objects.equals(c.getCharacter(dao).getName(), charName)){
                mind = c.getCharacter(dao).getMind();
                level = (c.getCharacter(dao).getXp()/100) +1;
                Character ca = new Wizard(c.getCharacter(dao).getName(),c.getCharacter(dao).getPlayer(),c.getCharacter(dao).getXp(),c.getCharacter(dao).getBody(),c.getCharacter(dao).getMind(),c.getCharacter(dao).getSpirit(),c.getCharacter(dao).getCharClass(),((int)Math.floor(Math.random() * (6) + 1) + mind)*level);
                newParty.add(new Party(ca,c.getHitPoint(),dao));
            }else
                newParty.add(c);

        }

        return newParty;
    }

    @Override
    public List<Party> shortRestAction(List<Party> parties, String charName, CharacterDAO dao) {
        return parties;
    }
}
