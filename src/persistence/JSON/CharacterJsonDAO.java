package persistence.JSON;

import business.entities.Character;
import business.entities.Classes.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import persistence.CharacterDAO;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * gets, adds or modifies data from the character json file
 * @author Youssef Bat, Alvaro Feher
 */

public class CharacterJsonDAO implements CharacterDAO {
    private static final String path = "data/characters.json";

    /**
     * default constructor
     */
    public CharacterJsonDAO() {
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
     * adds a character in the database
     * @param character character object
     * @return if character is added
     */
    @Override
    public int add(Character character) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonReader reader = new JsonReader(new FileReader(path));
            Type type = new TypeToken<List<Character>>() {}.getType();
            List<Character> list = gson.fromJson(reader, type);
            reader.close();
            if (list == null) {
                list = new ArrayList<>();
            }
            for (Character value : list) {
                if (Objects.equals(value.getName(), character.getName())) {
                    return 0;
                }
            }
            list.add(character);
            FileWriter writer = new FileWriter(path);
            writer.write(gson.toJson(list));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * deletes a character by its name
     * @param str character name
     */
    @Override
    public void delete(String str) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonReader reader = new JsonReader(new FileReader(path));
            Type type = new TypeToken<List<Character>>() {}.getType();
            List<Character> list = gson.fromJson(reader, type);
            reader.close();
            int index = -1;
            for (int i=0;i< list.size();i++) {
                if (Objects.equals(list.get(i).getName(), str)) {
                    index = i;
                }
            }
            list.remove(index);
            FileWriter writer = new FileWriter(path);
            writer.write(gson.toJson(list));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * gets a list of all the characters
     * @return a list of all the characters
     */
    @Override
    public List<Character> getAll() {
        JsonReader reader = null;
        List<Character> list;
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Character>>() {}.getType());
            if (list == null) {
                list = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            return null;
        }
        return list;
    }

    /**
     * gets a list of all the character that their player's name is a whole of str
     * @param str a substring of player's name
     * @return a list of all the character that their player's name is a whole of str
     */
    @Override
    public List<Character> getCharactersByPart(String str) {
        JsonReader reader = null;
        List<Character> list;
        List<Character> names = new ArrayList<>();
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Character>>() {}.getType());
            if (list == null) {
                return null;
            }
        } catch (FileNotFoundException e) {
            return null;
        }
        for (Character character : list) {
            if (Objects.equals(str, "")) {
                names.add(character);
            }
            if (character.getPlayer().toLowerCase().contains(str.toLowerCase()) && !str.isEmpty()) {
                names.add(character);
            }
        }
        return names;
    }

    /**
     * gets a list of all the character names
     * @return a list of all the character names
     */
    @Override
    public List<String> getCharactersNames() {
        JsonReader reader;
        List<Character> list;
        List<String> names = new ArrayList<>();
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Character>>() {}.getType());
            if (list == null) {
                list = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            return null;
        }
        for (Character character : list) {
            names.add(character.getName());
        }
        return names;
    }

    /**
     * get a list of characters that are positioned in the value of the parties_inx array
     * @param parties_inx an array containing the indexes of the characters
     * @return the character objects referring to their indexes
     */
    @Override
    public List<Character> getCharactersByIndexes(int[] parties_inx) {
        JsonReader reader = null;
        List<Character> list;
        List<Character> characterList = new ArrayList<>();
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Character>>() {}.getType());
            if (list == null) {
                return null;
            }
        } catch (FileNotFoundException e) {
            return null;
        }
        for (int partiesInx : parties_inx) {
            characterList.add(list.get(partiesInx));
        }
        return characterList;
    }

    /**
     * update the dataset of characters
     * @param new_characterList a list of all the characters
     */
    @Override
    public void update(List<Character> new_characterList) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(path);
            writer.write(gson.toJson(new_characterList));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get the character's spirit by the character's name
     * @param partyName the name of the charater
     * @return the character's spirit
     */
    @Override
    public int getCharactersSpiritByName(String partyName) {
        JsonReader reader = null;
        List<Character> list;
        try {
            reader = new JsonReader(new FileReader(path));
            list = new Gson().fromJson(reader, new TypeToken<List<Character>>() {}.getType());
            if (list == null) {
                return Integer.MIN_VALUE;
            }
        } catch (FileNotFoundException e) {
            return Integer.MIN_VALUE;
        }
        for (Character character : list) {
            if (Objects.equals(partyName, character.getName())) {
                return character.getSpirit();
            }
        }
        return Integer.MIN_VALUE;
    }

    public Character assignClass(String name, String player, int xp, int body, int mind, int spirit, String charClass){
        switch(charClass){
            case "Adventurer":
                return  new Adventurer(name, player, xp, body, mind, spirit, charClass);
            case "Warrior":
                return  new Warrior( name, player, xp, body, mind, spirit, charClass);
            case "Champion":
                return  new Champion(name, player, xp, body, mind, spirit, charClass);
            case "Cleric":
                return  new Cleric(name, player, xp, body, mind, spirit, charClass);
            case "Paladin":
                return  new Paladin(name, player, xp, body, mind, spirit, charClass);
            case "Wizard":
                return  new Wizard(name, player, xp, body, mind, spirit, charClass,0);
        }
        return null;
    }

}
