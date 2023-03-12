package presentation;


import business.*;
import business.entities.*;
import business.entities.Character;
import business.entities.Classes.*;
import persistence.JSON.CharacterJsonDAO;

import java.io.IOException;
import java.util.*;

/**
 * controls the interaction of the user input with the system
 */
public class UIController {
    private final ConsoleUIManager consoleUI;
    private final CharacterManager characterManager;
    private final MonsterManager monsterManager;
    private final AdventureManager adventureManager;
    private CombatantManager combatantManager;

    private boolean isLocal = true; // temporary variable for storage management
    /**
     * default constructor
     */
    public UIController() {
        consoleUI = new ConsoleUIManager();
        characterManager = new CharacterManager();
        adventureManager = new AdventureManager();
        monsterManager = new MonsterManager();
        combatantManager = new CombatantManager();
    }

    /**
     * shows the logo as the program starts, and runs the rest of the program
     */
    public void startProgram() throws IOException {
        consoleUI.showLogo();

        // todo: add persistence.API data option
        switch (consoleUI.showStorageMenuOptions()){
            case CLOUD -> isLocal = false;
            case LOCAL -> isLocal = true;
        }

        loadAndConnect();
    }


    private void loadAndConnect() {
        consoleUI.loadData();
        if (isLocal) {
            monsterManager.setIsLocal(true);
            if (monsterManager.loadMonsters() == null) {
                consoleUI.showLoadingError(false, true);
            } else {
                consoleUI.showLoadingError(true, true);
                isLocal = true;
                run();
            }
        }
        else {
            if (monsterManager.loadMonsters() == null) {
                consoleUI.showLoadingError(false, false);
                isLocal = true;
                monsterManager.setIsLocal(true);
                loadAndConnect();
            } else {
                consoleUI.showLoadingError(true, false);
                isLocal = false;
                monsterManager.setIsLocal(false);
                run();
            }
        }
    }

    /**
     * controls the main menu options
     */
    private void run() {
        adventureManager.setLocal(isLocal);
        combatantManager.setLocal(isLocal);
        characterManager.setLocal(isLocal);


        int characterCount = characterManager.getCharacterCount();

        switch (consoleUI.showMainMenu(characterCount)) {
            case CHARACTER_CREATION -> characterCreation();
            case LIST_CHARACTERS -> listCharacters();
            case CREATE_ADVENTURE -> createAdventure();
            case START_ADVENTURE -> {
                if(characterCount >= 3){
                    startAdventure();
                }else{
                    consoleUI.startAdventureMsg(-1);
                    run();
                }
            }
            case EXIT -> consoleUI.exit();
        }
    }


    /**
     * goes through a set of user interaction in order to create a new unique character
     */
    private void characterCreation() {
        String name;
        do {
            name = consoleUI.getUserName();
            if (characterManager.isNameValid(name)) {
                consoleUI.showCharacterNameNotValid();
            }
        } while (characterManager.isNameValid(name));
        String player_name = consoleUI.getPlayerName(name);
        int level = consoleUI.getPlayerLevel();
        consoleUI.generateStats(level);
        int body_roll = consoleUI.showRoleDice("Body", characterManager.rollDice(), characterManager.rollDice());
        int mind_roll = consoleUI.showRoleDice("Mind", characterManager.rollDice(), characterManager.rollDice());
        int spirit_roll = consoleUI.showRoleDice("Spirit", characterManager.rollDice(), characterManager.rollDice());
        int body = characterManager.generateStats(body_roll);
        int mind = characterManager.generateStats(mind_roll);
        int spirit = characterManager.generateStats(spirit_roll);
        consoleUI.showStats(body, mind, spirit);
        String initialClass = consoleUI.askInitialClass();
        int is_created = characterManager.createCharacter(name, player_name,level,body, mind, spirit, initialClass);
        consoleUI.showCharacterCreationStatus(characterManager.adjustCharacterName(name),is_created);
        run();
    }


    /**
     * list all the characters that contains a substring, and the option to delete a character
     */
    private void listCharacters() {
        String name = consoleUI.getPlayerNameToFilter();
        int index = consoleUI.filterPlayer(characterManager.listCharacters(name));
        if (index != 0) {
            Character character = characterManager.getCharacterByindex(name, index);
            int level = characterManager.xpToLevel(character.getXp());
            String character_to_delete = consoleUI.showFullCharacterInfo(character.getName(), character.getPlayer(), level, character.getXp(), character.getBody(), character.getMind(), character.getSpirit(), character.getCharClass());
            if (!character_to_delete.isEmpty()) {
                characterManager.deleteCharacter(character_to_delete);
                consoleUI.showCharacterDeletionStatus(character_to_delete);
            }
        }
        run();
    }


