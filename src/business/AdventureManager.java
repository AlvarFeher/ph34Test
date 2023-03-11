package business;

import business.entities.*;
import business.entities.Character;
import business.entities.Classes.*;
import persistence.AdventureDAO;
import persistence.AdventureDAO;
import persistence.JSON.AdventureJsonDAO;
import persistence.JSON.CharacterJsonDAO;
import persistence.JSON.MonstersJsonDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * manages adventure related features logic
 * @author Youssef Bat, Alvaro Feher
 */
public class AdventureManager {

    private final CharacterManager characterManager;
    private final AdventureDAO adventureJsonDAO;
    private final CharacterJsonDAO characterJsonDao;
    private final MonstersJsonDAO monstersJsonDAO;

    /**
     * constructor
     */
    public AdventureManager() {
        adventureJsonDAO = new AdventureJsonDAO();
        characterManager = new CharacterManager();
        characterJsonDao = new CharacterJsonDAO();
        monstersJsonDAO = new MonstersJsonDAO();
    }

    /**
     * checks if the name of the adventure exists already
     * @param adventure_name the name of the adventure
     * @return true if the name is unique, false otherwise
     */
    public boolean isAdventureNameUnique(String adventure_name) {
        for (Adventure a: adventureJsonDAO.getAll()){
            if (Objects.equals(a.getName(), adventure_name)){
                return false;
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
        Adventure adventure = adventureJsonDAO.getAdventureByName(adventure_name);
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
        Adventure adventure = adventureJsonDAO.getAdventureByName(adventure_name);
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

    public Party getPartyMemberByName(String currentAdventure, String partyName){
        List<Party> parties = adventureJsonDAO.getPartyByName(currentAdventure);
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
        List<Adventure> adventures = adventureJsonDAO.getAll();
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
        Adventure adventure = adventureJsonDAO.getAdventureByName(adventure_name);
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
        adventureJsonDAO.update(new_adventure);
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
        adventureJsonDAO.add(adventure);
    }

    /**
     * delete all monsters with the monster name that are in the encounter index of the adventure that has the adventure name
     * @param adventure_name the name of the adventure we are working with
     * @param encounterIndex the encounter position
     * @param monster_name the monster to be deleted
     */
    public void deleteMonster(String adventure_name, int encounterIndex, String monster_name){
        Adventure adventure = adventureJsonDAO.getAdventureByName(adventure_name);
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
        adventureJsonDAO.update(new_adventure);
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
        return adventureJsonDAO.getNumOfEncountersByName(currentAdventure);
    }

    /**
     * gets all monsters unfiltered in a specific encounter of a specific adventure
     * @param i the encounter index of an adventure
     * @param currentAdventure the name of the adventure
     * @return all monsters in a specific encounter of a specific adventure
     */
    public List<String> getMonsterNamesInEncounterUnfiltered(int i, String currentAdventure) {
        return adventureJsonDAO.getMonstersInEncounter(i, currentAdventure);
    }

    /**
     * updates the adventure by adding
     * @param currentAdventure the name of the adventure
     * @param parties_inx an array that contains in each position the position of a character
     */
    public void updateParty(String currentAdventure, int[] parties_inx) {
        Adventure adventure = adventureJsonDAO.getAdventureByName(currentAdventure);
        List<Character> characterList = characterManager.getCharactersByIndexes(parties_inx);

        List<Party> parties = new ArrayList<>();
        List<Integer> hit_points = characterManager.getMaxHitPointsByindex(parties_inx);

        for (int i=0;i< parties_inx.length;i++) {
            Character c = characterList.get(i);
            parties.add(new Party(characterJsonDao.assignClass(c.getName(),c.getPlayer(),c.getXp(),c.getBody(),c.getMind(),c.getSpirit(),c.getCharClass()), hit_points.get(i),characterJsonDao));
        }
        Adventure new_adventure = new Adventure(adventure.getName(), adventure.getNum_encounters(), adventure.getEncounters(), parties);
        adventureJsonDAO.update(new_adventure);
    }

    /**
     * updates the adventure in preparation stage
     * @param currentAdventure name of the adventure
     * @param parties_inx array of character's positions
     */
    public void updatePartyInPrepStage(String currentAdventure, int[] parties_inx) {
        // in this phase we only work with adventurer class therefore, the update is +1 in spirit
        Adventure adventure = adventureJsonDAO.getAdventureByName(currentAdventure);
        List<Party> parties = new ArrayList<>();

        for (int i=0;i< parties_inx.length;i++) {
            Character character = adventure.getParties().get(i).getCharacter(characterJsonDao);
            parties = character.preparationStageAction(adventure.getParties(),character.getName(),characterJsonDao);
            Adventure new_adventure = new Adventure(adventure.getName(), adventure.getNum_encounters(), adventure.getEncounters(), parties);
            adventureJsonDAO.update(new_adventure);
        }
    }

    public void updatePartyInShortRestStage(String currentAdventure, int[] parties_inx){
        Adventure adventure = adventureJsonDAO.getAdventureByName(currentAdventure);
        List<Party> parties = new ArrayList<>();

        for (int i = 0; i < parties_inx.length; i++) {
            Character character = adventure.getParties().get(i).getCharacter(characterJsonDao);
            parties = character.shortRestAction(adventure.getParties(),character.getName(),characterJsonDao);
            Adventure new_adventure = new Adventure(adventure.getName(), adventure.getNum_encounters(), adventure.getEncounters(), parties);
            adventureJsonDAO.update(new_adventure);
        }
    }

    // update party with the gained xp of the adventure
    public void updatePartyXpGaining(){

    }

    /**
     * checks if a combatant is a monster
     * @param currentAdventure name of adventure
     * @param encounter_pos encounter position
     * @param name name of combatant
     * @return true if combatant is a monster
     */
    public boolean isCombatantMonster(String currentAdventure, int encounter_pos, String name) {
        return adventureJsonDAO.isNameMonster(currentAdventure, encounter_pos, name);
    }

    /**
     * gets the attack action value of a character
     * @param currentAdventure name of the adventure
     * @param name name of the character
     * @return the attack action value of the character
     */

    // IMPORTANT !! needHealing is 1 if healing is needed, 0 if not
    public int takeAttackActionCharacter(String currentAdventure, String name, int currentAliveMonsters, int needHealing) {
        int damage = 0;

        List<Party> parties = adventureJsonDAO.getPartyByName(currentAdventure);
        Character character;
        for(Party p: parties){
            if(Objects.equals(p.getCharacter(characterJsonDao).getName(), name)){
                Character c = p.getCharacter(characterJsonDao);
                character = characterJsonDao.assignClass(c.getName(),c.getPlayer(),c.getXp(),c.getBody(),c.getMind(),c.getSpirit(),c.getCharClass());

                damage = character.doAction() + character.doAction(needHealing,currentAliveMonsters);
            }
        }
        return damage;
    }


    /**
     * check if any member from the party has its hp lower than the initial half
     * @param currentAdventure current adventure
     * @return boolean
     */

    // FIXME: add maximum hit points attribute to Party class
    public int checkPartyHalfHp(String currentAdventure){
        for(Party c: adventureJsonDAO.getPartyByName(currentAdventure)){
            if(c.getHitPoint() < c.getHitPoint()/2){
                return 1;
            }
        }
        return 0;
    }

    /**
     * check if anyone in the party needs healing
     * @return
     */
    // FIXME: add maximum hit points attribute to Party class
    public boolean checkHealingNeeded(String currentAdventure, List<Integer> maxHitPoints){
        int i=0;
        for(Party c: adventureJsonDAO.getPartyByName(currentAdventure)){
            if(c.getHitPoint() < maxHitPoints.get(i)/2){
                return true;
            }
            i++;
        }
        return false;
    }

    /**
     * gets the attack action value of a monster
     * @param currentAdventure name of the adventure
     * @param encounter_pos encounter position
     * @param name name of monster
     * @return the attack action value of the monster
     */
    public int  takeAttackActionMonster(String currentAdventure, int encounter_pos, String name) {
        int max = adventureJsonDAO.getDamageDiceByName(currentAdventure, encounter_pos, name);
        // aqui meter el damage dependiendo de que monster sea
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
        return adventureJsonDAO.areMonstersAllDead(currentAdventure, encounter_pos) || adventureJsonDAO.arePartyAllUnconscious(currentAdventure);
    }

    /**
     * checks if the adventure reached a TPU case, which means all characters in the party are unconscious
     * @param currentAdventure name of the adventure
     * @param encounter_pos encounter position
     * @return true if the adventure reached a TPU case
     */
    public boolean isTPU(String currentAdventure, int encounter_pos) {
        return adventureJsonDAO.arePartyAllUnconscious(currentAdventure);
    }

    // FIXME
    // attack from Boss Monster
    public String applyDamageOnAllParty(int damage, String currentAdventure, String damageType){
        if (adventureJsonDAO.arePartyAllUnconscious(currentAdventure)) {
            return null;
        }
        Adventure adventure = adventureJsonDAO.getAdventureByName(currentAdventure);
        Adventure newAdventure;
        List<Party> characters = adventureJsonDAO.getPartyByName(currentAdventure);
        List<Party> parties = new ArrayList<>();
        int i=0;
        boolean unconscious = false;
        for(Party p:characters){
            if(characters.get(i).getCharacter(characterJsonDao) instanceof Warrior || characters.get(i).getCharacter(characterJsonDao) instanceof Champion ){
                if(Objects.equals(damageType, "Physical")){
                    if(characters.get(i).getHitPoint() - damage/2 > 0){
                        parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage/2,characterJsonDao));
                    }else{
                        parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao));
                        unconscious = true;
                    }
                }else{
                    if(characters.get(i).getHitPoint() - damage > 0){
                        parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage,characterJsonDao));
                    }else{
                        parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao));
                        unconscious = true;
                    }
                }
            }

            if(characters.get(i).getCharacter(characterJsonDao) instanceof Paladin){
                if(Objects.equals(damageType, "Psychical")){
                    if(characters.get(i).getHitPoint() - damage/2 > 0){
                        parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage/2,characterJsonDao));
                    }else{
                        parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao));
                        unconscious = true;
                    }
                }else{
                    if(characters.get(i).getHitPoint() - damage > 0){
                        parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage,characterJsonDao));
                    }else{
                        parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao));
                        unconscious = true;
                    }                        }
            }

            if(characters.get(i).getCharacter(characterJsonDao) instanceof Wizard){
                int damageTaken = damage -  characterManager.xpToLevel(characters.get(i).getCharacter(characterJsonDao).getXp());
                int shield = ((Wizard) characters.get(i).getCharacter(characterJsonDao)).getShield();
                if(Objects.equals(damageType, "Magical")){
                    if(shield > 0){
                        parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), shield - damageTaken,characterJsonDao));
                    }else{
                        if(characters.get(i).getHitPoint() - damageTaken > 0){
                            parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damageTaken,characterJsonDao));
                        }else{
                            parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao));
                            unconscious = true;
                        }
                    }
                }else{
                    if(shield > 0){
                        parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), shield - damage,characterJsonDao));
                    }else{
                        if(characters.get(i).getHitPoint() - damage > 0){
                            parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage,characterJsonDao));
                        }else{
                            parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao));
                            unconscious = true;
                        }
                    }
                }
            }
            if(characters.get(i).getCharacter(characterJsonDao) instanceof Adventurer || characters.get(i).getCharacter(characterJsonDao) instanceof Cleric){
                if(characters.get(i).getHitPoint() - damage > 0){
                    parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage,characterJsonDao));
                }else{
                    parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao));
                    unconscious = true;
                }
            }

            i++;
        }

        newAdventure =  new Adventure(adventure.getName(), adventure.getNum_encounters(), adventure.getEncounters(), parties);
        adventureJsonDAO.update(newAdventure);
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
     * the monster whose turn to attack applies its damage dice on a non-unconscious party
     * @param damage damage value
     * @param current_adventure name of the adventure
     * @param parties_inx the parties index
     * @return the name of the character being attacked
     */
    public String applyDamageOnRandomConsciousParty(int damage, String current_adventure, int[] parties_inx, String damageType) {
        if (adventureJsonDAO.arePartyAllUnconscious(current_adventure)) {
            return null;
        }
        Adventure adventure = adventureJsonDAO.getAdventureByName(current_adventure);
        Adventure new_adventure;
        int party_pos;

        List<Party> characters = adventureJsonDAO.getPartyByName(current_adventure);
        List<Party> parties = new ArrayList<>();

        do {
            party_pos = (int) Math.floor(Math.random() * (parties_inx.length) + 1) - 1;
        }while (adventureJsonDAO.isPartyUnconsciousByPosition(current_adventure, party_pos));

        boolean unconscious = false;
        for (int i=0;i < parties_inx.length;i++) {
            if (i == party_pos) {
                    if(characters.get(i).getCharacter(characterJsonDao) instanceof Warrior || characters.get(i).getCharacter(characterJsonDao) instanceof Champion ){
                        if(Objects.equals(damageType, "Physical")){
                            if(characters.get(i).getHitPoint() - damage/2 > 0){
                                parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage/2,characterJsonDao));
                            }else{
                                parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao));
                                unconscious = true;
                            }
                        }else{
                            if(characters.get(i).getHitPoint() - damage > 0){
                                parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage,characterJsonDao));
                            }else{
                                parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao));
                                unconscious = true;
                            }
                        }
                    }

                    if(characters.get(i).getCharacter(characterJsonDao) instanceof Paladin){
                        if(Objects.equals(damageType, "Psychical")){
                            if(characters.get(i).getHitPoint() - damage/2 > 0){
                                parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage/2,characterJsonDao));
                            }else{
                                parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao));
                                unconscious = true;
                            }
                        }else{
                            if(characters.get(i).getHitPoint() - damage > 0){
                                parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage,characterJsonDao));
                            }else{
                                parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao));
                                unconscious = true;
                            }                        }
                    }

                    if(characters.get(i).getCharacter(characterJsonDao) instanceof Wizard){
                        int damageTaken = damage -  characterManager.xpToLevel(characters.get(i).getCharacter(characterJsonDao).getXp());
                        int shield = ((Wizard) characters.get(i).getCharacter(characterJsonDao)).getShield();
                        if(Objects.equals(damageType, "Magical")){
                            if(shield > 0){
                                parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), shield - damageTaken,characterJsonDao));
                            }else{
                                if(characters.get(i).getHitPoint() - damageTaken > 0){
                                    parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damageTaken,characterJsonDao));
                                }else{
                                    parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao));
                                    unconscious = true;
                                }
                            }
                        }else{
                            if(shield > 0){
                                parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), shield - damage,characterJsonDao));
                            }else{
                                if(characters.get(i).getHitPoint() - damage > 0){
                                    parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage,characterJsonDao));
                                }else{
                                    parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao));
                                    unconscious = true;
                                }
                            }
                        }
                    }

                    if(characters.get(i).getCharacter(characterJsonDao) instanceof Adventurer || characters.get(i).getCharacter(characterJsonDao) instanceof Cleric){
                        if(characters.get(i).getHitPoint() - damage > 0){
                            parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint() - damage,characterJsonDao));
                        }else{
                            parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), 0,characterJsonDao));
                            unconscious = true;
                        }
                    }
            }
            else {
                parties.add(new Party(characters.get(i).getCharacter(characterJsonDao), characters.get(i).getHitPoint(),characterJsonDao));
            }
        }
        new_adventure =  new Adventure(adventure.getName(), adventure.getNum_encounters(), adventure.getEncounters(), parties);
        adventureJsonDAO.update(new_adventure);
        if (unconscious) {
            return characters.get(party_pos).getCharacter(characterJsonDao).getName() + " falls unconscious";
        }
        else {
            return characters.get(party_pos).getCharacter(characterJsonDao).getName();
        }
    }

    public boolean isMonsterBoss(String name){
        List<Monster> monsters = monstersJsonDAO.getAll();
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
        List<Party> parties = adventureJsonDAO.getPartyByName(current_adventure);
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
     * the character whose turn to attack applies its damage dice on a monster
     * @param damage damage value
     * @param currentAdventure name of the adventure
     * @param encounterPos encounter position
     * @return the name of the monster being attacked
     */
    public String applyDamageOnRandomMonsterInEncounter(int damage, String currentAdventure, int encounterPos, String attackType) {
        Adventure adventure = adventureJsonDAO.getAdventureByName(currentAdventure);
        List<Monster> monsters = adventure.getEncounters().get(encounterPos);
        List<Monster> new_monsters = new ArrayList<>();
        String s = "";
        int monster_pos = (int)Math.floor(Math.random() * (monsters.size()));

        int[] aliveMonsters;

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
        adventureJsonDAO.update(new_adventure);
        if (dead) {
            return s + " dies";
        }
        else {
            return s;
        }
    }

    public void applyDamageOnAllMonsters(int damage, String currentAdventure, int encounterPos, String attackType){
        Adventure adventure = adventureJsonDAO.getAdventureByName(currentAdventure);
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
        adventureJsonDAO.update(newAdventure);
    }

    // should only heal a single character
    public String applyHealOnCharacter( int heal, String currentAdventure, List<Integer> maxHitPoints){
        Adventure adventure = adventureJsonDAO.getAdventureByName(currentAdventure);
        List<Party> parties = adventure.getParties();
        List<Party> new_parties = new ArrayList<>();
        int flag =0;
        String target = "";
        for (int i=0; i < parties.size(); i++){
            if(parties.get(i).getHitPoint() < maxHitPoints.get(i)/2 && flag == 0){
                //update adventure with character healed
                Party newParty = new Party(parties.get(i).getCharacter(characterJsonDao),parties.get(i).getHitPoint()+heal,characterJsonDao);
                new_parties.add(newParty);
                flag = 1;
                target =  newParty.getCharacter(characterJsonDao).getName();
            }else {
                new_parties.add(parties.get(i));
            }
        }
        Adventure new_adventure = new Adventure(adventure.getName(), adventure.getNum_encounters(), adventure.getEncounters(), new_parties );
        adventureJsonDAO.update(new_adventure);
        return target;
    }

    public void applyHealOnParty(int heal, String currentAdventure){
        Adventure adventure = adventureJsonDAO.getAdventureByName(currentAdventure);
        List<Party> parties = adventure.getParties();
        List<Party> new_parties = new ArrayList<>();
        int i;
        for (i=0; i < parties.size() ; i++){
            //update adventure with character healed
            Party newParty = new Party(parties.get(i).getCharacter(characterJsonDao),parties.get(i).getHitPoint()+heal,characterJsonDao);
            new_parties.add(newParty);
        }
        Adventure new_adventure = new Adventure(adventure.getName(), adventure.getNum_encounters(), adventure.getEncounters(), new_parties );
        adventureJsonDAO.update(new_adventure);
    }

    /**
     * get the xp gained in an encounter
     * @param adventure_name name of adventure
     * @param encounter_pos encounter position
     * @return the xp gained in an encounter
     */
    public int getXpGainedInEncounter(String adventure_name, int encounter_pos) {
        return adventureJsonDAO.getXpGainedInEncounter(adventure_name, encounter_pos);
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
        return adventureJsonDAO.getAdventuresSize();
    }

    /**
     * gets the name of the adventure in a specific position
     * @param i the position of the array of object of Adventures
     * @return the adventure name
     */
    public String getAdventureNameByIndex(int i) {
        return adventureJsonDAO.getNameByIndex(i);
    }

    /**
     * checks if a character is alive
     * @param currentAdventure name of adventure
     * @param s character name
     * @return true if a character is alive
     */
    public boolean isPartyAlive(String currentAdventure, String s) {
        return !adventureJsonDAO.isPartyUnconsciousByName(currentAdventure, s);
    }

    /**
     * checks if a monster is alive
     * @param currentAdventure name of adventure
     * @param encounter_pos encounter position
     * @param s character name
     * @return true if monster is alive
     */
    public boolean isMonsterAlive(String currentAdventure, int encounter_pos, String s) {
        return adventureJsonDAO.isMonsterAlive(currentAdventure, encounter_pos, s);
    }

    /**
     * add xp to each of the characters in the party
     * @param adventure_name name of adventure
     * @param xp_gained the xp that the characters will gain
     * @param parties_inx indexes of all the character in the party
     * @return a list where each position shows if the level of the character has increased or not
     */
    public List<Integer> gainXp(String adventure_name, int xp_gained, int[] parties_inx) {
        Adventure adventure = adventureJsonDAO.getAdventureByName(adventure_name);
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

            Character new_character = characterJsonDao.assignClass(character.getName(), character.getPlayer(), xp, character.getBody(), character.getMind(), character.getSpirit(), finalClass);

            Party new_party = new Party(new_character, adventure.getParties().get(i).getHitPoint(),characterJsonDao);
            parties.add(new_party);
        }
        Adventure new_adventure = new Adventure(adventure.getName(), adventure.getNum_encounters(), adventure.getEncounters(), parties);
        adventureJsonDAO.update(new_adventure);
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
                new_party = new Party(adventure.getParties().get(i).getCharacter(characterJsonDao), 0,characterJsonDao);
                list.add(0);
            }
            else {
                new_party = new Party(adventure.getParties().get(i).getCharacter(characterJsonDao), hp,characterJsonDao);
                list.add(1);
            }
            parties.add(new_party);
        }
        Adventure new_adventure = new Adventure(adventure.getName(), adventure.getNum_encounters(), adventure.getEncounters(), parties);

        adventureJsonDAO.update(new_adventure);
        return list;
    }

    /**
     * reeturns current number of monsters in an encounter of an adventure
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
        System.out.println("current alive monsters: "+count +"\n");
        return count;
    }

    public String getDamageTypeOfAttack(String attackerName, String currentAdventure){
        Adventure adventure= adventureJsonDAO.getAdventureByName(currentAdventure);
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


    public void testPrint(String currentAdventure, int encounterIndex){
        Adventure adventure = adventureJsonDAO.getAdventureByName(currentAdventure);
        List<Party> parties = adventure.getParties();
        List<List<Monster>> encounters = adventure.getEncounters();

        for(Party p: parties){
            System.out.println("name: "+p.getCharacter(characterJsonDao).getName()+"  hp: "+p.getHitPoint());
        }
        for(Monster m: encounters.get(encounterIndex) ){
            System.out.println("name: "+m.getName()+"  hp: "+m.getHitPoints());
        }
    }


    public Adventure getCopyAdventure(String currentAdventure) {
        return adventureJsonDAO.getAdventureByName(currentAdventure);
    }

    public void resetAdventure(Adventure adventure_copy) {
        adventureJsonDAO.update(adventure_copy);
    }
}
