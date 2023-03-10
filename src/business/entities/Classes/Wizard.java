package business.entities.Classes;

import business.entities.Character;
import business.entities.Party;
import persistence.JSON.CharacterJsonDAO;

import java.util.List;

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
    public Wizard(String name, String player, int xp, int body, int mind, int spirit, String charClass) {
        super(name, player, xp, body, mind, spirit, charClass);
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
    public List<Party> preparationStageAction(List<Party> party, String charName, CharacterJsonDAO dao) {
        return null;
    }

    // NO SHORT REST ACTION
}
