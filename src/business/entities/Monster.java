package business.entities;

/**
 * Monster entity
 */
public class Monster{
    private String name;
    private String challenge;
    private int experience;
    private int hitPoints;
    private int initiative;
    private String damageDice;
    private String damageType;

    /**
     * constructor
     * @param name monster name
     * @param challenge challenge
     * @param experience experience
     * @param hitPoints hit Points
     * @param initiative initiative value
     * @param damageDice damage Dice
     * @param damageType damage Type
     */
    public Monster(String name, String challenge, int experience, int hitPoints, int initiative, String damageDice, String damageType) {
        this.name = name;
        this.challenge = challenge;
        this.experience = experience;
        this.hitPoints = hitPoints;
        this.initiative = initiative;
        this.damageDice = damageDice;
        this.damageType = damageType;
    }


    /**
     * name getter
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * challenge getter
     * @return challenge
     */
    public String getChallenge() {
        return challenge;
    }

    /**
     * xp getter
     * @return xp
     */
    public int getExperience() {
        return experience;
    }

    /**
     * hit points getter
     * @return hit points
     */
    public int getHitPoints() {
        return hitPoints;
    }

    /**
     * initiative value getter
     * @return initiative value
     */
    public int getInitiative() {
        return initiative;
    }

    /**
     * damage type getter
     * @return damage type
     */
    public String getDamageType() {
        return damageType;
    }

    /**
     * damage dice getter but only its digit part
     * @return damage dice as an integer
     */
    public int getDamageDice() {
        return Integer.parseInt(damageDice.split("d")[1]);
    }


}
