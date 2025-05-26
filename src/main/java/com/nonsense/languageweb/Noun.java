/**
 * The {@code Noun} class represents a randomly selected noun from a file.
 * <p>
 * Nouns are loaded from the file {@code nouns.txt}, where each line contains a single noun.
 * The list is loaded only once and shared across all instances for performance.
 * Each instance of {@code Noun} stores one randomly chosen noun from the list.
 */

package com.nonsense.languageweb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class Noun {

    /** Path to the file containing the list of nouns. */
    private static final String FILE_PATH = "resources/nouns.txt";

    /** Random istance used to select a random noun. */
    private static final Random RANDOM = new Random();

    /** Static shared list of nouns loaded from the file. */
    private static List<String> nouns;

    /** The selected word (noun) for this istance. */
    private final String word;

    /**
     * Constructs a {@code Noun} object by randomly selecting a noun
     * from a list loaded from a file.
     * <p>
     * The file is read only once and cached statically for efficiency.
     *
     * @throws IOException if the noun file cannot be read or accessed
     */   
    public Noun() throws IOException {
        if (nouns == null) {
            nouns = Files.readAllLines(Paths.get(FILE_PATH));
        }
        this.word = nouns.get(RANDOM.nextInt(nouns.size()));
    }

    /**
     * Returns the noun selected for this instance.
     *
     * @return the selected noun as a {@code String}
     */
    public String getWord() {
        return word;
    }

    /**
     * Returns the string representation of this object,
     * which is the selected noun.
     *
     * @return the selected noun as a {@code String}
     */
    @Override
    public String toString() {
        return word;
    }
}
