package business.entities.Classes;

import business.entities.Character;
import business.entities.Party;
import persistence.CharacterDAO;
import persistence.JSON.CharacterJsonDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Paladin extends Character {

    private int testPrepStage;

    public int getTestPrepStage() {
        return testPrepStage;
    }

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
    public Paladin(String name, String player, int xp, int body, int mind, int spirit, String charClass) {
        super(name, player, xp, body, mind, spirit, charClass);
    }

    @Override
    public int doAction() {
        return 0;
    }

    @Override
    public int doAction(int healingNeeded, int param2) {
        if(healingNeeded == 1){
            return prayerOfMassHealing(getMind());
        } else {
            return neverOnMyWatch(getSpirit());
        }
    }
    public int prayerOfMassHealing(int mind){
        return (int)Math.floor(Math.random() * (10) + 1) + mind;
    }

    public int  neverOnMyWatch(int spirit){
        return (int)Math.floor(Math.random() * (8) + 1) + spirit;
    }

    // paladins add d3 mind to everyone but themselves
    @Override
    public List<Party> preparationStageAction(List<Party> party, String charName, CharacterDAO dao) {
        List<Party> newParty = new ArrayList<>();
        this.testPrepStage = (int)Math.floor(Math.random() * (3) + 1);
        for(Party c: party){
            if(!Objects.equals(c.getCharacter(dao).getName(), charName)){
                newParty.add(new Party(new Character(c.getCharacter(dao).getName(),c.getCharacter(dao).getPlayer(),c.getCharacter(dao).getXp(),c.getCharacter(dao).getBody(),c.getCharacter(dao).getMind()+this.testPrepStage,c.getCharacter(dao).getSpirit(),c.getCharacter(dao).getCharClass()),c.getHitPoint(),dao,0));
            }else
                newParty.add(c);
        }
        return newParty;
    }

    @Override
    public List<Party> shortRestAction(List<Party> parties, String charName, CharacterDAO dao){
        List<Party> newParty = new ArrayList<>();
        for(Party c: parties){
            int rand =(int)Math.floor(Math.random() * (8) + 1); // for now this is like adventurer/warrior
            if(Objects.equals(c.getCharacter(dao).getName(), charName)){
                Character ca = new Character(c.getCharacter(dao).getName(),c.getCharacter(dao).getPlayer(),c.getCharacter(dao).getXp(),c.getCharacter(dao).getBody(),c.getCharacter(dao).getMind(),c.getCharacter(dao).getSpirit(),c.getCharacter(dao).getCharClass());
                newParty.add(new Party(ca,c.getHitPoint()+rand,dao,0));
            }else
                newParty.add(c);
        }
        return newParty;
    }

}