    /**
     * create a new unique adventure with all of its encounters
     */
    private void createAdventure() {
        // get name and num of encounters
        String adventure_name = consoleUI.getAdventureName();
        if (!adventureManager.isAdventureNameUnique(adventure_name)) {
            consoleUI.showAdventureIsNotUnique();
        }
        else {
            int encountering_num = consoleUI.getEncounteringNum(adventure_name);
            if (encountering_num != -1) {
                adventureManager.createAdventure(adventure_name, encountering_num);
                // load monsters
                List<String> monsters = monsterManager.getAllMonstersName();
                List<String> challenges = monsterManager.getAllMonstersChallenges();
                for (int i = 1; i <= encountering_num; i++) {
                    boolean stay = true;
                    do {
                        consoleUI.showMonstersMsg(i, encountering_num);
                        List<String> monstersInEncounter = adventureManager.getMonsterNamesInEncounter(i-1, adventure_name);
                        if (monstersInEncounter.size() == 0) {
                            consoleUI.printEmpty();
                        } else {
                            for (int j=0;j<monstersInEncounter.size();j++) {
                                String monster = monstersInEncounter.get(j);
                                consoleUI.printMonsterInEncounter(j, monster, adventureManager.occurrenceMonsterInEncounter(i-1, adventure_name, monster));
                            }
                        }
                        switch (consoleUI.showMonstersMenu()) {
                            case ADD_MONSTER -> {
                                addMonster(monsters, challenges, adventure_name, i);
                            }
                            case DELETE_MONSTER -> {
                                deleteMonster(monstersInEncounter, adventure_name, i);
                            }
                            case CONTINUE -> stay = false;
                        }
                    } while (stay);

                }
                consoleUI.showAdventureCreated(adventure_name);
            }
        }
        run();
    }

    /**
     * deletes a set of monsters from an encounter of an adventure
     * @param monstersInEncounter the different monsters in a specific encounter of a specific adventure
     * @param adventure_name the name of the adventure
     * @param i the position of the encounter
     */
    private void deleteMonster(List<String> monstersInEncounter, String adventure_name, int i) {
        int monsterIndex = consoleUI.getNumOfMonsterToDelete(monstersInEncounter.size());
        if (monsterIndex == Integer.MIN_VALUE) {
            consoleUI.showNoMonsterInEncounter();
        }
        else {
            adventureManager.deleteMonster(adventure_name, i-1, monstersInEncounter.get(monsterIndex-1));
        }
    }

    /**
     * add monsters to the encounter
     * @param monsters a list of all monster names
     * @param challenges a list of all the monster's challenges
     * @param adventure_name the name of the adventure
     * @param i the encounter position
     */
    private void addMonster(List<String> monsters, List<String> challenges, String adventure_name, int i) {
        consoleUI.showAvailableMonsters(monsters, challenges);
        int monsterIndex = consoleUI.getMonsterFromMenu(monsters.size()) - 1;
        String currentMonsterName = monsters.get(monsterIndex);
        int monsterAmount = consoleUI.getNumMonstersToAdd(currentMonsterName);
        //check if monster to be added is not boss level for more than once
        if (monsterManager.isMonsterBoss(currentMonsterName)) {
            if (adventureManager.canMonsterBeAdded(adventure_name ,monsterAmount, i)) {
                //List<Monster> monstersToAdd = monsterManager.listMonstersOfEncounter(monsterAmount, currentMonsterName);
                adventureManager.updateAdventure(adventure_name, i-1, monsterManager.listMonstersOfEncounter(monsterAmount, currentMonsterName));
            }
            else {
                consoleUI.showMonsterIsBoss();
            }
        }
        else {
            //List<Monster> monstersToAdd = monsterManager.listMonstersOfEncounter(monsterAmount, currentMonsterName);
            adventureManager.updateAdventure(adventure_name, i-1, monsterManager.listMonstersOfEncounter(monsterAmount, currentMonsterName));
        }
    }

