package business.entities;

/**
 * combatant entity
 */
public class Combatant {
    private String name;
    private int initValue;

    /**
     * constructor
     * @param name combatant name
     * @param initValue initial value
     */
    public Combatant(String name, int initValue) {
        this.name = name;
        this.initValue = initValue;
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
