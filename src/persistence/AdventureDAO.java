package persistence;

import business.entities.Adventure;
import business.entities.Party;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * gets, adds or modifies data from the adventure json file
 * @author Youssef Bat, Alvaro Feher
 */

public class AdventureDAO implements AdventureJsonDAO{

    private static final String path = "data/adventures.json";

    // API DAO FOR ADVENTURE

    

    // LOCAL STORAGE FOR ADVENTURE

    /**
     * constructor where we create a new file in case of the file does not exist
     */
    public AdventureDAO() {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * add an adventure to the database
     * @param adventurer adventure object
     * @return if the adventure is added or not
     */
    @Override
    public int add(Adventure adventurer) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonReader reader = new JsonReader(new FileReader(path));
            Type type = new TypeToken<List<Adventure>>() {}.getType();
            List<Adventure> list = gson.fromJson(reader, type);
            reader.close();
            if (list != null) {
                list.add(adventurer);
            }
            else {
                list = new ArrayList<>();
                list.add(adventurer);
            }

            FileWriter writer = new FileWriter(path);
            writer.write(gson.toJson(list));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * gets all the adventures
     * @return list of adventure objects
     */
    @Override
    public List<Adventure> getAll() {
        JsonReader reader = null;
        List<Adventure> list;
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Adventure>>() {}.getType());
            if (list == null) {
                list = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            return null;
        }
        return list;
    }

