package persistence.API;

import business.entities.Adventure;
import business.entities.Party;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import persistence.AdventureDAO;
import persistence.JSON.CharacterJsonDAO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            Type type = new TypeToken<List<Adventure>>() {}.getType();
            List<Adventure> list = gson.fromJson(s, type);
            return list.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * deletes an adventure by giving its name as parameter
     * @param current_adventure name of adventure to delete
     */
    private void deleteByName(String current_adventure) {
        try {
            apiHelper.deleteFromUrl(base_url + "?name="+current_adventure);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * updates an adventure
     * @param adventure adventure object
     */
    @Override
    public void update(Adventure adventure) {
        deleteByName(adventure.getName());
        add(adventure);
    }
    /**
     * gets the name of the adventure if exists
     * @param inx position of the adventure
     * @return the name of the adventure if exists
     */
    @Override
    public String getNameByIndex(int inx) {
        try {
            String s = apiHelper.getFromUrl(base_url + "/"+inx);
            Gson gson = new Gson();
            Type type = new TypeToken<Adventure>() {}.getType();
            Adventure list = gson.fromJson(s, type);
            return list.getName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * gets the number of encounters of the adventure if exists
     * @param currentAdventure name of the adventure
     * @return the number of encounters of the adventure if exists
     */
    @Override
    public int getNumOfEncountersByName(String currentAdventure) {
        try {
            String s = apiHelper.getFromUrl(base_url + "?name="+currentAdventure);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Adventure>>() {}.getType();
            List<Adventure> list = gson.fromJson(s, type);
            return list.get(0).getNum_encounters();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.MIN_VALUE;
    }
    /**
     * gets all the names of monsters in an encounter of the adventure if exists
     * @param i position of the encounter
     * @param currentAdventure name of the adventure
     * @return all the names of monsters in an encounter of the adventure if exists
     */
    @Override
    public List<String> getMonstersInEncounter(int i, String currentAdventure) {
        try {
            String s = apiHelper.getFromUrl(base_url + "?name="+currentAdventure);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Adventure>>() {}.getType();
            List<Adventure> list = gson.fromJson(s, type);
            List<String> names = new ArrayList<>();
            for (int j=0;j<list.get(0).getEncounters().get(i).size();j++) {
                names.add(list.get(0).getEncounters().get(i).get(j).getName());
            }
            return names;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * gets all the parties of the adventure if exists
     * @param currentAdventure name of the adventure
     * @return list of parties objects of the adventure if exists
     */
    @Override
    public List<Party> getPartyByName(String currentAdventure) {
        try {
            String s = apiHelper.getFromUrl(base_url + "?name="+currentAdventure);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Adventure>>() {}.getType();
            List<Adventure> list = gson.fromJson(s, type);
            return list.get(0).getParties();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * looks if a monster is in an encounter of an adventure and is a boss or not
     * @param currentAdventure name of the adventure
     * @param encounter_pos the encounter position
     * @param name the name of the monster
     * @return if a monster is in an encounter of an adventure and is a boss or not
     */
    @Override
    public boolean isNameMonster(String currentAdventure, int encounter_pos, String name) {
        try {
            String s = apiHelper.getFromUrl(base_url + "?name="+currentAdventure);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Adventure>>() {}.getType();
            List<Adventure> list = gson.fromJson(s, type);
            for (int i=0;i<list.get(0).getEncounters().get(encounter_pos).size();i++) {
                if (Objects.equals(list.get(0).getEncounters().get(encounter_pos).get(i).getName(), name)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * get the damage dice of a monster in an encounter of an adventure
     * @param currentAdventure name of the adventure
     * @param encounter_pos the encounter position
     * @param name the name of the monster
     * @return the damage dice of a monster in an encounter of an adventure
     */
    @Override
    public int getDamageDiceByName(String currentAdventure, int encounter_pos, String name) {
        try {
            String s = apiHelper.getFromUrl(base_url + "?name="+currentAdventure);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Adventure>>() {}.getType();
            List<Adventure> list = gson.fromJson(s, type);
            for (int i=0;i<list.get(0).getEncounters().get(encounter_pos).size();i++) {
                if (Objects.equals(list.get(0).getEncounters().get(encounter_pos).get(i).getName(), name)) {
                    return list.get(0).getEncounters().get(encounter_pos).get(i).getDamageDice();
                }
            }
            return Integer.MIN_VALUE;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.MIN_VALUE;
    }
    /**
     * checks if all monsters in an encounter of an adventure are dead
     * @param currentAdventure name of the adventure
     * @param encounter_pos the encounter position
     * @return true if all monsters in an encounter of an adventure are dead
     */
    @Override
    public boolean areMonstersAllDead(String currentAdventure, int encounter_pos) {
        try {
            String s = apiHelper.getFromUrl(base_url + "?name="+currentAdventure);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Adventure>>() {}.getType();
            List<Adventure> list = gson.fromJson(s, type);
            for (int i=0;i<list.get(0).getEncounters().get(encounter_pos).size();i++) {
                if (list.get(0).getEncounters().get(encounter_pos).get(i).getHitPoints() > 0) {
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    /**
     * checks if all characters in an adventure are unconscious
     * @param currentAdventure name of the adventure
     * @return true if all characters in an adventure are unconscious
     */
    @Override
    public boolean arePartyAllUnconscious(String currentAdventure) {
        try {
            String s = apiHelper.getFromUrl(base_url + "?name="+currentAdventure);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Adventure>>() {}.getType();
            List<Adventure> list = gson.fromJson(s, type);
            for (int i=0;i<list.get(0).getParties().size();i++) {
                if (list.get(0).getParties().get(i).getHitPoint() > 0) {
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    /**
     * get the xp gained in an encounter of an adventure
     * @param adventure_name name of the adventure
     * @param encounter_pos the encounter position
     * @return the xp gained in an encounter of an adventure
     */
    @Override
    public int getXpGainedInEncounter(String adventure_name, int encounter_pos) {
        try {
            String s = apiHelper.getFromUrl(base_url + "?name="+adventure_name);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Adventure>>() {}.getType();
            List<Adventure> list = gson.fromJson(s, type);
            int sum = 0;
            for (int i=0;i<list.get(0).getEncounters().get(encounter_pos).size();i++) {
                sum += list.get(0).getEncounters().get(encounter_pos).get(i).getExperience();
            }
            return sum;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.MIN_VALUE;
    }

    /**
     * gets the character mind in an adventure
     * @param adventure_name name of the adventure
     * @param partyName name of the character
     * @return the character mind in an adventure
     */
    @Override
    public int getCharactersMindByName(String adventure_name, String partyName) {
        return 0;
    }

    /**
     * gets the number of adventures
     * @return the number of adventures
     */
    @Override
    public int getAdventuresSize() {
        try {
            String all = apiHelper.getFromUrl(base_url);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Adventure>>() {}.getType();
            List<Adventure> list = gson.fromJson(all, type);
            return list.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * checks if a specific character ( by name ) in an adventure is unconscious
     * @param currentAdventure name of the adventure
     * @param name the party name
     * @return true if the character is unconscious
     */
    @Override
    public boolean isPartyUnconsciousByName(String currentAdventure, String name) {
        try {
            String s = apiHelper.getFromUrl(base_url + "?name="+currentAdventure);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Adventure>>() {}.getType();
            List<Adventure> list = gson.fromJson(s, type);
            for (int i=0;i<list.get(0).getParties().size();i++) {
                if (Objects.equals(list.get(0).getParties().get(i).getCharacter(new CharacterApiDAO()).getName(), name)) {
                    return list.get(0).getParties().get(i).getHitPoint() <= 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * checks if a specific monster ( by name ) in an encounter of an adventure is alive
     * @param currentAdventure name of the adventure
     * @param encounter_pos the encounter position
     * @param str the monster name
     * @return true if monster is alive
     */
    @Override
    public boolean isMonsterAlive(String currentAdventure, int encounter_pos, String str) {
        try {
            String s = apiHelper.getFromUrl(base_url + "?name="+currentAdventure);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Adventure>>() {}.getType();
            List<Adventure> list = gson.fromJson(s, type);
            for (int i=0;i<list.get(0).getEncounters().get(encounter_pos).size();i++) {
                if (Objects.equals(list.get(0).getEncounters().get(encounter_pos).get(i).getName(), str)) {
                    if (list.get(0).getEncounters().get(encounter_pos).get(i).getHitPoints() > 0) {
                        return true;
                    }
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * checks if a specific character (by position )in an adventure is unconscious
     * @param currentAdventure name of the adventure
     * @param party_pos the party position
     * @return true if the character is unconscious
     */
    @Override
    public boolean isPartyUnconsciousByPosition(String currentAdventure, int party_pos) {
        try {
            String s = apiHelper.getFromUrl(base_url + "?name="+currentAdventure);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Adventure>>() {}.getType();
            List<Adventure> list = gson.fromJson(s, type);
            return list.get(0).getParties().get(party_pos).getHitPoint() <= 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
