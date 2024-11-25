import java.util.*;
public class LambdaNFA {
    private Set<State> states;
    private Set<Character>alphabet;
    private Map<State,Map<Character,Set<State>>>transtions;
    private Map<State,Set<State>>lambdaTransitions;
    private State initialState;
    private Set<State>finalStates;

    public LambdaNFA() {
        states = new HashSet<>();
        alphabet = new HashSet<>();
        transtions = new HashMap<>();
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
        transtions.computeIfAbsent(from, k -> new HashMap<>()).computeIfAbsent(symbol, k -> new HashSet<>()).add(to);

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

    public DeterministicFiniteAutomatum convertToDFA()
    {
        DeterministicFiniteAutomatum dfa = new DeterministicFiniteAutomatum();
        Map<Set<State>,State>DFAStateMap= new HashMap<>();
        Set<State>initialNFAStates=lambdaClosure(Collections.singleton(initialState));
        State DFAinitialState=new State();
        dfa.setInitialState(dfaInitialState);
        dfaStateMap.put(initialNfaStates, dfaInitialState);

        if (initialNfaStates.stream().anyMatch(State::isFinal)) {
            dfa.addFinalState(dfaInitialState);
        }

        Queue<Set<State>> unmarkedStates = new LinkedList<>();
        unmarkedStates.add(initialNfaStates);

        while (!unmarkedStates.isEmpty()) {
            Set<State> currentNfaStates = unmarkedStates.poll();
            State currentDfaState = dfaStateMap.get(currentNfaStates);


            for (Character symbol : alphabet) {
                Set<State> nextNfaStates = new HashSet<>();
                for (State nfaState : currentNfaStates) {
                    Set<State> transitions = this.transitions
                            .getOrDefault(nfaState, Collections.emptyMap())
                            .getOrDefault(symbol, Collections.emptySet());

                    for (State transitionState : transitions) {
                        nextNfaStates.addAll(lambdaClosure(Collections.singleton(transitionState)));
                    }
                }

                if (nextNfaStates.isEmpty()) continue;

                State nextDfaState = dfaStateMap.get(nextNfaStates);
                if (nextDfaState == null) {
                    nextDfaState = new State();
                    dfaStateMap.put(nextNfaStates, nextDfaState);
                    unmarkedStates.add(nextNfaStates);

                    if (nextNfaStates.stream().anyMatch(State::isFinal)) {
                        dfa.addFinalState(nextDfaState);
                    }
                }

                dfa.addTransition(currentDfaState, symbol, nextDfaState);
            }
        }

        return dfa;
    }




}