    /**
     * gets an object of adventure which has the name in the parameter
     * @param str name of the adventure
     * @return an object of adventure
     */
    @Override
    public Adventure getAdventureByName(String str) {
        JsonReader reader = null;
        List<Adventure> list;
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Adventure>>() {}.getType());
            if (list == null) {
                return null;
            }
            for (Adventure adventure : list) {
                if (Objects.equals(adventure.getName(), str)) {
                    return adventure;
                }
            }
        } catch (FileNotFoundException e) {
            return null;
        }
        return null;
    }

    /**
     * updates an adventure by another adventure
     * @param adventureToUpdate adventure object
     */
    public void update(Adventure adventureToUpdate){
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonReader reader = new JsonReader(new FileReader(path));
            Type type = new TypeToken<List<Adventure>>() {}.getType();
            List<Adventure> adventures = gson.fromJson(reader, type);
            reader.close();

            int i =0;
            for (Adventure a: adventures){
              if(Objects.equals(a.getName(), adventureToUpdate.getName())){
                  adventures.set(i,adventureToUpdate);
              }
              i++;
            }

            FileWriter writer = new FileWriter(path);
            writer.write(gson.toJson(adventures));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * gets the name of the adventure if exists
     * @param inx position of the adventure
     * @return the name of the adventure if exists
     */
    public String getNameByIndex(int inx) {
        JsonReader reader = null;
        List<Adventure> list;
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Adventure>>() {}.getType());
            if (list == null) {
                list = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            return null;
        }
        return list.get(inx).getName();
    }

    /**
     * gets the number of encounters of the adventure if exists
     * @param currentAdventure name of the adventure
     * @return the number of encounters of the adventure if exists
     */
    @Override
    public int getNumOfEncountersByName(String currentAdventure) {
        JsonReader reader = null;
        List<Adventure> list;
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Adventure>>() {}.getType());
            if (list == null) {
                list = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            return Integer.MIN_VALUE;
        }
        for (Adventure adventure : list) {
            if (Objects.equals(adventure.getName(), currentAdventure)) {
                return adventure.getNum_encounters();
            }
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
        JsonReader reader;
        List<Adventure> list;
        List<String> monsters = new ArrayList<>();
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Adventure>>() {}.getType());
            if (list == null) {
                return new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            return null;
        }
        for (Adventure adventure : list) {
            if (Objects.equals(currentAdventure, adventure.getName())) {
                for (int l = 0; l < adventure.getEncounters().get(i).size(); l++) {
                    monsters.add(adventure.getEncounters().get(i).get(l).getName());
                }
            }
        }
        return monsters;
    }


    /**
     * gets all the parties of the adventure if exists
     * @param currentAdventure name of the adventure
     * @return list of parties objects of the adventure if exists
     */
    @Override
    public List<Party> getPartyByName(String currentAdventure) {
        JsonReader reader = null;
        List<Adventure> list;
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Adventure>>() {}.getType());
            if (list == null) {
                list = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        }
        for (Adventure adventure : list) {
            if (Objects.equals(adventure.getName(), currentAdventure)) {
                return adventure.getParties();
            }
        }
        return new ArrayList<>();
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
        JsonReader reader = null;
        List<Adventure> list;
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Adventure>>() {}.getType());
            if (list == null) {
                list = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            return false;
        }
        for (Adventure adventure : list) {
            if (Objects.equals(adventure.getName(), currentAdventure)) {
                for (int i=0;i<adventure.getEncounters().get(encounter_pos).size();i++) {
                    if (Objects.equals(adventure.getEncounters().get(encounter_pos).get(i).getName(), name)) {
                        return true;
                    }
                }
            }
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
        JsonReader reader = null;
        List<Adventure> list;
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Adventure>>() {}.getType());
            if (list == null) {
                list = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            return Integer.MIN_VALUE;
        }
        for (Adventure adventure : list) {
            if (Objects.equals(adventure.getName(), currentAdventure)) {
                for (int i=0;i<adventure.getEncounters().get(encounter_pos).size();i++) {
                    if (Objects.equals(adventure.getEncounters().get(encounter_pos).get(i).getName(), name)) {
                        return adventure.getEncounters().get(encounter_pos).get(i).getDamageDice();
                    }
                }
            }
        }
        return Integer.MIN_VALUE;
    }

    /**
     * get the character class in an adventure
     * @param currentAdventure name of the adventure
     * @param name the name of the character
     * @return the character class in an adventure
     */
    @Override
    public String getCharacterClassByName(String currentAdventure, String name) {
        JsonReader reader = null;
        List<Adventure> list;
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Adventure>>() {}.getType());
            if (list == null) {
                list = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            return null;
        }
        for (Adventure adventure : list) {
            if (Objects.equals(adventure.getName(), currentAdventure)) {
                for (int j = 0; j < adventure.getParties().size(); j++) {
                    if (Objects.equals(adventure.getParties().get(j).getCharacter().getName(), name)) {
                        return adventure.getParties().get(j).getCharacter().getCharClass();
                    }
                }
            }
        }
        return null;
    }

    /**
     * get the character body in an adventure
     * @param currentAdventure name of the adventure
     * @param name name of the character
     * @return the character body in an adventure
     */
    @Override
    public int getCharactersBodyByName(String currentAdventure, String name) {
        JsonReader reader = null;
        List<Adventure> list;
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Adventure>>() {}.getType());
            if (list == null) {
                list = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            return Integer.MIN_VALUE;
        }
        for (Adventure adventure : list) {
            if (Objects.equals(adventure.getName(), currentAdventure)) {
                for (int j = 0; j < adventure.getParties().size(); j++) {
                    if (Objects.equals(adventure.getParties().get(j).getCharacter().getName(), name)) {
                        return adventure.getParties().get(j).getCharacter().getBody();
                    }
                }
            }
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
        JsonReader reader = null;
        List<Adventure> list;
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Adventure>>() {}.getType());
            if (list == null) {
                list = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            return false;
        }
        int count = 0;
        int size = 0;
        for (Adventure adventure : list) {
            if (Objects.equals(adventure.getName(), currentAdventure)) {
                size = adventure.getEncounters().get(encounter_pos).size();
                for (int i=0;i<size;i++) {
                    if (adventure.getEncounters().get(encounter_pos).get(i).getHitPoints() < 1) {
                        count++;
                    }
                }
            }
        }
        return count == size;
    }

    /**
     * checks if all characters in an adventure are unconscious
     * @param currentAdventure name of the adventure
     * @return true if all characters in an adventure are unconscious
     */
    @Override
    public boolean arePartyAllUnconscious(String currentAdventure) {
        JsonReader reader = null;
        List<Adventure> list;
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Adventure>>() {}.getType());
            if (list == null) {
                list = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            return false;
        }
        int size = 0;
        int count = 0;
        for (Adventure adventure : list) {
            if (Objects.equals(adventure.getName(), currentAdventure)) {
                size = adventure.getParties().size();
                for (int j = 0; j < size; j++) {
                    if (adventure.getParties().get(j).getHitPoint() < 1) {
                        count++;
                    }
                }
            }
        }
        return count == size;
    }

    /**
     * get the xp gained in an encounter of an adventure
     * @param adventure_name name of the adventure
     * @param encounter_pos the encounter position
     * @return the xp gained in an encounter of an adventure
     */
    @Override
    public int getXpGainedInEncounter(String adventure_name, int encounter_pos) {
        JsonReader reader = null;
        List<Adventure> list;
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Adventure>>() {}.getType());
            if (list == null) {
                list = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            return Integer.MIN_VALUE;
        }
        int size = 0;
        int sum_xp = 0;
        for (Adventure adventure : list) {
            if (Objects.equals(adventure.getName(), adventure_name)) {
                for (int i=0;i<adventure.getEncounters().get(encounter_pos).size();i++) {
                    if (adventure.getEncounters().get(encounter_pos).get(i).getHitPoints() > 0) {
                        sum_xp += adventure.getEncounters().get(encounter_pos).get(i).getExperience();
                        size++;
                    }
                }
            }
        }
        return sum_xp * size;
    }

    /**
     * gets the character mind in an adventure
     * @param currentAdventure name of the adventure
     * @param partyName name of the character
     * @return the character mind in an adventure
     */
    @Override
    public int getCharactersMindByName(String currentAdventure, String partyName) {
        JsonReader reader = null;
        List<Adventure> list;
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Adventure>>() {}.getType());
            if (list == null) {
                list = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            return Integer.MIN_VALUE;
        }
        for (Adventure adventure : list) {
            if (Objects.equals(adventure.getName(), currentAdventure)) {
                for (int j = 0; j < adventure.getParties().size(); j++) {
                    if (Objects.equals(adventure.getParties().get(j).getCharacter().getName(), partyName)) {
                        return adventure.getParties().get(j).getCharacter().getMind();
                    }
                }
            }
        }
        return Integer.MIN_VALUE;
    }

    /**
     * gets the number of adventures
     * @return the number of adventures
     */
    @Override
    public int getAdventuresSize() {
        JsonReader reader = null;
        List<Adventure> list;
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Adventure>>() {}.getType());
            if (list == null) {
                list = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            return Integer.MIN_VALUE;
        }
        return list.size();
    }

    /**
     * checks if a specific monster (by position) in an encounter of an adventure is dead
     * @param currentAdventure name of the adventure
     * @param encounterPos the encounter position
     * @param monster_pos the monster position
     * @return true if the monster is dead
     */
    @Override
    public boolean isMonsterDeadByPosition(String currentAdventure, int encounterPos, int monster_pos) {
        JsonReader reader = null;
        List<Adventure> list;
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Adventure>>() {}.getType());
            if (list == null) {
                list = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            return false;
        }
        for (Adventure adventure : list) {
            if (Objects.equals(adventure.getName(), currentAdventure)) {
                if (adventure.getEncounters().get(encounterPos).get(monster_pos).getHitPoints() < 1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * checks if a specific character ( by name ) in an adventure is unconscious
     * @param currentAdventure name of the adventure
     * @param s the party name
     * @return true if the character is unconscious
     */
    @Override
    public boolean isPartyUnconsciousByName(String currentAdventure, String s) {
        JsonReader reader = null;
        List<Adventure> list;
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Adventure>>() {}.getType());
            if (list == null) {
                list = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            return false;
        }
        for (Adventure adventure : list) {
            if (Objects.equals(adventure.getName(), currentAdventure)) {
                for (int j = 0; j < adventure.getParties().size(); j++) {
                    if (Objects.equals(adventure.getParties().get(j).getCharacter().getName(), s)) {
                        if(adventure.getParties().get(j).getHitPoint() < 1) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * checks if a specific monster ( by name ) in an encounter of an adventure is alive
     * @param currentAdventure name of the adventure
     * @param encounter_pos the encounter position
     * @param s the monster name
     * @return true if monster is alive
     */
    @Override
    public boolean isMonsterAlive(String currentAdventure, int encounter_pos, String s) {
        JsonReader reader = null;
        List<Adventure> list;
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Adventure>>() {}.getType());
            if (list == null) {
                list = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            return false;
        }
        for (Adventure adventure : list) {
            if (Objects.equals(adventure.getName(), currentAdventure)) {
                for (int i=0;i<adventure.getEncounters().get(encounter_pos).size();i++) {
                    if (Objects.equals(adventure.getEncounters().get(encounter_pos).get(i).getName(), s)) {
                        if (adventure.getEncounters().get(encounter_pos).get(i).getHitPoints() > 0) {
                            return true;
                        }
                    }
                }
            }
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
        JsonReader reader = null;
        List<Adventure> list;
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Adventure>>() {}.getType());
            if (list == null) {
                list = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            return false;
        }
        for (Adventure adventure : list) {
            if (Objects.equals(adventure.getName(), currentAdventure)) {
                if (adventure.getParties().get(party_pos).getHitPoint() < 1) {
                    return true;
                }
            }
        }
        return false;
    }


}
