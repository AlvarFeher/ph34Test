package persistence;

import business.entities.Character;

import java.util.List;

/**
 * interface of character json data access object
 */
public interface CharacterJsonDAO {
    /**
     * adds a character in the database
     * @param character character object
     * @return if character is added
     */
    int add(Character character);

    /**
     * deletes a character by its name
     * @param str character name
     */
    void delete(String str);

    /**
     * gets a list of all the characters
     * @return a list of all the characters
     */
    List<Character> getAll();

    /**
     * gets a list of all the character that their player's name is a whole of str
     * @param str a substring of player's name
     * @return a list of all the character that their player's name is a whole of str
     */
    List<Character> getCharactersByPart(String str);

    /**
     * gets a list of all the character names
     * @return a list of all the character names
     */
    List<String> getCharactersNames();

    /**
     * gets a list of characters matching their indexes in the parameter
     * @param parties_inx an array of positions
     * @return a list of characters matching their indexes in the parameter
     */
    List<Character> getCharactersByIndexes(int[] parties_inx);

    /**
     * update the dataset of characters
     * @param new_characterList a list of all the characters
     */
    void update(List<Character> new_characterList);

    /**
     * get the character's spirit by the character's name
     * @param partyName the name of the charater
     * @return the character's spirit
     */
    int getCharactersSpiritByName(String partyName);

}
