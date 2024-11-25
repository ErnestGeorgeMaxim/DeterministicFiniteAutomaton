import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Test Postfix Conversion
        String regex = "a(b|c)*";
        String postfix = PostFixConverter.infixToPostfix(regex);
        System.out.println("Infix: " + regex);
        System.out.println("Postfix: " + postfix);

        // Build Lambda NFA from simple regex (for demonstration purposes)
        LambdaNFA lambdaNFA = new LambdaNFA();

        State q0 = new State();
        State q1 = new State();
        State q2 = new State();

        lambdaNFA.addState(q0);
        lambdaNFA.addState(q1);
        lambdaNFA.addState(q2);

        lambdaNFA.setStartNode(q0);
        lambdaNFA.addFinalNode(q2);

        lambdaNFA.addTransition(q0, 'a', q1);
        lambdaNFA.addTransition(q1, 'b', q2);
        lambdaNFA.addLambdaTransition(q1, q2);

        // Convert Lambda NFA to DFA
        DeterministicFiniteAutomatum dfa = lambdaNFA.convertToDFA();

        // Print DFA transitions
        System.out.println("DFA Transitions:");
        dfa.printTransitions();

        // Verify DFA functionality (example input simulation included below)

        // Simulate DFA for example input
        String inputString = "ab";
        System.out.println("Input: " + inputString);

        State currentState = dfa.getInitialState();
        for (char c : inputString.toCharArray()) {
            if (!dfa.getAlphabet().contains(c) || dfa.getTransitions().get(currentState) == null || !dfa.getTransitions().get(currentState).containsKey(c)) {
                System.out.println("Rejected: Invalid transition at character '" + c + "'");
                return;
            }
            currentState = dfa.getTransitions().get(currentState).get(c);
        }

        if (dfa.getFinalState().contains(currentState)) {
            System.out.println("Accepted");
        } else {
            System.out.println("Rejected: Did not end in a final state");
        }
    }
}