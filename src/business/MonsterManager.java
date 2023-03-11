package business;

import business.entities.Monster;
import persistence.API.MonsterApiDAO;
import persistence.JSON.MonstersJsonDAO;
import persistence.MonsterDAO;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * manages monster related features logic
 * @author Youssef Bat, Alvaro Feher
 */


// TODO: if boss, damage affects to all characters,
//   if they receive damage of the same type as theirs, resultant damage is halved

public class MonsterManager {

    private final MonsterDAO monsterJsonDAO;
    private final MonsterDAO monsterApiDAO;
    private boolean is_local;

    public boolean isIt_local() {
        return is_local;
    }

    public void setIs_local(boolean is_local) {
        this.is_local = is_local;
    }

    /**
     * constructor
     */
    public MonsterManager() {
        monsterJsonDAO = new MonstersJsonDAO();
        monsterApiDAO = new MonsterApiDAO();
    }

    /**
     * get all the monsters in the data set
     * @return a list of monsters
     */
    public List<Monster> loadMonsters() {
        if (isIt_local()) {
            return monsterJsonDAO.getAll();
        }else {
            return monsterApiDAO.getAll();
        }
    }

    /**
     * get a monster by a given name
     * @param monsterName monster name
     * @return a monster object whose name is the given name
     */
    public Monster findByName(String monsterName){
        Monster monster = null;
        for (Monster m: loadMonsters()){
            if (Objects.equals(m.getName(), monsterName)){
                monster =  m;
            }
        }
        return monster;
    }

    /**
     * get a list of all monsters in the encounter
     * @param amount amount of monster in the encounter
     * @param monsterName monster name
     * @return a list of all monsters in the encounter
     */
    public List<Monster> listMonstersOfEncounter(int amount, String monsterName){
        List<Monster> monsters = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Monster m = findByName(monsterName); 
            monsters.add(m); 
        }
        return monsters;
    }

    /**
     * checks if a monster is a boss
     * @param currentMonsterName monster name
     * @return true if monster is boss, false otherwise
     */
    public boolean isMonsterBoss(String currentMonsterName) {
        List<Monster> monsters = monsterJsonDAO.getAll();
        for (Monster monster: monsters) {
            if (Objects.equals(monster.getName(), currentMonsterName)) {
                if (Objects.equals(monster.getChallenge(), "Boss")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * get all the monsters names from the data set
     * @return a list of all the monsters name
     */
    public List<String> getAllMonstersName() {
        List<String> names = new ArrayList<>();
        for (int i=0;i<monsterJsonDAO.getAll().size();i++) {
            names.add(monsterJsonDAO.getAll().get(i).getName());
        }
        return names;
    }

    /**
     * get all the monsters challenges from the data set
     * @return a list of all the monsters challenges
     */
    public List<String> getAllMonstersChallenges() {
        List<String> challenges = new ArrayList<>();
        for (int i=0;i<monsterJsonDAO.getAll().size();i++) {
            challenges.add(monsterJsonDAO.getAll().get(i).getChallenge());
        }
        return challenges;
    }

    /**
     * get the initiative values of some monsters
     * @param monsterNamesInEncounterUnfiltered monster names
     * @return list of the initiative values of the monsters
     */
    public List<Integer> getInitValueByNames(List<String> monsterNamesInEncounterUnfiltered) {
        List<Integer> init_values = new ArrayList<>();
        for (String partyName : monsterNamesInEncounterUnfiltered) {
            init_values.add((monsterJsonDAO.getInitValueByName(partyName)) + (int) (Math.random() * 12) + 1);
        }
        return init_values;
    }

    public String getDamageTypeOfMonster(String name){
        List<Monster> monsters = monsterJsonDAO.getAll();
        for(Monster m: monsters){
            if(Objects.equals(m.getName(), name)){
                return m.getDamageType();
            }
        }
        return "";
    }
}
