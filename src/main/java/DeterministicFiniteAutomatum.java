import lombok.Getter;

import java.util.*;

@Getter
public class DeterministicFiniteAutomatum {
    private Set<State> states;
    private Set<Character> alphabet;
    private Map<State, Map<Character, State>> transitions;
    private State initialState;
    private Set<State> finalStates;

    public DeterministicFiniteAutomatum() {
        states = new HashSet<>();
        alphabet = new HashSet<>();
        transitions = new HashMap<>();
        finalStates = new HashSet<>();
    }

    public void setInitialState(State state) {
        state.setStartNode(true);
        this.initialState = state;
        states.add(state);
    }

    public void addFinalState(State state) {
        state.setEndNode(true);
        finalStates.add(state);
        states.add(state);
    }

    public void addTransition(State from, Character symbol, State to) {
        alphabet.add(symbol);
        states.add(from);
        states.add(to);
        transitions.computeIfAbsent(from, k -> new HashMap<>()).put(symbol, to);
    }

    public boolean verifyAutomaton() {
        if (initialState == null || finalStates.isEmpty()) {
            System.err.println("Automatul nu are o stare inițială sau stări finale.");
            return false;
        }
        for (State state : states) {
            Map<Character, State> stateTransitions = transitions.get(state);
            if (stateTransitions == null) {
                System.err.println("Lipsesc tranziții pentru starea " + state);
                return false;
            }
            for (Character symbol : alphabet) {
                if (!stateTransitions.containsKey(symbol)) {
                    System.err.println("Lipsesc tranziții pentru starea " + state + " și simbolul " + symbol);
                    return false;
                }
            }
        }
        return true;
    }


    public void printAutomaton() {
        System.out.println("Automatul finit determinist:");
        System.out.println("Stare inițială: " + initialState);
        System.out.println("Stări finale: " + finalStates);
        for (State from : transitions.keySet()) {
            for (Character symbol : transitions.get(from).keySet()) {
                State to = transitions.get(from).get(symbol);
                System.out.println(from + " --(" + symbol + ")--> " + to);
            }
        }
    }

    public boolean checkWord(String word) {
        State currentState = initialState;
        for (char c : word.toCharArray()) {
            Map<Character, State> stateTransitions = transitions.get(currentState);
            if (stateTransitions == null || !stateTransitions.containsKey(c)) {
                return false;
            }
            currentState = stateTransitions.get(c);
        }
        return finalStates.contains(currentState);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Stare inițială: ").append(initialState).append("\n");
        sb.append("Stări finale: ").append(finalStates).append("\n");
        sb.append("Tranziții:\n");
        for (State from : transitions.keySet()) {
            for (Character symbol : transitions.get(from).keySet()) {
                State to = transitions.get(from).get(symbol);
                sb.append(from).append(" --(").append(symbol).append(")--> ").append(to).append("\n");
            }
        }
        return sb.toString();
    }
}
