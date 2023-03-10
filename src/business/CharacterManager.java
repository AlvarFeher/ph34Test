package business;

import business.entities.Character;
import business.entities.Classes.*;
import business.entities.Party;
import persistence.API.CharacterApiDAO;
import persistence.CharacterDAO;

import persistence.JSON.CharacterJsonDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * manages character related features logic
 * @author Youssef Bat, Alvaro Feher
 */
public class CharacterManager {

    private final CharacterDAO characterJsonDAO;
    private final CharacterDAO characterApiDAO;
    private boolean local;

    /**
     * checks if the storage method is local or in cloud
     * @return boolean depending on the storage method, local or in cloud
     */
    public boolean isLocal() {
        return local;
    }

    /**
     * sets storage method to either local or in cloud
     * @param local boolean depending on the storage method, local or in cloud
     */
    public void setLocal(boolean local) {
        this.local = local;
    }

    /**
     * constructor
     */
    public CharacterManager() {
        characterJsonDAO = new CharacterJsonDAO();
        characterApiDAO = new CharacterApiDAO();
    }

    /**
     * roll a d6
     * @return a random number between 1 and 6
     */
    public int rollDice(){
        return (int) (Math.random() * 6) +1;
    }

    /**
     * generate state based on a dice roll
     * @param dice_roll random number
     * @return -1 if dice roll = 0/1/2 , 0 if dice roll = 3/4/5, 1 if dice roll = 6/7/8/9, 2 if dice roll = 10/11, 3 if dice roll = 12
     */
    public int generateStats(int dice_roll) {
        int[] arr = {-1, -1, -1, 0, 0, 0, 1, 1, 1, 1, 2, 2, 3};
        return arr[dice_roll];
    }

    /**
     * converts xp to level
     * @param xp experience
     * @return xp converted to level
     */
    public int xpToLevel(int xp) {
        int[] level = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        return level[xp/100];
    }

    /**
     * converts a level to xp
     * @param level level
     * @return level converted to xp
     */
    public int levelToXp(int level) {
        int[] xp = {0, 0, 100, 200, 300, 400, 500, 600, 700, 800, 900};
        return xp[level];
    }

    /**
     * gets a characters actual class depending on its current level and initial class
     * @param level current level of a character
     * @param initialClass initial class of a character
     * @return real character class
     */
    public String levelToClass(int level, String initialClass) {
        String finalClass = "";
        if(Objects.equals(initialClass, "Adventurer") || Objects.equals(initialClass, "Warrior") || Objects.equals(initialClass, "Champion") ){
            if (level < 4 && level > 0) {
                finalClass = "Adventurer";
            } else if (level < 8 && level > 3) {
                finalClass = "Warrior";
            } else if (level <= 10 && level > 7) {
                finalClass = "Champion";
            }
        } else if (Objects.equals(initialClass, "Cleric") || Objects.equals(initialClass, "Paladin")) {
            if (level > 0 && level < 5) {
                finalClass = "Cleric";
            } else {
                finalClass = "Paladin";
            }
        } else if (Objects.equals(initialClass, "Wizard")) {
            finalClass = "Wizard";
        }
        return finalClass;
    }

    /**
     * adjust name to have first letter as capital case and the rest of letters into small case
     * @param name character name
     * @return name adjusted
     */
    public String adjustCharacterName(String name) {
        char first = java.lang.Character.toUpperCase(name.charAt(0));
        StringBuilder new_name = new StringBuilder();
        new_name.append(first);
        for (int i=1;i<name.length();i++) {
            new_name.append(java.lang.Character.toLowerCase(name.charAt(i)));
        }
        return new_name.toString();
    }

    /**
     * adds a character to the data set taking into account its class and statistics.
     * @param name character's name
     * @param player_name player name
     * @param level character's level
     * @param body character's body statistics
     * @param mind character's mind statistics
     * @param spirit character's spirit statistics
     * @param initialClass character's initial class
     * @return if the character is created
     */
    public int createCharacter(String name, String player_name, int level, int body, int mind, int spirit, String initialClass) {
        Character character;
        switch (initialClass) {
            case "Adventurer" -> {
                character = new Adventurer(adjustCharacterName(name), player_name, levelToXp(level), body, mind, spirit, levelToClass(level, initialClass));
                if (isLocal()) {
                    return characterJsonDAO.add(character);
                } else {
                    return characterApiDAO.add(character);
                }
            }
            case "Warrior" -> {
                character = new Warrior(adjustCharacterName(name), player_name, levelToXp(level), body, mind, spirit, levelToClass(level, initialClass));
                if (isLocal()) {
                    return characterJsonDAO.add(character);
                } else {
                    return characterApiDAO.add(character);
                }
            }
            case "Champion" -> {
                character = new Champion(adjustCharacterName(name), player_name, levelToXp(level), body, mind, spirit, levelToClass(level, initialClass));
                if (isLocal()) {
                    return characterJsonDAO.add(character);
                } else {
                    return characterApiDAO.add(character);
                }
            }
            case "Cleric" -> {
                character = new Cleric(adjustCharacterName(name), player_name, levelToXp(level), body, mind, spirit, levelToClass(level, initialClass));
                if (isLocal()) {
                    return characterJsonDAO.add(character);
                } else {
                    return characterApiDAO.add(character);
                }
            }
            case "Paladin" -> {
                character = new Paladin(adjustCharacterName(name), player_name, levelToXp(level), body, mind, spirit, levelToClass(level, initialClass));
                if (isLocal()) {
                    return characterJsonDAO.add(character);
                } else {
                    return characterApiDAO.add(character);
                }
            }
            case "Wizard" -> {
                character = new Wizard(adjustCharacterName(name), player_name, levelToXp(level), body, mind, spirit, levelToClass(level, initialClass), 0);
                if (isLocal()) {
                    return characterJsonDAO.add(character);
                } else {
                    return characterApiDAO.add(character);
                }
            }
        }
        return 1;
    }

