package business;

import business.entities.*;
import business.entities.Character;
import business.entities.Classes.*;
import persistence.API.AdventureApiDAO;
import persistence.API.CharacterApiDAO;
import persistence.API.MonsterApiDAO;
import persistence.AdventureDAO;
import persistence.AdventureDAO;
import persistence.CharacterDAO;
import persistence.JSON.AdventureJsonDAO;
import persistence.JSON.CharacterJsonDAO;
import persistence.JSON.MonstersJsonDAO;
import persistence.MonsterDAO;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Objects;

/**
 * manages adventure related features logic
 * @author Youssef Bat, Alvaro Feher
 */
public class AdventureManager {

    private final CharacterManager characterManager;
    private final AdventureDAO adventureJsonDAO;
    private final AdventureDAO adventureApiDAO;
    private final CharacterDAO characterJsonDao;
    private final CharacterDAO characterApiDAO;
    private final MonsterDAO monstersJsonDAO;
    private final MonsterDAO monstersApiDAO;
    private boolean local;

    /**
     * check if data storage is local or in cloud
     * @return boolean weather storage is local or in cloud
     */
    public boolean isLocal() {
        return local;
    }

    /**
     * Sets storage as either local or in cloud
     * @param local boolean defining storage as local or in cloud
     */
    public void setLocal(boolean local) {
        this.local = local;
    }



    /**
     * constructor
     */
    public AdventureManager() {
        adventureJsonDAO = new AdventureJsonDAO();
        characterManager = new CharacterManager();
        characterJsonDao = new CharacterJsonDAO();
        monstersJsonDAO = new MonstersJsonDAO();
        adventureApiDAO = new AdventureApiDAO();
        monstersApiDAO = new MonsterApiDAO();
        characterApiDAO = new CharacterApiDAO();
    }

