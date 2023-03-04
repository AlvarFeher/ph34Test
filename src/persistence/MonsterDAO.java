package persistence;

import business.entities.Monster;

import java.util.List;

/**
 * interface of monster json data access object
 */
public interface MonsterDAO {

    /**
     * get all the monsters in the dataset
     * @return a list of all the monsters
     */
    List<Monster> getAll();

    /**
     * gets the initial value of a monster by its name
     * @param monster_name name of monster
     * @return the initial value of a monster
     */
    int getInitValueByName(String monster_name);
}
