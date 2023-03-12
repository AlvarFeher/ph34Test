package business;

import business.entities.Combatant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * manages Combatant related features logic
 * @author Youssef Bat, Alvaro Feher
 */

public class CombatantManager {

    private final CharacterManager characterManager;
    private final MonsterManager monsterManager;
    private boolean local;


    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }


    /**
     * constructor
     */
    public CombatantManager() {
        monsterManager = new MonsterManager();
        characterManager = new CharacterManager();
    }

    /**
     * rolling initiative, and sorting all the combatants by their initial value
     * @param partyNames names of the characters
     * @param monsterNamesInEncounterUnfiltered monster names
     * @return a sorted list of combatants
     */
    public List<Combatant> rollInitiative(String[] partyNames, List<String> monsterNamesInEncounterUnfiltered) {
        monsterManager.setIsLocal(isLocal());
        characterManager.setLocal(isLocal());
        List<Integer> party_initiative_value = characterManager.getInitValueByNames(partyNames);
        List<Integer> monster_initiative_value = monsterManager.getInitValueByNames(monsterNamesInEncounterUnfiltered);
        List<Combatant> combatents = new ArrayList<>();
        for (int i=0;i< partyNames.length;i++) {
            combatents.add(new Combatant(partyNames[i], party_initiative_value.get(i)));
        }
        for (int i=0;i< monsterNamesInEncounterUnfiltered.size();i++) {
            combatents.add(new Combatant(monsterNamesInEncounterUnfiltered.get(i), monster_initiative_value.get(i)));
        }
        //sorting the list based on the initiative value
        combatents.sort(Comparator.comparingInt(Combatant::getInitValue));
        Collections.reverse(combatents);
        return combatents;

    }

    /**
     * initial value getter
     * @param combatants list of combatants
     * @return a list of initial value
     */
    public List<Integer> getInitValues(List<Combatant> combatants) {
        monsterManager.setIsLocal(isLocal());
        characterManager.setLocal(isLocal());
        List<Integer> list = new ArrayList<>();
        for (Combatant combatant : combatants) {
            list.add(combatant.getInitValue());
        }
       return list;
    }

    /**
     * names getter
     * @param combatants list of combatants
     * @return a list of names
     */
    public List<String> getNames(List<Combatant> combatants) {
        monsterManager.setIsLocal(isLocal());
        characterManager.setLocal(isLocal());
        List<String> list = new ArrayList<>();
        for (Combatant combatant : combatants) {
            list.add(combatant.getName());
        }
        return list;
    }

}
