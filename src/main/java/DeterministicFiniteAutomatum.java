import lombok.Getter;

import java.util.*;

@Getter
public class DeterministicFiniteAutomatum {
    private Set<State> states;
    private Set<Character> alphabet;
    private Map<State,Map<Character,State>> transitions;
    private State initialState;
    private Set<State> finalState;

    public DeterministicFiniteAutomatum() {
        states = new HashSet<>();
        alphabet = new HashSet<>();
        transitions = new HashMap<>();
        finalState = new HashSet<>();
    }


    public void setInitialState(State state) {
        state.setStartNode(true);
        this.initialState = state;
        states.add(state);
    }

    public void addFinalState(State state) {
        state.setEndNode(true);
        finalState.add(state);
        states.add(state);
    }

    public void addTransition(State from, Character symbol,State to) {
        alphabet.add(symbol);
        states.add(from);
        states.add(to);

        transitions.computeIfAbsent(from, k -> new HashMap<>()).put(symbol, to);
    }

    public void printTransitions() {
        for(State from : transitions.keySet()) {
            for(Character symbol : transitions.get(from).keySet()) {
                State to = transitions.get(from).get(symbol);
                System.out.println(from + " --(" + symbol + ")-->" + to);
            }
        }
    }
}
