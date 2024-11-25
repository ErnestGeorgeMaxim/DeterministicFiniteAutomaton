import java.util.*;

public class PostFixConverter {
    private static int precedence(char op) {
        switch (op) {
            case '*': return 3;
            case '.': return 2;
            case '|': return 1;
            default: return 0;
        }
    }

    public static String infixToPostfix(String regex) {
        StringBuilder output = new StringBuilder();
        Stack<Character> operators = new Stack<>();
        regex = addImplicitConcatenation(regex);

        for (char c : regex.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                output.append(c);
            } else if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    output.append(operators.pop());
                }
                operators.pop();
            } else if (".|*".indexOf(c) != -1) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                    output.append(operators.pop());
                }
                operators.push(c);
            }
        }

        while (!operators.isEmpty()) {
            output.append(operators.pop());
        }

        return output.toString();
    }

    private static String addImplicitConcatenation(String regex) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < regex.length(); i++) {
            char current = regex.charAt(i);
            result.append(current);

            if (i < regex.length() - 1) {
                char next = regex.charAt(i + 1);
                if ((Character.isLetterOrDigit(current) && Character.isLetterOrDigit(next)) ||
                        (current == ')' && Character.isLetterOrDigit(next)) ||
                        (current == '*' && Character.isLetterOrDigit(next)) ||
                        (current == ')' && next == '(') ||
                        (current == '*' && next == '(')) {
                    result.append('.');
                }
            }
        }
        return result.toString();
    }

    public static DeterministicFiniteAutomatum convertToDFA(String regex) {
        LambdaNFA nfa = RegexToNFAConverter.convertToNFA(regex); // Vei implementa RegexToNFAConverter
        return nfa.convertToDFA();
    }
}
