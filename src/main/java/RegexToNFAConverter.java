import java.util.*;

public class RegexToNFAConverter {
    public static LambdaNFA convertToNFA(String regex) {
        // Pas 1: Convertim expresia regulată din infix în postfix (folosind PostFixConverter)
        String postfixRegex = PostFixConverter.infixToPostfix(regex);
        System.out.println("Expresie postfixată: " + postfixRegex);

        // Pas 2: Construim Lambda-NFA-ul folosind stiva
        Stack<LambdaNFA> stack = new Stack<>();

        for (char c : postfixRegex.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                // Dacă simbolul este un operand, creăm un NFA pentru el
                stack.push(createBasicNFA(c));
            } else if (c == '*') {
                // Operatori: *, care înseamnă "Kleene Star"
                LambdaNFA nfa = stack.pop();
                stack.push(applyKleeneStar(nfa));
            } else if (c == '|') {
                // Operatori: |, care înseamnă "sau"
                LambdaNFA nfa2 = stack.pop();
                LambdaNFA nfa1 = stack.pop();
                stack.push(applyUnion(nfa1, nfa2));
            } else if (c == '.') {
                // Operatori: ., care înseamnă "concatenare"
                LambdaNFA nfa2 = stack.pop();
                LambdaNFA nfa1 = stack.pop();
                stack.push(applyConcatenation(nfa1, nfa2));
            } else {
                throw new IllegalArgumentException("Operator necunoscut: " + c);
            }
        }

        // Rezultatul final din stivă este NFA-ul complet
        if (stack.size() != 1) {
            throw new IllegalStateException("Expresia regulată nu este validă.");
        }

        return stack.pop();
    }

    private static LambdaNFA createBasicNFA(char symbol) {
        // Creează un NFA de bază pentru un singur simbol
        LambdaNFA nfa = new LambdaNFA();
        State start = new State();
        State end = new State();
        nfa.addState(start);
        nfa.addState(end);
        nfa.setInitialState(start);
        nfa.addFinalState(end);

        nfa.addTransition(start, symbol, end);
        return nfa;
    }


private static LambdaNFA applyKleeneStar(LambdaNFA nfa) {
    LambdaNFA result = new LambdaNFA();
    State start = new State();
    State end = new State();

    result.addState(start);
    result.addState(end);
    result.setInitialState(start);
    result.addFinalState(end);

    // Copy all states and transitions from original NFA
    copyStatesAndTransitions(nfa, result);

    // Add lambda transitions for the Kleene star operation
    result.addLambdaTransition(start, nfa.getInitialState());  // Enter the loop
    result.addLambdaTransition(start, end);  // Skip the loop entirely

    // Add transitions from final states
    for (State finalState : nfa.getFinalStates()) {
        result.addLambdaTransition(finalState, nfa.getInitialState());  // Repeat
        result.addLambdaTransition(finalState, end);  // Exit loop
    }

    return result;
}
    private static LambdaNFA applyUnion(LambdaNFA nfa1, LambdaNFA nfa2) {
        // Creează un NFA pentru operatorul | (sau)
        LambdaNFA result = new LambdaNFA();
        State newStart = new State();
        State newEnd = new State();

        result.addState(newStart);
        result.addState(newEnd);
        result.setInitialState(newStart);
        result.addFinalState(newEnd);

        // Adăugăm tranziții lambda
        result.addLambdaTransition(newStart, nfa1.getInitialState());
        result.addLambdaTransition(newStart, nfa2.getInitialState());
        for (State finalState : nfa1.getFinalStates()) {
            result.addLambdaTransition(finalState, newEnd);
        }
        for (State finalState : nfa2.getFinalStates()) {
            result.addLambdaTransition(finalState, newEnd);
        }

        // Copiem toate stările și tranzițiile din NFA-urile originale
        copyStatesAndTransitions(nfa1, result);
        copyStatesAndTransitions(nfa2, result);

        return result;
    }

    private static LambdaNFA applyConcatenation(LambdaNFA nfa1, LambdaNFA nfa2) {
        LambdaNFA result = new LambdaNFA();
        copyStatesAndTransitions(nfa1, result);

        // Adăugăm tranziții lambda de la stările finale ale primului NFA
        // la starea inițială a celui de-al doilea NFA
        for (State finalState : nfa1.getFinalStates()) {
            result.addLambdaTransition(finalState, nfa2.getInitialState());
        }

        copyStatesAndTransitions(nfa2, result);

        // Starea inițială a rezultatului este cea a primului NFA
        result.setInitialState(nfa1.getInitialState());

        // Stările finale ale rezultatului sunt cele din al doilea NFA
        for (State finalState : nfa2.getFinalStates()) {
            result.addFinalState(finalState);
        }

        return result;
    }

    private static void copyStatesAndTransitions(LambdaNFA from, LambdaNFA to) {
        for (State state : from.getStates()) {
            to.addState(state);
        }
        for (Map.Entry<State, Map<Character, Set<State>>> entry : from.getTransitions().entrySet()) {
            State fromState = entry.getKey();
            for (Map.Entry<Character, Set<State>> transition : entry.getValue().entrySet()) {
                char symbol = transition.getKey();
                for (State toState : transition.getValue()) {
                    to.addTransition(fromState, symbol, toState);
                }
            }
        }
        for (Map.Entry<State, Set<State>> entry : from.getLambdaTransitions().entrySet()) {
            State fromState = entry.getKey();
            for (State toState : entry.getValue()) {
                to.addLambdaTransition(fromState, toState);
            }
        }
    }
}
