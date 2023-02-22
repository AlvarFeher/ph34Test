package persistence;

import business.entities.Adventure;
import business.entities.Party;

import java.util.List;

/**
 * interface of adventure json data access object
 */
public interface AdventureJsonDAO {
    /**
     * add an adventure to the database
     * @param adventure adventure object
     * @return if the adventure is added or not
     */
    int add(Adventure adventure);

    /**
     * gets all the adventures
     * @return list of adventure objects
     */
    List<Adventure> getAll();

    /**
     * gets an object of adventure which has the name in the parameter
     * @param str name of the adventure
     * @return an object of adventure
     */
    Adventure getAdventureByName(String str);

    /**
     * updates an adventure by another adventure
     * @param adventure adventure object
     */
    void update(Adventure adventure);

    /**
     * gets the name of the adventure if exists
     * @param inx position of the adventure
     * @return the name of the adventure if exists
     */
    String getNameByIndex(int inx);

    /**
     * gets the number of encounters of the adventure if exists
     * @param currentAdventure name of the adventure
     * @return the number of encounters of the adventure if exists
     */
    int getNumOfEncountersByName(String currentAdventure);

    /**
     * gets all the names of monsters in an encounter of the adventure if exists
     * @param i position of the encounter
     * @param currentAdventure name of the adventure
     * @return all the names of monsters in an encounter of the adventure if exists
     */
    List<String> getMonstersInEncounter(int i, String currentAdventure);

    /**
     * gets all the parties of the adventure if exists
     * @param currentAdventure name of the adventure
     * @return list of parties objects of the adventure if exists
     */
    List<Party> getPartyByName(String currentAdventure);

    /**
     * looks if a monster is in an encounter of an adventure and is a boss or not
     * @param currentAdventure name of the adventure
     * @param encounter_pos the encounter position
     * @param name the name of the monster
     * @return if a monster is in an encounter of an adventure and is a boss or not
     */
    boolean isNameMonster(String currentAdventure, int encounter_pos, String name);

    /**
     * get the damage dice of a monster in an encounter of an adventure
     * @param currentAdventure name of the adventure
     * @param encounter_pos the encounter position
     * @param name the name of the monster
     * @return the damage dice of a monster in an encounter of an adventure
     */
    int getDamageDiceByName(String currentAdventure, int encounter_pos, String name);

    /**
     * get the character class in an adventure
     * @param currentAdventure name of the adventure
     * @param name the name of the character
     * @return the character class in an adventure
     */
    String getCharacterClassByName(String currentAdventure, String name);

    /**
     * get the character body in an adventure
     * @param currentAdventure name of the adventure
     * @param name name of the character
     * @return the character body in an adventure
     */
    int getCharactersBodyByName(String currentAdventure, String name);


    /**
     * checks if all monsters in an encounter of an adventure are dead
     * @param currentAdventure name of the adventure
     * @param encounter_pos the encounter position
     * @return true if all monsters in an encounter of an adventure are dead
     */
    boolean areMonstersAllDead(String currentAdventure, int encounter_pos);

    /**
     * checks if all characters in an adventure are unconscious
     * @param currentAdventure name of the adventure
     * @return true if all characters in an adventure are unconscious
     */
    boolean arePartyAllUnconscious(String currentAdventure);

    /**
     * get the xp gained in an encounter of an adventure
     * @param adventure_name name of the adventure
     * @param encounter_pos the encounter position
     * @return the xp gained in an encounter of an adventure
     */
    int getXpGainedInEncounter(String adventure_name, int encounter_pos);

    /**
     * gets the character mind in an adventure
     * @param adventure_name name of the adventure
     * @param partyName name of the character
     * @return the character mind in an adventure
     */
    int getCharactersMindByName(String adventure_name, String partyName);

    /**
     * gets the number of adventures
     * @return the number of adventures
     */
    int getAdventuresSize();

    /**
     * checks if a specific monster (by position) in an encounter of an adventure is dead
     * @param currentAdventure name of the adventure
     * @param encounterPos the encounter position
     * @param monster_pos the monster position
     * @return true if the monster is dead
     */
    boolean isMonsterDeadByPosition(String currentAdventure, int encounterPos, int monster_pos);

    /**
     * checks if a specific character ( by name ) in an adventure is unconscious
     * @param currentAdventure name of the adventure
     * @param s the party name
     * @return true if the character is unconscious
     */
    boolean isPartyUnconsciousByName(String currentAdventure, String s);

    /**
     * checks if a specific monster ( by name ) in an encounter of an adventure is alive
     * @param currentAdventure name of the adventure
     * @param encounter_pos the encounter position
     * @param s the monster name
     * @return true if monster is alive
     */
    boolean isMonsterAlive(String currentAdventure, int encounter_pos, String s);

    /**
     * checks if a specific character (by position )in an adventure is unconscious
     * @param currentAdventure name of the adventure
     * @param party_pos the party position
     * @return true if the character is unconscious
     */
    boolean isPartyUnconsciousByPosition(String currentAdventure, int party_pos);

    int getCharactersSpiritByName(String currentAdventure, String name);

    int getCharactersXpByName(String currentAdventure, String name);
}
