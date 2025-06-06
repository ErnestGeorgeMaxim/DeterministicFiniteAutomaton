import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String filePath = "src/main/java/Regex.txt";
        String regex = readRegexFromFile(filePath);

        try {

            DeterministicFiniteAutomatum dfa = PostFixConverter.convertToDFA(regex);
            System.out.println("Automatul finit determinist a fost generat cu succes!");

            boolean running = true;
            while (running) {
                System.out.println("\nMeniu:");
                System.out.println("1. Afișare expresie regulată");
                System.out.println("2. Afișare automat finit determinist (în consolă și în fișier)");
                System.out.println("3. Verificare cuvânt în automat");
                System.out.println("4. Ieșire");
                System.out.print("Alegerea dvs.: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.println("Expresia regulată este: " + regex);
                        break;

                    case 2:

                        System.out.println("\nAutomatul finit determinist:");
                        dfa.printAutomaton();

                        String outputFile = "dfa_output.txt";
                        saveAutomatonToFile(dfa, outputFile);
                        System.out.println("Automatul a fost salvat în fișierul: " + outputFile);
                        break;

                    case 3:
                        System.out.print("Introduceți un cuvânt pentru verificare: ");
                        String word = scanner.nextLine();
                        boolean accepted = dfa.checkWord(word);
                        System.out.println("Cuvântul \"" + word + "\" " +
                                (accepted ? "este acceptat de automat." : "nu este acceptat de automat."));
                        break;

                    case 4:
                        System.out.println("Va urez o zi!");
                        running = false;
                        break;

                    default:
                        System.out.println("Opțiune invalidă. Vă rugăm să încercați din nou.");
                }
            }
        } catch (Exception e) {
            System.err.println("Eroare la generarea automatului: " + e.getMessage());
        }
    }

    private static String readRegexFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.readLine().trim();
        } catch (IOException e) {
            System.err.println("Eroare la citirea fișierului: " + e.getMessage());
            return null;
        }
    }

    private static void saveAutomatonToFile(DeterministicFiniteAutomatum dfa, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("Automatul finit determinist:");
            writer.println(dfa);
        } catch (IOException e) {
            System.err.println("Eroare la salvarea automatului: " + e.getMessage());
        }
    }
}
