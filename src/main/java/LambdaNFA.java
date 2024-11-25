import java.util.*;
public class LambdaNFA {
    private Set<State> states;
    private Set<Character>alphabet;
    private Map<State,Map<Character,Set<State>>>transitions;
    private Map<State,Set<State>>lambdaTransitions;
    private State initialState;
    private Set<State>finalStates;

    public LambdaNFA() {
        states = new HashSet<>();
        alphabet = new HashSet<>();
        transitions = new HashMap<>();
        lambdaTransitions = new HashMap<>();
        finalStates = new HashSet<>();
    }

    public void addState(State state) {
        states.add(state);
    }

    public void setStartNode(State state) {
        state.setStartNode(true);
        this.initialState = state;
    }

    public void addFinalNode(State state) {
        state.setEndNode(true);
        finalStates.add(state);
    }

    public void addTransition(State from,Character symbol, State to) {
        if(symbol!=null)alphabet.add(symbol);
        transitions.computeIfAbsent(from, k -> new HashMap<>()).computeIfAbsent(symbol, k -> new HashSet<>()).add(to);

    }

    public void addLambdaTransition(State from, State to) {
        lambdaTransitions.computeIfAbsent(from, k -> new HashSet<>()).add(to);
    }

    private Set<State>lambdaClosure(Set<State> states) {
        Set<State> closure = new HashSet<>(states);
        Deque<State> stack = new ArrayDeque<>(states);
        while (!stack.isEmpty()) {
            State current = stack.pop();
            Set<State>lambdaStates = lambdaTransitions.getOrDefault(current, Collections.emptySet());
            for(State next: lambdaStates) {
                if(!closure.contains(next)) {
                    closure.add(next);
                    stack.push(next);
                }
            }
        }
        return closure;
    }

    public DeterministicFiniteAutomatum convertToDFA() {
        DeterministicFiniteAutomatum dfa = new DeterministicFiniteAutomatum();
        Map<Set<State>, State> dfaStateMap = new HashMap<>();
        Set<State> initialNFAStates = lambdaClosure(Collections.singleton(initialState));
        State dfaInitialState = new State();
        dfa.setInitialState(dfaInitialState);
        dfaStateMap.put(initialNFAStates, dfaInitialState);

        if (initialNFAStates.stream().anyMatch(State::isEndNode)) {
            dfa.addFinalState(dfaInitialState);
        }

        Queue<Set<State>> unmarkedStates = new LinkedList<>();
        unmarkedStates.add(initialNFAStates);

        while (!unmarkedStates.isEmpty()) {
            Set<State> currentNFAStates = unmarkedStates.poll();
            State currentDFAState = dfaStateMap.get(currentNFAStates);

            for (Character symbol : alphabet) {
                Set<State> nextNFAStates = new HashSet<>();
                for (State nfaState : currentNFAStates) {
                    Set<State> transitions = this.transitions
                            .getOrDefault(nfaState, Collections.emptyMap())
                            .getOrDefault(symbol, Collections.emptySet());

                    for (State transitionState : transitions) {
                        nextNFAStates.addAll(lambdaClosure(Collections.singleton(transitionState)));
                    }
                }

                if (nextNFAStates.isEmpty()) continue;

                State nextDFAState = dfaStateMap.get(nextNFAStates);
                if (nextDFAState == null) {
                    nextDFAState = new State();
                    dfaStateMap.put(nextNFAStates, nextDFAState);
                    unmarkedStates.add(nextNFAStates);

                    if (nextNFAStates.stream().anyMatch(State::isEndNode)) {
                        dfa.addFinalState(nextDFAState);
                    }
                }

                dfa.addTransition(currentDFAState, symbol, nextDFAState);
            }
        }

        return dfa;
    }

}
