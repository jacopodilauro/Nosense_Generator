package com.nonsense.languageweb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

/**
 * The {@code Adjective} class represents a randomly selected adjective from a file.
 * <p>
 * Adjectives are loaded from the file {@code adjective.txt}, where each line contains a single adjective.
 * The list is loaded only once and shared across all instances for performance.
 * Each instance of {@code Adjective} stores one randomly chosen adjective from the list.
 */
public class Adjective {
   /** Path to the file containing the list of adjectives. */
    private static final String FILE_PATH = "resources/adjectives.txt";

    /** Random number generator to select a random adjective */
    private static final Random RANDOM = new Random();

    /** Static shared list of all adjectives loaded from the file */
    private static List<String> adjectives;

    /** The selected word (adjective) for this instance. */
    private final String word;

    /**
     * Constructs a {@code Adjective} object by randomly selecting a adjective
     * from a list loaded from a file.
     * <p>
     * The file is read only once and cached statically for efficiency.
     *
     * @throws IOException if the adjective file cannot be read or accessed
     */      
    public Adjective() throws IOException {
        if (adjectives == null) {
            adjectives = Files.readAllLines(Paths.get(FILE_PATH));
        }
        this.word = adjectives.get(RANDOM.nextInt(adjectives.size()));
    }

    /**
     * Returns the adjective selected for this instance.
     *
     * @return the selected adjective as a {@code String}
     */
    public String getWord() {
        return word;
    }

    /**
     * Returns the string representation of this object,
     * which is the selected adjective.
     *
     * @return the selected adjective as a {@code String}
     */
    @Override
    public String toString() {
        return word;
    }
}
