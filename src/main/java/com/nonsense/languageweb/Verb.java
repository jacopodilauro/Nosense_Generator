package com.nonsense.languageweb;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class Verb {
    private static final String FILE_PATH = "resources/verbs.txt";
    private static final Random RANDOM = new Random();
    private static List<String> verbs;

    private final String word;

    // Costruttore
    public Verb() throws IOException {
        if (verbs == null) {
            verbs = Files.readAllLines(Paths.get(FILE_PATH));
        }
        this.word = verbs.get(RANDOM.nextInt(verbs.size()));
    }

    public String getWord() {
        return word;
    }

    @Override
    public String toString() {
        return word;
    }
}