    /**
     * controlling the adventure execution process
     */
    private void startAdventure() {

        int adventure_size = adventureManager.getSize();
        consoleUI.startAdventureMsg(adventure_size);
        if (adventure_size > 0) {
            for (int i = 0; i < adventure_size; i++) {
                consoleUI.showAvailableAdventures(i, adventureManager.getAdventureNameByIndex(i));
            }
            int adventure_index = consoleUI.chooseAdventure(adventure_size) - 1;
            String currentAdventure = adventureManager.getAdventureNameByIndex(adventure_index);

            Adventure adventure_copy = adventureManager.getCopyAdventure(currentAdventure);
            int characterCount = characterManager.getCharacterCount();
            int characterNum = consoleUI.chooseNumOfCharactersAdventure(currentAdventure, characterCount);
;

            int[] parties_inx = new int[characterNum];
            Arrays.fill(parties_inx, Integer.MIN_VALUE);


            for (int i = 1; i <= characterNum; i++) {
                consoleUI.showCurrentParty(characterManager.getPartyNames(parties_inx), characterNum, i - 1);
                consoleUI.showAvailableCharacters(characterManager.getAllCharacterNames());
                int num_characters = characterManager.getCharacterCount();
                int chosenCharacterIndex = consoleUI.chooseCharacterForParty(i, num_characters) - 1;

                if (adventureManager.checkCharacterRepetition(parties_inx, i - 1, chosenCharacterIndex)) {
                    parties_inx[i - 1] = chosenCharacterIndex;
                } else {
                    consoleUI.characterHasBeenChoosen();
                    i--;
                }
            }
            consoleUI.showCurrentParty(characterManager.getPartyNames(parties_inx), characterNum, characterNum);
            consoleUI.showEndOfFillingPartyInAdventure(currentAdventure);

            //get max hit points
            List<Integer> max_hit_points = characterManager.getMaxHitPointsByindex(parties_inx);

            adventureManager.updateParty(currentAdventure, parties_inx);

            for (int i = 0; i < adventureManager.getNumOfEncountersByName(currentAdventure); i++) {
                int xp_gain = adventureManager.getXpGainedInEncounter(currentAdventure, i);
                consoleUI.showEncounterMsg(i);
                List<String> monsters = adventureManager.getMonsterNamesInEncounter(i, currentAdventure);
                for (String monster : monsters) {
                    consoleUI.showMonsterInEncounter(monster, adventureManager.occurrenceMonsterInEncounter(i, currentAdventure, monster));
                }

                //preparation_stage
                consoleUI.printPreparationStageTitle();

                //adding attribute bonus to party members from preparation stage actions
                adventureManager.updatePartyInPrepStage(currentAdventure, parties_inx);
                consoleUI.showPrepStageActions(adventureManager.getCharactersFromParty(currentAdventure));

                List<Combatant> combatants = combatantManager.rollInitiative(characterManager.getPartyNames(parties_inx), adventureManager.getMonsterNamesInEncounterUnfiltered(i, currentAdventure));
                consoleUI.showRollingInitiative(combatantManager.getNames(combatants), combatantManager.getInitValues(combatants));

                //combat stage
                combatStage(i, parties_inx, currentAdventure, max_hit_points, combatants);

                if (adventureManager.isTPU(currentAdventure)) {
                    consoleUI.showTPU();
                    run();
                }

                //short rest stage
                shortRestStageXp(parties_inx, currentAdventure, xp_gain);  // gain xp
                adventureManager.updatePartyInShortRestStage(currentAdventure,parties_inx); // character actions
                consoleUI.showShortRestActions(adventureManager.getCharactersFromParty(currentAdventure));
            }

            consoleUI.showPartyCompleteAdventure(currentAdventure);
            adventureManager.resetAdventure(adventure_copy);
        }

        run();

    }


