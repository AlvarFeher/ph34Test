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

    private void deleteByName(String current_adventure) {
        try {
            apiHelper.deleteFromUrl(base_url + "?name="+current_adventure);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Adventure adventure) {
        deleteByName(adventure.getName());
        add(adventure);
    }

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

    @Override
    public int getCharactersMindByName(String adventure_name, String partyName) {
        return 0;
    }

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
