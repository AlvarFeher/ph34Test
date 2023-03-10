package persistence.API;

import business.entities.Adventure;
import business.entities.Party;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import persistence.AdventureDAO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AdventureApiDAO implements AdventureDAO {

    private ApiHelper apiHelper;
    private final String base_url = "https://balandrau.salle.url.edu/dpoo/S1-Project_ICE10/adventures";

    /**
     * Constructor initializing a new object of Api Helper
     */
    public AdventureApiDAO() {
        try {
            apiHelper = new ApiHelper();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * adds an adventure object to the database via a POST METHOD in the API
     * @param adventure adventure object
     * @return 0
     */
    @Override
    public int add(Adventure adventure) {
        try {
            String body = new Gson().toJson(adventure);
            apiHelper.postToUrl(base_url, body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * get all the adventures stored in the API server
     * @return a list of all the adventures available
     */
    @Override
    public List<Adventure> getAll() {
        try {
            String all = apiHelper.getFromUrl(base_url);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Adventure>>() {}.getType();
            return gson.fromJson(all, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get an adventure object whose name is str
     * @param str name of the adventure
     * @return adventure abject if exists, null otherwise
     */
    @Override
    public Adventure getAdventureByName(String str) {
        try {
            String s = apiHelper.getFromUrl(base_url + "?name="+str);
            Gson gson = new Gson();
            Type type = new TypeToken<Adventure>() {}.getType();
            return gson.fromJson(s , type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void deleteAll() {
        try {
            apiHelper.deleteFromUrl(base_url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteByName(String current_adventure) {
        try {
            apiHelper.deleteFromUrl(base_url + "?name="+current_adventure);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Adventure adventure) {

    }

    @Override
    public String getNameByIndex(int inx) {
        return null;
    }

    @Override
    public int getNumOfEncountersByName(String currentAdventure) {
        return 0;
    }

    @Override
    public List<String> getMonstersInEncounter(int i, String currentAdventure) {
        return null;
    }

    @Override
    public List<Party> getPartyByName(String currentAdventure) {
        return null;
    }

    @Override
    public boolean isNameMonster(String currentAdventure, int encounter_pos, String name) {
        return false;
    }

    @Override
    public int getDamageDiceByName(String currentAdventure, int encounter_pos, String name) {
        return 0;
    }

    @Override
    public String getCharacterClassByName(String currentAdventure, String name) {
        return null;
    }

    @Override
    public int getCharactersBodyByName(String currentAdventure, String name) {
        return 0;
    }

    @Override
    public boolean areMonstersAllDead(String currentAdventure, int encounter_pos) {
        return false;
    }

    @Override
    public boolean arePartyAllUnconscious(String currentAdventure) {
        return false;
    }

    @Override
    public int getXpGainedInEncounter(String adventure_name, int encounter_pos) {
        return 0;
    }

    @Override
    public int getCharactersMindByName(String adventure_name, String partyName) {
        return 0;
    }

    @Override
    public int getAdventuresSize() {
        return 0;
    }

    @Override
    public boolean isMonsterDeadByPosition(String currentAdventure, int encounterPos, int monster_pos) {
        return false;
    }

    @Override
    public boolean isPartyUnconsciousByName(String currentAdventure, String s) {
        return false;
    }

    @Override
    public boolean isMonsterAlive(String currentAdventure, int encounter_pos, String s) {
        return false;
    }

    @Override
    public boolean isPartyUnconsciousByPosition(String currentAdventure, int party_pos) {
        return false;
    }

    @Override
    public int getCharactersSpiritByName(String currentAdventure, String name) {
        return 0;
    }

    @Override
    public int getCharactersXpByName(String currentAdventure, String name) {
        return 0;
    }
}