    /**
     * checks if a name matches the right considerations.
     * @param name name
     * @return true if name is valid, false otherwise
     */
    public boolean isNameValid(String name) {
        String special_char = "[!@#$%&*()_+=|<>?{}\\[\\]~-];";
        for (int i=0;i<name.length();i++) {
            if (java.lang.Character.isDigit(name.charAt(i))) {
                return true;
            }
            for (int j=0;j<special_char.length();j++) {
                if (name.charAt(i) == special_char.charAt(j)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * gets a list of all the characters that their names contains name
     * @param name a part of character's name
     * @return a list of all the characters that their names contains name
     */
    public List<String> listCharacters(String name) {
        List<String> names = new ArrayList<>();
        List<Character> list;
        if (isLocal()) {
            list = characterJsonDAO.getCharactersByPart(name);
        }
        else {
            list = characterApiDAO.getCharactersByPart(name);
        }
        for (Character character : list) {
            names.add(character.getName());
        }
        return names;
    }

    /**
     * get an object of character by a set of indormation
     * @param name character name
     * @param index index of character
     * @return the character
     */
    public Character getCharacterByindex(String name, int index) {
        if (isLocal()) {
            return characterJsonDAO.getCharactersByPart(name).get(index - 1);
        }
        else {
            return characterApiDAO.getCharactersByPart(name).get(index - 1);
        }
    }

    /**
     * deletes a character
     * @param character_to_delete name of character
     */
    public void deleteCharacter(String character_to_delete) {
        if (isLocal()) {
            characterJsonDAO.delete(character_to_delete);
        }
        else {
            characterApiDAO.delete(character_to_delete);
        }
    }

    /**
     * gets the number of characters in the data set depending on its locality
     * @return the number of characters in the data set
     */
    public int getCharacterCount(){
        if (isLocal()) {
            return characterJsonDAO.getAll().size();
        }
        else {
            return characterApiDAO.getAll().size();
        }
    }

    /**
     * get all the characters from the Json storage
     * @return all the characters
     */
    public List<Character> getAll(){
        if (isLocal()) {
            return characterJsonDAO.getAll();
        }
        return characterApiDAO.getAll();
    }

    /**
     * a list of all the characters names
     * @return all the characters names
     */
    public List<String> getAllCharacterNames() {
        if (isLocal()) {
            return characterJsonDAO.getCharactersNames();
        }
        return characterApiDAO.getCharactersNames();
    }

    /**
     * gets all the names of characters that corresponds to the character's index in the parties_inx array
     * @param parties_inx an array that contains in each position the position of a character
     * @return a string array of all the names of characters that correspondcs to the character's index in the array of the parameter
     */
    public String[] getPartyNames(int[] parties_inx) {
        List<String> characterNames = getAllCharacterNames();
        String[] names = new String[parties_inx.length];
        for (int i=0;i<parties_inx.length;i++) {
            if (parties_inx[i] < 0) {
                names[i] = "Empty";
            }
            else {
                names[i] = characterNames.get(parties_inx[i]);
            }
        }
        return names;
    }

    /**
     * get initial values of all character by their names
     * @param partyNames the character names
     * @return a list of all the initial values
     */
    public List<Integer> getInitValueByNames(String[] partyNames) {
        List<Integer> init_values = new ArrayList<>();
        for (String partyName : partyNames) {
            if (isLocal()) {
                init_values.add((characterJsonDAO.getCharactersSpiritByName(partyName)) + (int) (Math.random() * 12) + 1);
            }
            else {
                init_values.add((characterApiDAO.getCharactersSpiritByName(partyName)) + (int) (Math.random() * 12) + 1);
            }
        }
        return init_values;
    }

    /**
     * gets all the characters that correspond to their respective indexes in the data set
     * @param parties_inx an array containing all the positions of characters in the data set
     * @return a list of characters
     */
    public List<Character> getCharactersByIndexes(int[] parties_inx) {
        if (isLocal()) {
            return characterJsonDAO.getCharactersByIndexes(parties_inx);
        }
        return characterApiDAO.getCharactersByIndexes(parties_inx);
    }

    public String getInitialClass(String currentClass){
        if(currentClass.equals("adventurer") || currentClass.equals("warrior") || currentClass.equals("champion")){
            return "adventurer"; 
        } else if (currentClass.equals("cleric") || currentClass.equals("paladin")) {
            return "cleric";
        }else{
            return "wizard";
        }
    }

    /**
     * get the hit points of each of the characters in the party
     * @param parties_inx an array containing all the positions of characters in the data set
     * @return a list of hit points
     */
    public List<Integer> getMaxHitPointsByindex(int[] parties_inx) {
        List<Integer> hp = new ArrayList<>();
        for (int partiesInx : parties_inx) {
            Character character = getAll().get(partiesInx);
            if(Objects.equals(character.getCharClass(), "Warrior")){
                hp.add(((10 + character.getBody()) * xpToLevel(character.getXp()))+character.getBody()*xpToLevel(character.getXp()));
            }else
                hp.add((10 + character.getBody()) * xpToLevel(character.getXp()));
        }
        return hp;
    }





}
