package business.entities.Classes;

import business.entities.Character;

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




}
