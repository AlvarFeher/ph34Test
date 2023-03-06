package business.entities.Classes;

import business.entities.Character;

public class Paladin extends Character {
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


}
