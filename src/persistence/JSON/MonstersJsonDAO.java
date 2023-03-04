package persistence.JSON;
import business.entities.Monster;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import persistence.MonsterDAO;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * gets, adds or modifies data from the monster json file
 * @author Youssef Bat, Alvaro Feher
 */

public class MonstersJsonDAO implements MonsterDAO {

    private static final String path = "data/monsters.json";




    /**
     * default constructor
     */
    public MonstersJsonDAO() {
    }


    /**
     * get all the monsters in the dataset
     * @return a list of all the monsters
     */
    @Override
    public List<Monster> getAll() {
        JsonReader reader = null;
        List<Monster> list;
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Monster>>() {}.getType());
            if (list == null) {
                list = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            return null;
        }
        return list;
    }

    /**
     * gets the initial value of a monster by its name
     * @param monster_name name of monster
     * @return the initial value of a monster
     */
    @Override
    public int getInitValueByName(String monster_name) {
        JsonReader reader ;
        List<Monster> list;
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Monster>>() {}.getType());
            if (list == null) {
                return Integer.MIN_VALUE;
            }
        } catch (FileNotFoundException e) {
            return Integer.MIN_VALUE;
        }
        for (Monster monster : list) {
            if (Objects.equals(monster_name, monster.getName())) {
                return monster.getInitiative();
            }
        }
        return Integer.MIN_VALUE;
    }


}
