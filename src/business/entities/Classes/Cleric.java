package business.entities.Classes;

import business.entities.Character;

public class Cleric extends Character {
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
    public Cleric(String name, String player, int xp, int body, int mind, int spirit, String charClass) {
        super(name, player, xp, body, mind, spirit, charClass);
    }

    @Override
    public int doAction() {
        return 0;
    }

    @Override
    public int doAction(int healingNeeded, int param2) {
        if(healingNeeded == 1){
            return prayerOfHealing(getMind());
        } else {
            return notOnMyWatch(getSpirit());
        }
    }


    public int prayerOfHealing(int mind){
        return (int)Math.floor(Math.random() * (10) + 1) + mind;
    }

    public int notOnMyWatch(int spirit){
        return (int)Math.floor(Math.random() * (4) + 1) + spirit;
    }

}
