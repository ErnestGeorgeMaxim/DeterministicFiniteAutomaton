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

        for (int i = 0; i < regex.length(); i++) {
            char c = regex.charAt(i);

            if (Character.isLetterOrDigit(c)) {
                output.append(c);
            } else if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    output.append(operators.pop());
                }
                if (!operators.isEmpty()) {
                    operators.pop(); // Remove '('

                    // Check if there's a * after the closing parenthesis
                    if (i + 1 < regex.length() && regex.charAt(i + 1) == '*') {
                        output.append('*');
                        i++; // Skip the * in the next iteration
                    }
                }
            } else {
                while (!operators.isEmpty() && operators.peek() != '(' &&
                        precedence(operators.peek()) >= precedence(c)) {
                    output.append(operators.pop());
                }
                operators.push(c);
            }
        }

        while (!operators.isEmpty()) {
            if (operators.peek() != '(') {
                output.append(operators.pop());
            } else {
                operators.pop();
            }
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

                // Add concatenation operator if:
                // 1. Current is a letter/digit and next is a letter/digit
                // 2. Current is a letter/digit and next is an opening parenthesis
                // 3. Current is a closing parenthesis and next is a letter/digit
                // 4. Current is a * and next is a letter/digit or opening parenthesis
                // 5. Current is a closing parenthesis and next is an opening parenthesis
                if ((Character.isLetterOrDigit(current) && (Character.isLetterOrDigit(next) || next == '(')) ||
                        (current == ')' && (Character.isLetterOrDigit(next) || next == '(')) ||
                        (current == '*' && (Character.isLetterOrDigit(next) || next == '('))) {
                    result.append('.');
                }
            }
        }

        return result.toString();
    }

    public static DeterministicFiniteAutomatum convertToDFA(String regex) {
        LambdaNFA nfa = RegexToNFAConverter.convertToNFA(regex);
        return nfa.convertToDFA();
    }
}