package persistence.API;

import business.entities.Character;
import business.entities.Classes.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import persistence.CharacterDAO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CharacterApiDAO implements CharacterDAO {

    private ApiHelper apiHelper;
    private final String url = "https://balandrau.salle.url.edu/dpoo/S1-Project_ICE10/characters";

    /**
     * default constructor
     */
    public CharacterApiDAO(){
        try {
            apiHelper = new ApiHelper();
        } catch (IOException e) {
            e.printStackTrace();
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
            List<Character> characters = getAll();
            for (Character value : characters) {
                if (Objects.equals(value.getName(), character.getName())) {
                    return 0;
                }
            }
            String body = new Gson().toJson(character);
            apiHelper.postToUrl(url, body);
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * deletes a character by its name
     * @param str character name
     */
    @Override
    public void delete(String str) {
        try {
            apiHelper.deleteFromUrl(url + "?name="+str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * retrieve all the characters that are stored in the cloud
     * @return a list of all the characters in the cloud
     */
    @Override
    public List<Character> getAll() {
        try {
            String all = apiHelper.getFromUrl(url);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Character>>() {}.getType();
            return gson.fromJson(all, type);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * get all characters that in their player's name contains str
     * @param str a substring of player's name
     * @return a list of character objects whose players have a name starting with str
     */
    @Override
    public List<Character> getCharactersByPart(String str) {
        try {
            String all = apiHelper.getFromUrl(url + "?player="+str);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Character>>() {}.getType();
            return gson.fromJson(all, type);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * gets a list of all the character names
     * @return a list of all the character names
     */
    @Override
    public List<String> getCharactersNames() {
        try {
            String all = apiHelper.getFromUrl(url);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Character>>() {}.getType();
            List<String> names = new ArrayList<>();
            List<Character> characters = gson.fromJson(all, type);
            for (Character character : characters) {
                names.add(character.getName());
            }
            return names;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * get a list of characters that are positioned in the value of the parties_inx array
     * @param parties_inx an array containing the indexes of the characters
     * @return the character objects referring to their indexes
     */
    @Override
    public List<Character> getCharactersByIndexes(int[] parties_inx) {
        try {
            String all = apiHelper.getFromUrl(url);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Character>>() {}.getType();
            List<Character> characterList = gson.fromJson(all, type);
            List<Character> characters = new ArrayList<>();
            for (int partiesInx : parties_inx) {
                characters.add(characterList.get(partiesInx));
            }
            return characters;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * update the dataset of characters
     * @param new_characterList a list of all the characters
     */
    @Override
    public void update(List<Character> new_characterList) {
        try {
            apiHelper.deleteFromUrl(url);
            String body = new Gson().toJson(new_characterList);
            apiHelper.postToUrl(url, body);
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
        try {
            String all = apiHelper.getFromUrl(url + "?name="+partyName);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Character>>() {}.getType();
            List<Character> list = gson.fromJson(all, type);
            return list.get(0).getSpirit();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.MIN_VALUE;
    }

    /**
     * method to assign a class to a given character by its attributes. converts instance of object depending on its character class attribute
     * @param name character's name
     * @param player player whi created the character
     * @param xp character's xp
     * @param body character's body
     * @param mind character's mind
     * @param spirit character's spirit
     * @param charClass character's class
     * @param shield wizard's shield
     * @return new instance of character depending on its class
     */
    @Override
    public Character assignClass(String name, String player, int xp, int body, int mind, int spirit, String charClass, int shield) {
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
                return  new Wizard(name, player, xp, body, mind, spirit, charClass,shield);
        }
        return null;
    }
}
