package business.entities.Classes;

import business.entities.Character;
import business.entities.Party;
import persistence.CharacterDAO;
import persistence.JSON.CharacterJsonDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Champion extends Character {
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
    public Champion(String name, String player, int xp, int body, int mind, int spirit, String charClass) {
        super(name, player, xp, body, mind, spirit, charClass);
    }

    @Override
    public int doAction() {
        return (int)Math.floor(Math.random() * (10) + 1) + getBody();
    } // improved sword slash

    @Override
    public int doAction(int param1, int param2) {
        return 0;
    }

    // champions add 1 spirit to everyone but themselves
    @Override
    public List<Party> preparationStageAction(List<Party> party, String charName, CharacterDAO dao) {
        List<Party> newParty = new ArrayList<>();
        for(Party c: party){
            if(!Objects.equals(c.getCharacter(dao).getName(), charName)){
                newParty.add(new Party(new Character(c.getCharacter(dao).getName(),c.getCharacter(dao).getPlayer(),c.getCharacter(dao).getXp(),c.getCharacter(dao).getBody(),c.getCharacter(dao).getMind(),c.getCharacter(dao).getSpirit()+1,c.getCharacter(dao).getCharClass()),c.getHitPoint(),dao));
            }else
                newParty.add(c);
        }
        return newParty;
    }

    // FIXME: the champion heals to 100% hp, we need maxHitPoints as attribute
    @Override
    public List<Party> shortRestAction(List<Party> parties, String charName, CharacterDAO dao){
        List<Party> newParty = new ArrayList<>();
        for(Party c: parties){
            int rand =(int)Math.floor(Math.random() * (8) + 1); // for now this is like adventurer/warrior
            if(Objects.equals(c.getCharacter(dao).getName(), charName)){
                Character ca = new Character(c.getCharacter(dao).getName(),c.getCharacter(dao).getPlayer(),c.getCharacter(dao).getXp(),c.getCharacter(dao).getBody(),c.getCharacter(dao).getMind(),c.getCharacter(dao).getSpirit(),c.getCharacter(dao).getCharClass());
                newParty.add(new Party(ca,c.getHitPoint()+rand,dao));
            }else
                newParty.add(c);
        }
        return newParty;
    }



}
