package business.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * class representing the adventure object
 */
public class Adventure {

    private String name;
    private int num_encounters;
    private List<List<Monster>> encounters;
    private List<Party> parties;

    /**
     * a constructor without parties
     * @param name name of the adventure
     * @param num_encounters number of encounters
     * @param encounters a list of another list of monsters
     */
    public Adventure(String name, int num_encounters, List<List<Monster>> encounters) {
        this.name = name;
        this.num_encounters = num_encounters;
        this.encounters = encounters;
    }

    /**
     * a constructor with all the attributes
     * @param name name of the adventure
     * @param num_encounters number of encounters
     * @param encounters a list of another list of monsters
     * @param parties a list of characters with their hit point
     */
    public Adventure(String name, int num_encounters, List<List<Monster>> encounters, List<Party> parties) {
        this.name = name;
        this.num_encounters = num_encounters;
        this.encounters = encounters;
        this.parties = parties;
    }

    /**
     * name getter
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * number of encounters getter
     * @return number of encounters
     */
    public int getNum_encounters() {
        return num_encounters;
    }

    /**
     * encounters getter
     * @return encounters
     */
    public List<List<Monster>> getEncounters() {
        return encounters;
    }

    /**
     * parties getter
     * @return parties
     */
    public List<Party> getParties() {
        return parties;
    }


}
