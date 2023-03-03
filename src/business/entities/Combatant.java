package business.entities;

/**
 * combatant entity
 */
public class Combatant {
    private String name;
    private int initValue;
    private boolean isAlive;

    /**
     * constructor
     * @param name combatant name
     * @param initValue initial value
     */
    public Combatant(String name, int initValue) {
        this.name = name;
        this.initValue = initValue;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    /**
     * name getter
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * initial value getter
     * @return initial value
     */
    public int getInitValue() {
        return initValue;
    }


}
