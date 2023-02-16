package persistence.API;

import business.entities.Monster;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MonsterApiDAO {

    private ApiHelper apiHelper;
    private final String base_url = "https://balandrau.salle.url.edu/dpoo/shared/monsters";

    /**
     * constructor
     */
    public MonsterApiDAO() {
        try {
            apiHelper = new ApiHelper();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * retrieve all the monsters that are stored in the cloud
     * @return a list of all the monsters in the cloud
     */
    public List<Monster> getAll() {
        try {
            String all = apiHelper.getFromUrl(base_url);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Monster>>() {}.getType();
            return gson.fromJson(all , type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get all the monsters with a certain initial value
     * @param init_val initial value
     * @return a list of all the monsters with the initiative value given
     */
    public List<Monster> getByInitiative(int init_val) {
        try {
            String s = apiHelper.getFromUrl(base_url + "?initiative="+init_val);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Monster>>() {}.getType();
            return gson.fromJson(s , type);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get the initiative value of a monster given its name
     * @param monster_name name of the monster
     * @return the initiative value
     */
    public int getInitValueByName(String monster_name) {
        try {
            String s =  apiHelper.getFromUrl(base_url + "?name="+monster_name);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Monster>>() {}.getType();
            List<Monster> monster = gson.fromJson(s, type);
            return monster.get(0).getInitiative();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.MIN_VALUE;
    }
}