    /**
     * checks if the name of the adventure exists already
     * @param adventure_name the name of the adventure
     * @return true if the name is unique, false otherwise
     */
    public boolean isAdventureNameUnique(String adventure_name) {
        if (isLocal()) {
            for (Adventure a : adventureJsonDAO.getAll()) {
                if (Objects.equals(a.getName(), adventure_name)) {
                    return false;
                }
            }
        }
        else {
            for (Adventure a : adventureApiDAO.getAll()) {
                if (Objects.equals(a.getName(), adventure_name)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * gets all monsters filtered in a specific encounter of a specific adventure
     * @param i the encounter index of an adventure
     * @param adventure_name the name of the adventure
     * @return all monsters in a specific encounter of a specific adventure
     */
    public List<String> getMonsterNamesInEncounter(int i, String adventure_name) {
        Adventure adventure;
        if (isLocal()) {
             adventure = adventureJsonDAO.getAdventureByName(adventure_name);
        }
        else {
            adventure = adventureApiDAO.getAdventureByName(adventure_name);
        }
        if (adventure == null) {
            return new ArrayList<>(0);
        }
        if (adventure.getEncounters().get(i).size() == 0) {
            return new ArrayList<>();
        }
        List<String> list = new ArrayList<>(adventure.getEncounters().get(i).size());
        List<String> filtered = new ArrayList<>(0);
        //get every monster even if repeated
        for(int j=0;j<adventure.getEncounters().get(i).size();j++) {
            list.add(adventure.getEncounters().get(i).get(j).getName());
        }
        //filter list by name
        filtered.add(list.get(0));
        for(int j=1;j<list.size();j++) {
            if (!filtered.contains(list.get(j))) {
                filtered.add(list.get(j));
            }
        }
        return filtered;
    }

    /**
     * gets how many monsters of name given in the parameter are there in the encounter
     * @param i the index of the encounter
     * @param adventure_name the name of the adventure
     * @param monster the name of the monster
     * @return how many monsters of name given in the parameter are there in the encounter
     */
    public int occurrenceMonsterInEncounter(int i, String adventure_name, String monster) {
        Adventure adventure;
        if (isLocal()) {
            adventure = adventureJsonDAO.getAdventureByName(adventure_name);
        }
        else{
            adventure = adventureApiDAO.getAdventureByName(adventure_name);
        }

        if (adventure == null) {
            return 0;
        }
        int count = 0;
        for(int j=0;j<adventure.getEncounters().get(i).size();j++) {
            if (Objects.equals(adventure.getEncounters().get(i).get(j).getName(), monster)) {
                count++;
            }
        }
        return count;
    }

    /**
     * gets a Party member by giving its name and the current adventure being played. It works for either local or cloud storage
     * @param currentAdventure adventure being played
     * @param partyName name of the party member to search for
     * @return if the party member is found it returns the Party object of it. If not returns null.
     */
    public Party getPartyMemberByName(String currentAdventure, String partyName){
        List<Party> parties;
        if (isLocal()) {
            parties = adventureJsonDAO.getPartyByName(currentAdventure);
        }
        else {
            parties = adventureApiDAO.getPartyByName(currentAdventure);
        }
        for(Party p: parties){
            if(Objects.equals(p.getCharacter(characterJsonDao).getName(), partyName)){
                return p;
            }
        }
        return null;
    }

    /**
     * when adding monsters is that the system won’t allow for the addition of
     * more than one monster with the Boss difficulty rating
     * @param currentMonster the name of the current monster
     * @param monsterAmount the amount of monsters to be added
     * @param currentEncounter the current encounter we are at
     * @return false if the monster to be added is a boss difficulty, and we already have in the system (amount of 1). true otherwise
     */
    public boolean canMonsterBeAdded(String currentMonster, int monsterAmount, int currentEncounter) {
        List<Adventure> adventures ;
        if (isLocal()) {
            adventures = adventureJsonDAO.getAll();
        }
        else {
            adventures = adventureApiDAO.getAll();
        }
        for (Adventure adventure:adventures) {
            if (Objects.equals(adventure.getName(), currentMonster)) {
                for (int i=0;i < adventure.getEncounters().get(currentEncounter-1).size();i++) {
                    if (Objects.equals(adventure.getEncounters().get(currentEncounter - 1).get(i).getChallenge(), "Boss")) {
                        return false;
                    }
                }
            }
        }
        return monsterAmount <= 1;
    }


    /**
     * updating (by adding) the monsters in an encounter of an adventure
     * @param adventure_name the name of the adventure we are working with
     * @param i the encounter position
     * @param monstersToAdd monsters to be added in the encounter of the adventure
     */
    public void updateAdventure(String adventure_name, int i, List<Monster> monstersToAdd) {
        Adventure adventure;
        if (isLocal()) {
            adventure = adventureJsonDAO.getAdventureByName(adventure_name);
        }
        else{
            adventure = adventureApiDAO.getAdventureByName(adventure_name);
        }
        List<Monster> monsters = adventure.getEncounters().get(i);
        monsters.addAll(monstersToAdd);
        List<List<Monster>> new_monsters = new ArrayList<>(adventure.getNum_encounters());
        for (int k=0;k< adventure.getNum_encounters();k++) {
            new_monsters.add(new ArrayList<>());
        }
        for (int j=0;j< adventure.getNum_encounters();j++) {
            if (j==i) {
                new_monsters.get(j).addAll(monsters);
            }
            else {
                new_monsters.get(j).addAll(adventure.getEncounters().get(j));
            }
        }
        Adventure new_adventure = new Adventure(adventure.getName(), adventure.getNum_encounters(), new_monsters);
        if (isLocal()) {
            adventureJsonDAO.update(new_adventure);
        }
        else {
            adventureApiDAO.update(new_adventure);
        }
    }


    /**
     * initializing the creation of an adventure with its name and the number of encounters in the adventure
     * @param adventure_name the name of the adventure
     * @param encountering_num the number of encounters
     */
    public void createAdventure(String adventure_name, int encountering_num) {
        List<List<Monster>> list = new ArrayList<>(encountering_num);
        for (int i=0;i<encountering_num;i++) {
            list.add(new ArrayList<>());
        }
        Adventure adventure = new Adventure(adventure_name, encountering_num, list);
        if (isLocal()) {
            adventureJsonDAO.add(adventure);
        } else {
            adventureApiDAO.add(adventure);
        }
    }

    /**
     * delete all monsters with the monster name that are in the encounter index of the adventure that has the adventure name
     * @param adventure_name the name of the adventure we are working with
     * @param encounterIndex the encounter position
     * @param monster_name the monster to be deleted
     */
    public void deleteMonster(String adventure_name, int encounterIndex, String monster_name){
        Adventure adventure;
        if (isLocal()) {
            adventure = adventureJsonDAO.getAdventureByName(adventure_name);
        }
        else {
            adventure = adventureApiDAO.getAdventureByName(adventure_name);
        }
        List<List<Monster>> new_monsters = new ArrayList<>(adventure.getNum_encounters());
        List<Monster> monsters = adventure.getEncounters().get(encounterIndex);
        List<Monster> monsters_updated =new ArrayList<>();
        for (Monster monster : monsters) {
            if (!Objects.equals(monster.getName(), monster_name)) {
                monsters_updated.add(monster);
            }
        }
        for (int k=0;k< adventure.getNum_encounters();k++) {
            new_monsters.add(new ArrayList<>());
        }
        for (int j=0;j< adventure.getNum_encounters();j++) {
            if (j==encounterIndex) {
                new_monsters.get(j).addAll(monsters_updated);
            }
            else {
                new_monsters.get(j).addAll(adventure.getEncounters().get(j));
            }
        }
        Adventure new_adventure = new Adventure(adventure.getName(), adventure.getNum_encounters(), new_monsters);
        if (isLocal()) {
            adventureJsonDAO.update(new_adventure);
        }else {
            adventureApiDAO.update(new_adventure);
        }
    }


    /**
     * check if chosenCharacterIndex exist already in parties
     * @param parties the array thAT contains the position of the characters at a party
     * @param index_at the length of the array parties where it is not null
     * @param chosenCharacterIndex the number ot be checked along with the content of parties
     * @return true if does not exist, false otherwise
     */
    public boolean checkCharacterRepetition(int[] parties, int index_at, int chosenCharacterIndex) {
        for (int i=0;i<index_at;i++) {
            if (parties[i] == chosenCharacterIndex) {
                return false;
            }
        }
        return true;
    }


    /**
     * returns the number of encounters in a specific adventure
     * @param currentAdventure the name of the adventure
     * @return how many encounters there is in this adventure
     */
    public int getNumOfEncountersByName(String currentAdventure) {
        if (isLocal()) {
            return adventureJsonDAO.getNumOfEncountersByName(currentAdventure);
        }
        return adventureApiDAO.getNumOfEncountersByName(currentAdventure);
    }

    /**
     * gets all monsters unfiltered in a specific encounter of a specific adventure
     * @param i the encounter index of an adventure
     * @param currentAdventure the name of the adventure
     * @return all monsters in a specific encounter of a specific adventure
     */
    public List<String> getMonsterNamesInEncounterUnfiltered(int i, String currentAdventure) {
        if (isLocal()) {
            return adventureJsonDAO.getMonstersInEncounter(i, currentAdventure);
        }
        return adventureApiDAO.getMonstersInEncounter(i, currentAdventure);
    }

    /**
     * updates the adventure by adding
     * @param currentAdventure the name of the adventure
     * @param parties_inx an array that contains in each position the position of a character
     */
    public void updateParty(String currentAdventure, int[] parties_inx) {
        Adventure adventure;
        characterManager.setLocal(isLocal());
        if (isLocal()) {
            adventure = adventureJsonDAO.getAdventureByName(currentAdventure);
        }
        else {
            adventure = adventureApiDAO.getAdventureByName(currentAdventure);
        }
        List<Character> characterList = characterManager.getCharactersByIndexes(parties_inx);

        List<Party> parties = new ArrayList<>();
        List<Integer> hit_points = characterManager.getMaxHitPointsByindex(parties_inx);

        for (int i=0;i< parties_inx.length;i++) {
            Character c = characterList.get(i);
            parties.add(new Party(characterJsonDao.assignClass(c.getName(),c.getPlayer(),c.getXp(),c.getBody(),c.getMind(),c.getSpirit(),c.getCharClass(),0), hit_points.get(i),characterJsonDao,0));
        }
        Adventure new_adventure = new Adventure(adventure.getName(), adventure.getNum_encounters(), adventure.getEncounters(), parties);
        if (isLocal()) {
            adventureJsonDAO.update(new_adventure);
        }
        else {
            adventureApiDAO.update(new_adventure);
        }
    }

    /**
     * updates the adventure's party during preparation stage. All characters perform their actions depending on their classes and update the party.
     * @param currentAdventure name of the adventure
     * @param parties_inx array of character's positions
     */
    public void updatePartyInPrepStage(String currentAdventure, int[] parties_inx) {
        Adventure adventure;
        if (isLocal()) {
            adventure = adventureJsonDAO.getAdventureByName(currentAdventure);
        }
        else {
            adventure = adventureApiDAO.getAdventureByName(currentAdventure);
        }
        List<Party> parties = new ArrayList<>();
        for (int i=0;i< parties_inx.length;i++) {
            Character character = adventure.getParties().get(i).getCharacter(characterJsonDao);
            parties = character.preparationStageAction(adventure.getParties(),character.getName(),characterJsonDao);
            Adventure new_adventure = new Adventure(adventure.getName(), adventure.getNum_encounters(), adventure.getEncounters(), parties);
            if (isLocal()) {
                adventureJsonDAO.update(new_adventure);
            }
            else {
                adventureApiDAO.update(new_adventure);
            }
        }
    }


    /**
     * Updates the party during the short rest stage of an adventure. All characters perform their actions depending on their classes.
     * @param currentAdventure name of current adventure being played
     * @param parties_inx array of character's positions
     */
    public void updatePartyInShortRestStage(String currentAdventure, int[] parties_inx){
        Adventure adventure;
        if (isLocal()) {
            adventure = adventureJsonDAO.getAdventureByName(currentAdventure);
        }
        else {
            adventure = adventureApiDAO.getAdventureByName(currentAdventure);
        }

        List<Party> parties = new ArrayList<>();

        for (int i = 0; i < parties_inx.length; i++) {
            Character character = adventure.getParties().get(i).getCharacter(characterJsonDao);
            parties = character.shortRestAction(adventure.getParties(),character.getName(),characterJsonDao);
            Adventure new_adventure = new Adventure(adventure.getName(), adventure.getNum_encounters(), adventure.getEncounters(), parties);
            if (isLocal()) {
                adventureJsonDAO.update(new_adventure);
            }
            else {
                adventureApiDAO.update(new_adventure);
            }
        }
    }


    /**
     * checks if a combatant is a monster
     * @param currentAdventure name of adventure
     * @param encounter_pos encounter position
     * @param name name of combatant
     * @return true if combatant is a monster
     */
    public boolean isCombatantMonster(String currentAdventure, int encounter_pos, String name) {
        if (isLocal()) {
            return adventureJsonDAO.isNameMonster(currentAdventure, encounter_pos, name);
        }
        return adventureApiDAO.isNameMonster(currentAdventure, encounter_pos, name);
    }

    /**
     * Characters perform their attack actions depending on their classes.
     * @param currentAdventure name of the adventure
     * @param name name of the character
     * @param needHealing equals 1 if healing is needed, 0 if not
     * @param currentAliveMonsters amount of monsters alive in adventure at that moment
     * @return the attack action value of the character
     */

    public int takeAttackActionCharacter(String currentAdventure, String name, int currentAliveMonsters, int needHealing) {
        int damage = 0;

        List<Party> parties;
        if (isLocal()) {
            parties = adventureJsonDAO.getPartyByName(currentAdventure);
        }
        else {
            parties = adventureApiDAO.getPartyByName(currentAdventure);
        }
        Character character;
        for(Party p: parties){
            if(Objects.equals(p.getCharacter(characterJsonDao).getName(), name)){
                Character c = p.getCharacter(characterJsonDao);
                character = characterJsonDao.assignClass(c.getName(),c.getPlayer(),c.getXp(),c.getBody(),c.getMind(),c.getSpirit(),c.getCharClass(),0);

                damage = character.doAction() + character.doAction(needHealing,currentAliveMonsters);
            }
        }
        return damage;
    }


    /**
     * check if any member from the party has its hp lower than the initial half
     * @param currentAdventure current adventure
     * @return boolean showing if anyone needs healing
     */

    public int checkPartyHalfHp(String currentAdventure, List<Integer> maxHitPoints){
        int i=0;
        if (isLocal()) {
            for (Party c : adventureJsonDAO.getPartyByName(currentAdventure)) {
                if (c.getHitPoint() < maxHitPoints.get(i) / 2) {
                    return 1;
                }
                i++;
            }
        }
        else {
            for (Party c : adventureApiDAO.getPartyByName(currentAdventure)) {
                if (c.getHitPoint() < maxHitPoints.get(i) / 2) {
                    return 1;
                }
                i++;
            }
        }
        return 0;
    }

    /**
     * check if anyone in the party needs healing. Checks if anyone has less hit points than its initial maximum
     * @return boolean shpwing if healing is needed
     */

    public boolean checkHealingNeeded(String currentAdventure, List<Integer> maxHitPoints){
        int i=0;
        if (isLocal()) {
            for (Party c : adventureJsonDAO.getPartyByName(currentAdventure)) {
                if (c.getHitPoint() < maxHitPoints.get(i)) {
                    return true;
                }
                i++;
            }
        }
        else {
            for (Party c : adventureApiDAO.getPartyByName(currentAdventure)) {
                if (c.getHitPoint() < maxHitPoints.get(i)) {
                    return true;
                }
                i++;
            }
        }
        return false;
    }

    /**
     * gets the attack action value of a monster depending on its stats and dice.
     * @param currentAdventure name of the adventure
     * @param encounter_pos encounter position
     * @param name name of monster
     * @return the attack action value of the monster
     */
    public int  takeAttackActionMonster(String currentAdventure, int encounter_pos, String name) {
        int max;
        if (isLocal()) {
            max = adventureJsonDAO.getDamageDiceByName(currentAdventure, encounter_pos, name);
        }
        else {
            max = adventureApiDAO.getDamageDiceByName(currentAdventure, encounter_pos, name);
        }
        return (int)Math.floor(Math.random() * (max) + 1);
    }

    /**
     * remove all the parties in an adventure
     * @param currentAdventure name of the adventure
     */
    public void resetParty(String currentAdventure) {
        Adventure adventure = adventureJsonDAO.getAdventureByName(currentAdventure);
        Adventure new_adventure = new Adventure(adventure.getName(), adventure.getNum_encounters(), adventure.getEncounters(), null);
        adventureJsonDAO.update(new_adventure);
    }

    /**
     * check if all the monsters in an encounter are dead or if all the party is unconscious
     * @param currentAdventure name of the adventure
     * @param encounter_pos encounter position
     * @return true if all the monsters in an encounter are dead or if all the party is unconscious, false otherwise
     */
    public boolean isCombatEnd(String currentAdventure, int encounter_pos) {
        if (isLocal()) {
            return adventureJsonDAO.areMonstersAllDead(currentAdventure, encounter_pos) || adventureJsonDAO.arePartyAllUnconscious(currentAdventure);
        }
        return adventureApiDAO.areMonstersAllDead(currentAdventure, encounter_pos) || adventureApiDAO.arePartyAllUnconscious(currentAdventure);
    }

    /**
     * checks if the adventure reached a TPU case, which means all characters in the party are unconscious
     * @param currentAdventure name of the adventure
     * @return true if the adventure reached a TPU case
     */
    public boolean isTPU(String currentAdventure) {
        if (isLocal()) {
            return adventureJsonDAO.arePartyAllUnconscious(currentAdventure);
        }else {
            return adventureApiDAO.arePartyAllUnconscious(currentAdventure);
        }
    }


    /**
     * Applies the damage produced by a Boss monster to all members of a party. Including passive abilities and damage reductions.
     * @param damage damage made by monster
     * @param currentAdventure current adventure being played
     * @param damageType type of damage from monster
     * @return returns string stating the consciousness state of the party members
     */
    // attack from Boss Monster
    public String applyDamageOnAllParty(int damage, String currentAdventure, String damageType){
        if (isLocal()) {
            if (adventureJsonDAO.arePartyAllUnconscious(currentAdventure)) {
                return null;
            }
        }
        else {
            if (adventureApiDAO.arePartyAllUnconscious(currentAdventure)) {
                return null;
            }
        }
        Adventure adventure;
        if (isLocal()) {
            adventure = adventureJsonDAO.getAdventureByName(currentAdventure);
        }
        else {
            adventure = adventureApiDAO.getAdventureByName(currentAdventure);
        }
        Adventure newAdventure;
        List<Party> characters;
        if (isLocal()) {
            characters = adventureJsonDAO.getPartyByName(currentAdventure);
        }
        else{
            characters = adventureApiDAO.getPartyByName(currentAdventure);
        }
        List<Party> parties = new ArrayList<>();
        int i=0;
        boolean unconscious = false;
        for(Party p:characters){
            if(characters.get(i).getCharacter(characterJsonDao) instanceof Warrior || characters.get(i).getCharacter(characterJsonDao) instanceof Champion ){
                if(Objects.equals(damageType, "Physical")){
                    if(characters.get(i).getHitPoint() - damage/2 > 0){
                        parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage/2,characterJsonDao,0));
                    }else{
                        parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao,0));
                        unconscious = true;
                    }
                }else{
                    if(characters.get(i).getHitPoint() - damage > 0){
                        parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage,characterJsonDao,0));
                    }else{
                        parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao,0));
                        unconscious = true;
                    }
                }
            }

            if(characters.get(i).getCharacter(characterJsonDao) instanceof Paladin){
                if(Objects.equals(damageType, "Psychical")){
                    if(characters.get(i).getHitPoint() - damage/2 > 0){
                        parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage/2,characterJsonDao,0));
                    }else{
                        parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao,0));
                        unconscious = true;
                    }
                }else{
                    if(characters.get(i).getHitPoint() - damage > 0){
                        parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage,characterJsonDao,0));
                    }else{
                        parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao,0));
                        unconscious = true;
                    }                        }
            }

            if(characters.get(i).getCharacter(characterJsonDao) instanceof Wizard){
                int damageTaken = damage -  characterManager.xpToLevel(characters.get(i).getCharacter(characterJsonDao).getXp());
                int shield = ((Wizard) characters.get(i).getCharacter(characterJsonDao)).getShield();
                if(Objects.equals(damageType, "Magical")){
                    if(shield > 0){
                        parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), shield - damageTaken,characterJsonDao,0));
                    }else{
                        if(characters.get(i).getHitPoint() - damageTaken > 0){
                            parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damageTaken,characterJsonDao,0));
                        }else{
                            parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao,0));
                            unconscious = true;
                        }
                    }
                }else{
                    if(shield > 0){
                        parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), shield - damage,characterJsonDao,0));
                    }else{
                        if(characters.get(i).getHitPoint() - damage > 0){
                            parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage,characterJsonDao,0));
                        }else{
                            parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao,0));
                            unconscious = true;
                        }
                    }
                }
            }
            if(characters.get(i).getCharacter(characterJsonDao) instanceof Adventurer || characters.get(i).getCharacter(characterJsonDao) instanceof Cleric){
                if(characters.get(i).getHitPoint() - damage > 0){
                    parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage,characterJsonDao,0));
                }else{
                    parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao,0));
                    unconscious = true;
                }
            }

            i++;
        }

        newAdventure =  new Adventure(adventure.getName(), adventure.getNum_encounters(), adventure.getEncounters(), parties);
        if (isLocal()) {
            adventureJsonDAO.update(newAdventure);
        }
        else{
            adventureApiDAO.update(newAdventure);
        }
        for(Party p : parties){
            if(p.getHitPoint() > 0){ // character is conscious
                return p.getCharacter(characterJsonDao).getName();
            }else{
                return p.getCharacter(characterJsonDao).getName() + " falls unconscious";
            }
        }
        return "";
    }

    /**
     * the monster whose turn to attack applies its damage dice on a non-unconscious party.
     * Including passive abilities and damage reductions depending on classes.
     * @param damage damage value
     * @param current_adventure name of the adventure
     * @param parties_inx the parties index
     * @return the name of the character being attacked
     */
    public String applyDamageOnRandomConsciousParty(int damage, String current_adventure, int[] parties_inx, String damageType) {
        if (isLocal()) {
            if (adventureJsonDAO.arePartyAllUnconscious(current_adventure)) {
                return null;
            }
        }
        else {
            if (adventureApiDAO.arePartyAllUnconscious(current_adventure)) {
                return null;
            }
        }
        Adventure adventure;
        if (isLocal()) {
            adventure = adventureJsonDAO.getAdventureByName(current_adventure);
        }
        else {
            adventure = adventureApiDAO.getAdventureByName(current_adventure);
        }
        Adventure new_adventure;
        int party_pos;

        List<Party> characters;
        if (isLocal()) {
            characters = adventureJsonDAO.getPartyByName(current_adventure);
        }
        else {
            characters = adventureApiDAO.getPartyByName(current_adventure);
        }
        List<Party> parties = new ArrayList<>();


        if (isLocal()) {
            do {
                party_pos = (int) Math.floor(Math.random() * (parties_inx.length) + 1) - 1;
            } while (adventureJsonDAO.isPartyUnconsciousByPosition(current_adventure, party_pos));
        }
        else {
            do {
                party_pos = (int) Math.floor(Math.random() * (parties_inx.length) + 1) - 1;
            } while (adventureApiDAO.isPartyUnconsciousByPosition(current_adventure, party_pos));
        }
        boolean unconscious = false;
        for (int i=0;i < parties_inx.length;i++) {
            if (i == party_pos) {
                    if(characters.get(i).getCharacter(characterJsonDao) instanceof Warrior || characters.get(i).getCharacter(characterJsonDao) instanceof Champion ){
                        if(Objects.equals(damageType, "Physical")){
                            if(characters.get(i).getHitPoint() - damage/2 > 0){
                                parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage/2,characterJsonDao,0));
                            }else{
                                parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao,0));
                                unconscious = true;
                            }
                        }else{
                            if(characters.get(i).getHitPoint() - damage > 0){
                                parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage,characterJsonDao,0));
                            }else{
                                parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao,0));
                                unconscious = true;
                            }
                        }
                    }

                    if(characters.get(i).getCharacter(characterJsonDao) instanceof Paladin){
                        if(Objects.equals(damageType, "Psychical")){
                            if(characters.get(i).getHitPoint() - damage/2 > 0){
                                parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage/2,characterJsonDao,0));
                            }else{
                                parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao,0));
                                unconscious = true;
                            }
                        }else{
                            if(characters.get(i).getHitPoint() - damage > 0){
                                parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage,characterJsonDao,0));
                            }else{
                                parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao,0));
                                unconscious = true;
                            }                        }
                    }

                    if(characters.get(i).getCharacter(characterJsonDao) instanceof Wizard){
                        int damageTaken = damage -  characterManager.xpToLevel(characters.get(i).getCharacter(characterJsonDao).getXp());
                        int shield = ((Wizard) characters.get(i).getCharacter(characterJsonDao)).getShield();
                        if(Objects.equals(damageType, "Magical")){
                            if(shield > 0){
                                parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), shield - damageTaken,characterJsonDao,0));
                            }else{
                                if(characters.get(i).getHitPoint() - damageTaken > 0){
                                    parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damageTaken,characterJsonDao,0));
                                }else{
                                    parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao,0));
                                    unconscious = true;
                                }
                            }
                        }else{
                            if(shield > 0){
                                parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), shield - damage,characterJsonDao,0));
                            }else{
                                if(characters.get(i).getHitPoint() - damage > 0){
                                    parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage,characterJsonDao,0));
                                }else{
                                    parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao,0));
                                    unconscious = true;
                                }
                            }
                        }
                    }

                    if(characters.get(i).getCharacter(characterJsonDao) instanceof Adventurer || characters.get(i).getCharacter(characterJsonDao) instanceof Cleric){
                        if(characters.get(i).getHitPoint() - damage > 0){
                            parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage,characterJsonDao,0));
                        }else{
                            parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao,0));
                            unconscious = true;
                        }
                    }
            }
            else {
                parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint(),characterJsonDao,0));
            }
        }
        new_adventure =  new Adventure(adventure.getName(), adventure.getNum_encounters(), adventure.getEncounters(), parties);
        if (isLocal()) {
            adventureJsonDAO.update(new_adventure);
        }
        else{
            adventureApiDAO.update(new_adventure);
        }

        if (unconscious) {
            return characters.get(party_pos).getCharacter(characterJsonDao).getName() + " falls unconscious";
        }
        else {
            return characters.get(party_pos).getCharacter(characterJsonDao).getName();
        }
    }

    /**
     * Checks if a given monster is a Boss or not
     * @param name name of the monster to check
     * @return boolean indicating if monster is boss or not
     */
    public boolean isMonsterBoss(String name){
        List<Monster> monsters;
        if (isLocal()) {
            monsters = monstersJsonDAO.getAll();
        }
        else {
            monsters = monstersApiDAO.getAll();
        }
        for(Monster m : monsters){
            if(Objects.equals(m.getName(), name)){
                return Objects.equals(m.getChallenge(), "Boss");
            }
        }
        return false;
    }

    /**
     * get a list of all hit points of parties
     * @param current_adventure the name of the adventure
     * @return a list of all hit points of parties
     */
    public List<Integer> getHitPointsByindex(String current_adventure) {
        List<Integer> hp = new ArrayList<>();
        List<Party> parties;
        if (isLocal()) {
            parties = adventureJsonDAO.getPartyByName(current_adventure);
        }
        else {
            parties = adventureApiDAO.getPartyByName(current_adventure);
        }
        for (Party party : parties) {
            hp.add(party.getHitPoint());
        }
        return hp;
    }

    /**
     * roll a d10, and see if the hit is fail or a normal git or a critical hit
     * @return 0 fail, 1 normal hit, 2 critical hit
     */
    public int isItAHit() {
        int[] res = {-1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 2};
        return res[(int)Math.floor(Math.random() * (10) + 1)];
    }



    /**
     * The character whose turn to attack applies its damage on a monster. It updates the adventure taking into account the damage reductions of a Boss monster.
     * It is used for all attacks that damage a single monster in an encounter.
     * @param damage damage value
     * @param currentAdventure name of the adventure
     * @param encounterPos encounter position
     * @return the name of the monster being attacked
     */
    public String applyDamageOnRandomMonsterInEncounter(int damage, String currentAdventure, int encounterPos, String attackType) {
        Adventure adventure;
        if (isLocal()) {
            adventure = adventureJsonDAO.getAdventureByName(currentAdventure);
        }
        else {
            adventure = adventureApiDAO.getAdventureByName(currentAdventure);
        }
        List<Monster> monsters = adventure.getEncounters().get(encounterPos);
        List<Monster> new_monsters = new ArrayList<>();
        String s = "";
        int monster_pos = (int)Math.floor(Math.random() * (monsters.size()));

        boolean dead = false;
        for (int i=0;i<monsters.size();i++) {
            String damage_dice = "d" + monsters.get(i).getDamageDice();
            if (i==monster_pos) {
                s = monsters.get(i).getName();

                if(Objects.equals(monsters.get(i).getChallenge(), "Boss")){
                    if (Objects.equals(monsters.get(i).getDamageType(), attackType)) {
                        if (monsters.get(i).getHitPoints() - damage/2 >= 0) {
                            new_monsters.add(new Monster(monsters.get(i).getName(), monsters.get(i).getChallenge(), monsters.get(i).getExperience(), monsters.get(i).getHitPoints() - damage/2, monsters.get(i).getInitiative(), damage_dice, monsters.get(i).getDamageType()));
                        }
                        else {
                            dead = true;
                        }
                    }else{
                        if (monsters.get(i).getHitPoints() - damage > 0) {
                            new_monsters.add(new Monster(monsters.get(i).getName(), monsters.get(i).getChallenge(), monsters.get(i).getExperience(), monsters.get(i).getHitPoints() - damage, monsters.get(i).getInitiative(), damage_dice, monsters.get(i).getDamageType()));
                        }
                        else {
                            dead = true;
                        }
                    }
                }else{
                    if (monsters.get(i).getHitPoints() - damage > 0) {
                        new_monsters.add(new Monster(monsters.get(i).getName(), monsters.get(i).getChallenge(), monsters.get(i).getExperience(), monsters.get(i).getHitPoints() - damage, monsters.get(i).getInitiative(), damage_dice, monsters.get(i).getDamageType()));
                    }
                    else {
                        dead = true;
                    }
                }
            }
            else {
                new_monsters.add(new Monster(monsters.get(i).getName(), monsters.get(i).getChallenge(), monsters.get(i).getExperience(), monsters.get(i).getHitPoints(), monsters.get(i).getInitiative(), damage_dice, monsters.get(i).getDamageType()));
            }
        }

        List<List<Monster>> encounter = new ArrayList<>(adventure.getNum_encounters());

        for (int k=0;k< adventure.getNum_encounters();k++) {
            encounter.add(new ArrayList<>());
        }

        for (int j=0;j< adventure.getNum_encounters();j++) {
            if (j==encounterPos) {
                encounter.get(j).addAll(new_monsters);
            }
            else {
                encounter.get(j).addAll(adventure.getEncounters().get(j));
            }
        }
        Adventure new_adventure = new Adventure(adventure.getName(), adventure.getNum_encounters(), encounter, adventure.getParties());
        if (isLocal()) {
            adventureJsonDAO.update(new_adventure);
        }
        else {
            adventureApiDAO.update(new_adventure);
        }
        if (dead) {
            return s + " dies";
        }
        else {
            return s;
        }
    }

    /**
     * Applies damage by updating adventure. Used for Fireball attack, since it affects the entire encounter.
     * @param damage damage produced by fireball attack to each monster
     * @param currentAdventure adventure being played
     * @param encounterPos encounter being played in adventure
     * @param attackType attack type.  magical, physical or psychical
     */
    public void applyDamageOnAllMonsters(int damage, String currentAdventure, int encounterPos, String attackType){
        Adventure adventure;
        if (isLocal()) {
            adventure = adventureJsonDAO.getAdventureByName(currentAdventure);
        }
        else {
            adventure = adventureApiDAO.getAdventureByName(currentAdventure);
        }
        List<Monster> monsters = adventure.getEncounters().get(encounterPos);
        List<Monster> new_monsters = new ArrayList<>();
        for(Monster m: monsters){
            String damage_dice = "d" + m.getDamageDice();
            if(Objects.equals(m.getChallenge(), "Boss")){
                if(Objects.equals(m.getDamageType(), attackType)){
                    Monster newMonster = new Monster(m.getName(),m.getChallenge(),m.getExperience(),m.getHitPoints()-damage/2,m.getInitiative(),damage_dice,m.getDamageType());
                    new_monsters.add(newMonster);
                }else {
                    Monster newMonster = new Monster(m.getName(), m.getChallenge(), m.getExperience(), m.getHitPoints() - damage, m.getInitiative(), damage_dice, m.getDamageType());
                    new_monsters.add(newMonster);
                }

            }else{
                Monster newMonster = new Monster(m.getName(),m.getChallenge(),m.getExperience(),m.getHitPoints()-damage,m.getInitiative(),damage_dice,m.getDamageType());
                new_monsters.add(newMonster);
            }

        }

        List<List<Monster>> encounters = new ArrayList<>();
        int i =0;
        for(List<Monster> encounter : adventure.getEncounters()){
            if(i != encounterPos)
                encounters.add(encounter);
            else
                encounters.add(new_monsters);
            i++;
        }

        Adventure newAdventure = new Adventure(adventure.getName(),adventure.getNum_encounters(),encounters,adventure.getParties());
        if (isLocal()) {
            adventureJsonDAO.update(newAdventure);
        }
        else {
            adventureApiDAO.update(newAdventure);
        }
    }


    /**
     * applies healing on a single character by updating the adventure. Adds healing amount by another character.
     * @param heal hit points to be regenerated
     * @param currentAdventure current adventure being played
     * @param maxHitPoints list of maximum hit points of all characters in the party
     * @return rteurns the character who received the healing
     */
    public String applyHealOnCharacter( int heal, String currentAdventure, List<Integer> maxHitPoints){
        Adventure adventure;
        if (isLocal()) {
            adventure = adventureJsonDAO.getAdventureByName(currentAdventure);
        }
        else {
            adventure = adventureApiDAO.getAdventureByName(currentAdventure);
        }
        List<Party> parties = adventure.getParties();
        List<Party> new_parties = new ArrayList<>();
        int flag =0;
        String target = "";
        for (int i=0; i < parties.size(); i++){
            if(parties.get(i).getHitPoint() < maxHitPoints.get(i)/2 && flag == 0){
                //update adventure with character healed
                Party newParty = new Party(parties.get(i).getCharacter(characterJsonDao),parties.get(i).getHitPoint()+heal,characterJsonDao,0);
                new_parties.add(newParty);
                flag = 1;
                target =  newParty.getCharacter(characterJsonDao).getName();
            }else {
                new_parties.add(parties.get(i));
            }
        }
        Adventure new_adventure = new Adventure(adventure.getName(), adventure.getNum_encounters(), adventure.getEncounters(), new_parties );
        if (isLocal()) {
            adventureJsonDAO.update(new_adventure);
        }else {
            adventureApiDAO.update(new_adventure);
        }
        return target;
    }

    /**
     * applies healing on all the party by updating the adventure with all its characters healed a certain amount of hit points.
     * @param heal hit points to be regenerated
     * @param currentAdventure current adventure being played
     */
    public void applyHealOnParty(int heal, String currentAdventure){
        Adventure adventure;
        if (isLocal()) {
            adventure = adventureJsonDAO.getAdventureByName(currentAdventure);
        }
        else {
            adventure = adventureApiDAO.getAdventureByName(currentAdventure);
        }
        List<Party> parties = adventure.getParties();
        List<Party> new_parties = new ArrayList<>();
        int i;
        for (i=0; i < parties.size() ; i++){
            //update adventure with character healed
            Party newParty = new Party(parties.get(i).getCharacter(characterJsonDao),parties.get(i).getHitPoint()+heal,characterJsonDao,0);
            new_parties.add(newParty);
        }
        Adventure new_adventure = new Adventure(adventure.getName(), adventure.getNum_encounters(), adventure.getEncounters(), new_parties );
        if (isLocal()) {
            adventureJsonDAO.update(new_adventure);
        }
        else {
            adventureApiDAO.update(new_adventure);
        }
    }

    /**
     * get the xp gained in an encounter
     * @param adventure_name name of adventure
     * @param encounter_pos encounter position
     * @return the xp gained in an encounter
     */
    public int getXpGainedInEncounter(String adventure_name, int encounter_pos) {
        if (isLocal()) {
            return adventureJsonDAO.getXpGainedInEncounter(adventure_name, encounter_pos);
        }
        return adventureApiDAO.getXpGainedInEncounter(adventure_name, encounter_pos);
    }

    /**
     * apply healing action to the characters
     * @param adventure_name name of adventure
     * @param party_names names of characters in the party
     * @return an array where each position there is the healing value
     */
    public int[] takeHealingActionCharacter(String adventure_name, String[] party_names) {
        int[] healing_arr = new int[party_names.length];
        for (int i=0;i< party_names.length;i++) {
            int mindByName = adventureJsonDAO.getCharactersMindByName(adventure_name,party_names[i]);
            int rand = (int)Math.floor(Math.random() * (8) + 1);
            healing_arr[i] = mindByName + rand;
        }
        return healing_arr;
    }



    /**
     * get the number of adventures in the data set
     * @return the number of adventures
     */
    public int getSize() {
        if (isLocal()) {
            return adventureJsonDAO.getAdventuresSize();
        }
        return adventureApiDAO.getAdventuresSize();
    }

    /**
     * gets the name of the adventure in a specific position
     * @param i the position of the array of object of Adventures
     * @return the adventure name
     */
    public String getAdventureNameByIndex(int i) {
        if (isLocal()) {
            return adventureJsonDAO.getNameByIndex(i);
        }
        return adventureApiDAO.getNameByIndex(i);
    }

    /**
     * checks if a character is alive
     * @param currentAdventure name of adventure
     * @param s character name
     * @return true if a character is alive
     */
    public boolean isPartyAlive(String currentAdventure, String s) {
        if (isLocal()) {
            return !adventureJsonDAO.isPartyUnconsciousByName(currentAdventure, s);
        }
        return !adventureApiDAO.isPartyUnconsciousByName(currentAdventure, s);
    }

    /**
     * checks if a monster is alive
     * @param currentAdventure name of adventure
     * @param encounter_pos encounter position
     * @param s character name
     * @return true if monster is alive
     */
    public boolean isMonsterAlive(String currentAdventure, int encounter_pos, String s) {
        if (isLocal()) {
            return adventureJsonDAO.isMonsterAlive(currentAdventure, encounter_pos, s);
        }
        return adventureApiDAO.isMonsterAlive(currentAdventure, encounter_pos, s);
    }

    /**
     * add xp to each of the characters in the party
     * @param adventure_name name of adventure
     * @param xp_gained the xp that the characters will gain
     * @param parties_inx indexes of all the character in the party
     * @return a list where each position shows if the level of the character has increased or not
     */
    public List<Integer> gainXp(String adventure_name, int xp_gained, int[] parties_inx) {
        Adventure adventure;
        if (isLocal()) {
            adventure= adventureJsonDAO.getAdventureByName(adventure_name);
        }
        else {
            adventure= adventureApiDAO.getAdventureByName(adventure_name);
        }
        List<Party> parties = new ArrayList<>();
        List<Integer> list= new ArrayList<>();
        for (int i=0;i< parties_inx.length;i++) {
            Character character = adventure.getParties().get(i).getCharacter(characterJsonDao);
            int xp = character.getXp() + xp_gained;
            if (characterManager.xpToLevel(character.getXp()) == characterManager.xpToLevel(xp)) {
                list.add(0);
            }
            else {
                list.add(characterManager.xpToLevel(xp));
            }

            int level = characterManager.xpToLevel(character.getXp()); // calculate character level
            String finalClass = characterManager.levelToClass(level,character.getCharClass()); // get class according to character level

            Character new_character = characterJsonDao.assignClass(character.getName(), character.getPlayer(), xp, character.getBody(), character.getMind(), character.getSpirit(), finalClass,0);

            Party new_party = new Party(new_character, adventure.getParties().get(i).getHitPoint(),characterJsonDao,0);
            parties.add(new_party);
        }
        Adventure new_adventure = new Adventure(adventure.getName(), adventure.getNum_encounters(), adventure.getEncounters(), parties);
        if (isLocal()) {
            adventureJsonDAO.update(new_adventure);
        }else {
            adventureApiDAO.update(new_adventure);
        }
        return list;
    }

    /**
     * apply healing values to each of the characters in the party
     * @param adventure_name name of adventure
     * @param bandage_time bandage values
     * @param parties_inx indexes of all the character in the party
     * @return a list where each position shows if the character is healed or not
     */
    public List<Integer> healParty(String adventure_name, int[] bandage_time, int[] parties_inx) {
        Adventure adventure = adventureJsonDAO.getAdventureByName(adventure_name);
        List<Party> parties = new ArrayList<>();
        List<Integer> list= new ArrayList<>();
        for (int i=0;i< parties_inx.length;i++) {
            int hp = adventure.getParties().get(i).getHitPoint() + bandage_time[i];
            Party new_party;
            if (adventureJsonDAO.isPartyUnconsciousByPosition(adventure_name, i)) {
                new_party = new Party(adventure.getParties().get(i).getCharacter(characterJsonDao), 0,characterJsonDao,0);
                list.add(0);
            }
            else {
                new_party = new Party(adventure.getParties().get(i).getCharacter(characterJsonDao), hp,characterJsonDao,0);
                list.add(1);
            }
            parties.add(new_party);
        }
        Adventure new_adventure = new Adventure(adventure.getName(), adventure.getNum_encounters(), adventure.getEncounters(), parties);

        adventureJsonDAO.update(new_adventure);
        return list;
    }

    /**
     * get current number of monsters in an encounter of an adventure
     * @param currentAdventure current adventure
     * @param encounterIndex encounter index in the adventure
     * @return amount of alive monsters, that number being the amount of existing monsters
     */
    public int currentAliveMonsters(List<Combatant> combatants,String currentAdventure, int encounterIndex){
        int count =0;
        for(Combatant c : combatants){
            if(isCombatantMonster(currentAdventure,encounterIndex,c.getName())){
                count++;
            }
        }
        return count;
    }

    /**
     * gets the damage type of an attack performed by a character, since it can be magical, physical, or psychical
     * @param attackerName name of character performing the attack action
     * @param currentAdventure adventure being played
     * @return typology of the attack
     */
    public String getDamageTypeOfAttack(String attackerName, String currentAdventure){
        Adventure adventure;
        if (isLocal()) {
            adventure= adventureJsonDAO.getAdventureByName(currentAdventure);
        }
        else {
            adventure= adventureApiDAO.getAdventureByName(currentAdventure);
        }
        List<Party> parties = adventure.getParties();
        for(Party p: parties){
            if(Objects.equals(p.getCharacter(characterJsonDao).getName(), attackerName)){
                if(Objects.equals(p.getCharacter(characterJsonDao).getCharClass(), "Wizard")){
                    return "Magical";
                } else if (Objects.equals(p.getCharacter(characterJsonDao).getCharClass(), "Cleric") || Objects.equals(p.getCharacter(characterJsonDao).getCharClass(), "Paladin")) {
                    return "Psychical";
                }else
                    return "Physical";
            }
        }
        return "Physical";
    }


    /**
     * gets list of characters from a party
     * @param currentAdventure adventure being played
     * @return list of characters in the party of the given adventure
     */
    public List<Character> getCharactersFromParty(String currentAdventure){
        Adventure a;
        if (isLocal()) {
            a = adventureJsonDAO.getAdventureByName(currentAdventure);
        }
        else {
            a = adventureApiDAO.getAdventureByName(currentAdventure);
        }
        List<Character> finalList = new ArrayList<>();
        for (Party p: a.getParties()){
            finalList.add(p.getCharacter(characterJsonDao));
        }
        return finalList;
    }

    /**
     * gets a copy of a given adventure
     * @param currentAdventure adventure to get
     * @return copy of given adventure
     */

    public Adventure getCopyAdventure(String currentAdventure) {
        if (isLocal()) {
            return adventureJsonDAO.getAdventureByName(currentAdventure);
        }
        return adventureApiDAO.getAdventureByName(currentAdventure);
    }

    /**
     * resets adventure with original encounters and empty party
     * @param adventure_copy copy of adventure to be updated
     */
    public void resetAdventure(Adventure adventure_copy) {
        if (isLocal()) {
            adventureJsonDAO.update(adventure_copy);
        }
        else {
            adventureApiDAO.update(adventure_copy);
        }
    }
}
