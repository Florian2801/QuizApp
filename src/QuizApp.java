import java.util.*;
import java.io.*;
import java.util.concurrent.*;

public class QuizApp {

    static class Question {
        String text;
        String[] choices;
        int correct;

        Question(String text, String[] choices, int correct) {
            this.text = text;
            this.choices = choices;
            this.correct = correct;
        }
    }

    static List<Question> questions = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static int score = 0;

    public static void main(String[] args) {
        loadQuestions();

        clear();
        title();

        System.out.println("⏱ Tu as 20 secondes pour répondre à chaque question !");
        System.out.print("Ton nom : ");
        String player = scanner.nextLine();

        Collections.shuffle(questions);

        for (Question q : questions) {
            askQuestion(q);
        }

        endScreen(player);
    }

    static void loadQuestions() {
        questions.add(new Question(
                "Quelle est la capitale du Canada ?",
                new String[]{"Toronto", "Ottawa", "Montréal", "Vancouver"}, 2));

        questions.add(new Question(
                "Quel est le plus grand océan ?",
                new String[]{"Atlantique", "Indien", "Pacifique", "Arctique"}, 3));

        questions.add(new Question(
                "Quelle planète est la plus proche du Soleil ?",
                new String[]{"Vénus", "Mercure", "Mars", "Terre"}, 2));

        questions.add(new Question(
                "Combien de côtés a un hexagone ?",
                new String[]{"5", "6", "7", "8"}, 2));

        questions.add(new Question(
                "Quel animal est le plus rapide sur terre ?",
                new String[]{"Lion", "Guépard", "Antilope", "Autruche"}, 2));

        questions.add(new Question(
                "Quel pays a inventé la pizza ?",
                new String[]{"France", "USA", "Italie", "Espagne"}, 3));

        questions.add(new Question(
                "Quelle est la plus haute montagne du monde ?",
                new String[]{"K2", "Everest", "Mont Blanc", "Kilimandjaro"}, 2));

        questions.add(new Question(
                "Quelle est la monnaie du Japon ?",
                new String[]{"Yuan", "Won", "Yen", "Dollar"}, 3));

        questions.add(new Question(
                "Quel est le plus long fleuve du monde ?",
                new String[]{"Nil", "Amazone", "Yangtsé", "Mississippi"}, 2));

        questions.add(new Question(
                "Combien de joueurs dans une équipe de football sur le terrain ?",
                new String[]{"9", "10", "11", "12"}, 3));
    }

    static void askQuestion(Question q) {
        System.out.println("\n--------------------------------");
        System.out.println("📌 " + q.text);

        for (int i = 0; i < q.choices.length; i++) {
            System.out.println((i + 1) + ") " + q.choices[i]);
        }

        int answer = timedInput();

        if (answer == q.correct) {
            System.out.println(color("✅ Correct !", 32));
            score += 1;
        } else {
            System.out.println(color("❌ Faux !", 31) +
                    " Réponse: " + q.choices[q.correct - 1]);
        }

        System.out.println("Score: " + score);
    }

    static int timedInput() {
        ExecutorService ex = Executors.newSingleThreadExecutor();
        Future<Integer> future = ex.submit(() -> {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                return -1;
            }
        });

        try {
            return future.get(20, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            System.out.println(color("⏱ Temps écoulé !", 33));
            return -1;
        } catch (Exception e) {
            return -1;
        } finally {
            ex.shutdownNow();
        }
    }

    static void endScreen(String player) {
        clear();
        System.out.println("🏁 QUIZ TERMINÉ");
        System.out.println("Score final : " + score + "/10");

        saveScore(player, score);
        showLeaderboard();
    }

    static void saveScore(String name, int score) {
        try (PrintWriter out = new PrintWriter(new FileWriter("leaderboard.txt", true))) {
            out.println(name + ":" + score);
        } catch (IOException e) {
            System.out.println("Erreur sauvegarde score");
        }
    }

    static void showLeaderboard() {
        System.out.println("\n🏆 CLASSEMENT");

        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("leaderboard.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException ignored) {}

        lines.stream()
                .sorted((a, b) -> {
                    int sa = Integer.parseInt(a.split(":")[1]);
                    int sb = Integer.parseInt(b.split(":")[1]);
                    return sb - sa;
                })
                .limit(10)
                .forEach(System.out::println);
    }

    static void title() {
        System.out.println(color("""
        ===============================
            🚀 JAVA QUIZ CHALLENGE
        ===============================
        """, 36));
    }

    static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static String color(String text, int code) {
        return "\033[" + code + "m" + text + "\033[0m";
    }
}