    /**
     * controlling the combat stage of the adventure execution
     * @param encounter_pos the encounter position
     * @param parties_inx the parties in an adventure
     * @param currentAdventure the name of the adventure
     * @param max_hit_points a list of the maximum hit points of the parties
     * @param combatants a list of the combatant in an encounter
     */
    private void combatStage(int encounter_pos, int[] parties_inx, String currentAdventure, List<Integer> max_hit_points, List<Combatant> combatants) {
        consoleUI.startCombatStage();
        List<String> combatants_name = combatantManager.getNames(combatants);

        int[] deadCombatants = new int[combatants_name.size()]; // track which combatants are alive or dead: 0 = alive ; 1 = dead
        int round = 0;

        while(!adventureManager.isCombatEnd(currentAdventure, encounter_pos)) {

            consoleUI.showRoundCombatStage(round, characterManager.getPartyNames(parties_inx), adventureManager.getHitPointsByindex(currentAdventure), max_hit_points);

            for (Combatant c : combatants) {
                int actionValue;
                String damageType;
                int rollDiced = adventureManager.isItAHit();
                if (adventureManager.isCombatantMonster(currentAdventure, encounter_pos, c.getName())) {
                    if (adventureManager.isMonsterAlive(currentAdventure, encounter_pos, c.getName())) {
                        actionValue = adventureManager.takeAttackActionMonster(currentAdventure, encounter_pos, c.getName());
                        damageType = monsterManager.getDamageTypeOfMonster(c.getName());
                        String party;
                        if(adventureManager.isMonsterBoss(c.getName())){
                            party = adventureManager.applyDamageOnAllParty(actionValue*rollDiced,currentAdventure,damageType);
                        }else{
                            party = adventureManager.applyDamageOnRandomConsciousParty(actionValue * rollDiced, currentAdventure, parties_inx, damageType);
                        }
                        if (party == null) {
                            return;
                        }
                        consoleUI.showAttackAction(adventureManager.getCharactersFromParty(currentAdventure),null,adventureManager.isMonsterBoss(c.getName()),0, c.getName(), party, rollDiced, actionValue * rollDiced,"physical");
                    }
                }
                else {
                    if (adventureManager.isPartyAlive(currentAdventure, c.getName())) {

                        // we are considering both healing and damage value as the same
                        actionValue = adventureManager.takeAttackActionCharacter(currentAdventure, c.getName(), adventureManager.currentAliveMonsters(combatants, currentAdventure, encounter_pos), adventureManager.checkPartyHalfHp(currentAdventure,max_hit_points));

                        // find character by name
                        Party p = adventureManager.getPartyMemberByName(currentAdventure, c.getName());

                        if (p != null) {

                        Character ch = p.getCharacter(new CharacterJsonDAO());

                        String attackType = "";
                        if (ch instanceof Adventurer || ch instanceof Warrior || ch instanceof Champion) {
                            attackType = "Physical";
                        } else if (ch instanceof Wizard) {
                            attackType = "Magical";
                        } else
                            attackType = "Psychical";

                        // fireball to all alive monsters
                        if ((adventureManager.currentAliveMonsters(combatants, currentAdventure, encounter_pos) > 3 && ch instanceof Wizard) && !adventureManager.checkHealingNeeded(currentAdventure, max_hit_points)) {
                            List<String> monsterNames = adventureManager.getMonsterNamesInEncounterUnfiltered(encounter_pos,currentAdventure);
                            consoleUI.showFireballAttack(actionValue,monsterNames,ch.getName());
                            adventureManager.applyDamageOnAllMonsters(actionValue, currentAdventure, encounter_pos,attackType);
                        }

                        // heal a character
                        else if (ch instanceof Cleric && adventureManager.checkHealingNeeded(currentAdventure, max_hit_points)) {
                           String target =  adventureManager.applyHealOnCharacter(actionValue, currentAdventure, max_hit_points);
                            consoleUI.showClericHeal(ch.getName(), actionValue,target);
                        }

                        // heal all party members
                        else if (ch instanceof Paladin && adventureManager.checkHealingNeeded(currentAdventure, max_hit_points)) {
                            adventureManager.applyHealOnParty(actionValue, currentAdventure);
                            String[] partyNames = characterManager.getPartyNames(parties_inx);
                            consoleUI.showPaladinHeal(ch.getName(),actionValue,partyNames);


                        } else {  // attacks to a random monster
                            String monster = adventureManager.applyDamageOnRandomMonsterInEncounter(actionValue * rollDiced, currentAdventure, encounter_pos, attackType);
                            consoleUI.showAttackAction(null,ch.getCharClass(),false,1, c.getName(), monster, rollDiced, actionValue * rollDiced, adventureManager.getDamageTypeOfAttack(c.getName(), currentAdventure));
                        }
                    }
                    }
                }
            }
            round++;
            consoleUI.showEndOfRound(round);
        }
        consoleUI.allEnemiesAreDefeated();
    }

    /**
     * controlling the short rest stage of the adventure execution
     * @param parties_inx the parties in an adventure
     * @param adventure_name the name of the adventure
     * @param xp_gain the xp from monsters in encounter
     */
    private void shortRestStageXp(int[] parties_inx, String adventure_name, int xp_gain) {
        consoleUI.printRestStageTitle();
        String[] party_names = characterManager.getPartyNames(parties_inx);
        List<Integer> lvl_increase = adventureManager.gainXp(adventure_name, xp_gain, parties_inx);
        consoleUI.showXpGaining(party_names, xp_gain, lvl_increase);
    }
}
