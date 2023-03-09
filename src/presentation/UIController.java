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
    private final CombatantManager combatantManager;
    private final AdventureManager adventureManager;

    private boolean isLocal = true; // temporary variable for storage management
    /**
     * default constructor
     */
    public UIController() {
        consoleUI = new ConsoleUIManager();
        monsterManager = new MonsterManager();
        characterManager = new CharacterManager();
        combatantManager = new CombatantManager();
        adventureManager = new AdventureManager();
    }

    /**
     * shows the logo as the program starts, and runs the rest of the program
     */
    public void startProgram() throws IOException {
        consoleUI.showLogo();

        // todo: add persistence.API data option
        switch (consoleUI.showStorageMenuOptions()){
            case CLOUD -> isLocal = false; // this is probably a bad practice
            case LOCAL -> isLocal = true;
        }
        consoleUI.loadData();

        if (monsterManager.loadMonsters() == null) {
            consoleUI.showLoadingError(false);
        }
        else {
            consoleUI.showLoadingError(true);
            run();
        }
    }

    /**
     * controls the main menu options
     */
    private void run() {
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
            int characterCount = characterManager.getCharacterCount();
            int characterNum = consoleUI.chooseNumOfCharactersAdventure(currentAdventure, characterCount);

            adventureManager.resetParty(currentAdventure);

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

            // FIXME: check down from here *********************

            for (int i = 0; i < adventureManager.getNumOfEncountersByName(currentAdventure); i++) {
                consoleUI.showEncounterMsg(i);
                List<String> monsters = adventureManager.getMonsterNamesInEncounter(i, currentAdventure);
                for (String monster : monsters) {
                    consoleUI.showMonsterInEncounter(monster, adventureManager.occurrenceMonsterInEncounter(i, currentAdventure, monster));
                }

                //preparation_stage

                consoleUI.printPreparationStageTitle();
                consoleUI.printSelfMotivation(characterManager.getPartyNames(parties_inx));

                //adding attribute bonus to party members from preparation stage actions
                adventureManager.updatePartyInPrepStage(currentAdventure, parties_inx);

                List<Combatant> combatants = combatantManager.rollInitiative(characterManager.getPartyNames(parties_inx), adventureManager.getMonsterNamesInEncounterUnfiltered(i, currentAdventure));
                consoleUI.showRollingInitiative(combatantManager.getNames(combatants), combatantManager.getInitValues(combatants));

                //combat stage
                combatStage(i, parties_inx, currentAdventure, max_hit_points, combatants);

                if (adventureManager.isTPU(currentAdventure, i)) {
                    consoleUI.showTPU();
                    run();
                }
                //short rest stage
                shortRestStage(parties_inx, currentAdventure, i);
            }


            consoleUI.showPartyCompleteAdventure(currentAdventure);
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
                        String party = adventureManager.applyDamageOnRandomConsciousParty(actionValue * rollDiced, currentAdventure, parties_inx, damageType);
                        if (party == null) {
                            return;
                        }
                        consoleUI.showAttackAction(0, c.getName(), party, rollDiced, actionValue * rollDiced,"physical");
                    }
                }
                else {
                    if (adventureManager.isPartyAlive(currentAdventure, c.getName())) {

                        // we are considering both healing and damage value as the same
                        actionValue = adventureManager.takeAttackActionCharacter(currentAdventure, c.getName(), adventureManager.currentAliveMonsters(combatants,currentAdventure,encounter_pos),adventureManager.checkPartyHalfHp(currentAdventure));

                        // find character by name
                        Party p = adventureManager.getPartyMemberByName(currentAdventure,c.getName());
                        Character ch = p.getCharacter(new CharacterJsonDAO());

                        // fireball to all alive monsters
                        if((adventureManager.currentAliveMonsters(combatants,currentAdventure,encounter_pos) > 3 && ch instanceof Wizard) && !adventureManager.checkHealingNeeded(currentAdventure,max_hit_points)){
                            adventureManager.applyDamageOnAllMonsters(actionValue,currentAdventure,encounter_pos);
                            System.out.println("FIREBALL");
                        }

                        // heal a character
                        else if (ch instanceof Cleric && adventureManager.checkHealingNeeded(currentAdventure, max_hit_points)) {
                             adventureManager.applyHealOnCharacter(actionValue,currentAdventure,max_hit_points);
                            System.out.println("cleric heals");
                        }

                        // heal all party members
                        else if (ch instanceof Paladin && adventureManager.checkHealingNeeded(currentAdventure,max_hit_points)) {
                            adventureManager.applyHealOnParty(actionValue,currentAdventure);
                            System.out.println("paladin heals");
                        }

                        else {  // attacks to a random monster
                            String monster = adventureManager.applyDamageOnRandomMonsterInEncounter(actionValue * rollDiced, currentAdventure, encounter_pos);
                            consoleUI.showAttackAction(1, c.getName(), monster, rollDiced, actionValue * rollDiced, adventureManager.getDamageTypeOfAttack(c.getName(),currentAdventure));
                        }
                        adventureManager.testPrint(currentAdventure,encounter_pos);
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
     * @param encounter_pos the encounter position
     */
    private void shortRestStage(int[] parties_inx, String adventure_name, int encounter_pos) {
        consoleUI.printRestStageTitle();
        String[] party_names = characterManager.getPartyNames(parties_inx);
        int xp_gained = adventureManager.getXpGainedInEncounter(adventure_name, encounter_pos);
        List<Integer> lvl_increase = adventureManager.gainXp(adventure_name, 22, parties_inx);
        consoleUI.showXpGaining(party_names, 22, lvl_increase);
        int[] bandage_time = adventureManager.takeHealingActionCharacter(adventure_name, party_names);
        List<Integer> is_healed =  adventureManager.healParty(adventure_name, bandage_time, parties_inx);
        consoleUI.showHealingTime(party_names, bandage_time, is_healed);
    }


}
