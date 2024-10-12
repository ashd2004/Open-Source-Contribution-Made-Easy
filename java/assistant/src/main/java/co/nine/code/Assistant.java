package co.nine.code;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * Created By : Lalit The Coder of house Umbarkar, First of his name, Khalasar of UAC, King in the West.
 * Created On : 28 Sep 2019
 * Organisation: 9code
 */
public class Assistant {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Map<String, String> qAndAMap = new HashMap<>();

    public static void main(String[] args) {
        Optional<Map<String, String>> optionalQAndAs = loadQAndA();
        if (!optionalQAndAs.isPresent()) {
            System.err.println("Could not load questions and answers");
            return;
        }

        String line;
        sout("Hi.",
                "How may I help you?",
                "Ask me simple questions and I'll try to answer them. Just say 'bye' to quit.");
        while (!(line = scanner.nextLine()).toLowerCase().matches("(q|quit|end|close|bye)")) {

            final String userInput = line.trim().toLowerCase();

            String response = qAndAMap.entrySet().stream()
                    .filter(entry -> userInput.contains(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .orElse("I don't know the answer to that. Please ask me something easy.");

            sout(response);
        }
        sout("Ok bye");
    }

    private static void sout(String... messages) {
        for (String message : messages) {
            System.out.println(message);
        }
    }

    private static Optional<Map<String, String>> loadQAndA() {
        URL resourceURL = Assistant.class.getClassLoader().getResource("q_and_a.json");
        if (resourceURL == null) {
            System.err.println("Could not find the resource: q_and_a.json");
            return Optional.empty();
        }

        // Convert the URL to a Path using toURI
        Path filePath;
        try {
            filePath = Path.of(resourceURL.toURI());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            QAndA[] qAndAs = new Gson().fromJson(reader, QAndA[].class);
            for (QAndA qa : qAndAs) {
                qAndAMap.put(qa.question.toLowerCase(), qa.answer);
            }
            return Optional.of(qAndAMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static class QAndA {
        public String question;
        public String answer;
    }
}
