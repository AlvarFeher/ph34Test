package persistence.API;

import business.entities.Character;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import persistence.CharacterDAO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CharacterApiDAO implements CharacterDAO {

    private ApiHelper apiHelper;
    private final String url = "https://balandrau.salle.url.edu/dpoo/S1-Project_ICE10/characters";


    public CharacterApiDAO(){
        try {
            apiHelper = new ApiHelper();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int add(Character character) {
        try {
            String body = new Gson().toJson(character);
            apiHelper.postToUrl(url, body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

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
}
