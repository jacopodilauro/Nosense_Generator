package com.nonsense;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class Noun {
    private static final String FILE_PATH = "resources/nouns.txt";
    private static final Random RANDOM = new Random();
    private static List<String> nouns;

    private final String word;

    // Costruttore
    public Noun() throws IOException {
        if (nouns == null) {
            nouns = Files.readAllLines(Paths.get(FILE_PATH));
        }
        this.word = nouns.get(RANDOM.nextInt(nouns.size()));
    }

    public String getWord() {
        return word;
    }

    @Override
    public String toString() {
        return word;
    }
}
