package com.nonsense.languageweb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class Adjective {
    private static final String FILE_PATH = "resources/adjectives.txt";
    private static final Random RANDOM = new Random();
    private static List<String> adjectives;

    private final String word;

    // Costruttore
    public Adjective() throws IOException {
        if (adjectives == null) {
            adjectives = Files.readAllLines(Paths.get(FILE_PATH));
        }
        this.word = adjectives.get(RANDOM.nextInt(adjectives.size()));
    }

    public String getWord() {
        return word;
    }

    @Override
    public String toString() {
        return word;
    }
}
