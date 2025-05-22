/**
 * The {@code Verb} class represents a randomly selected verb from a file.
 * <p>
 * Verbs are loaded from the file {@code verbs.txt}, where each line contains a single verb.
 * The list is loaded only once and shared across all instances for performance.
 * Each instance of {@code Verb} stores one randomly chosen verb from the list.
 */

package com.nonsense;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class Verb {

    /** Path to the file containing the list verbs. */
    private static final String FILE_PATH = "resources/verbs.txt";

    /** Random instance used to select a random verb. */
    private static final Random RANDOM = new Random();

    /** Static shared list of all verbs loaded from the file. */
    private static List<String> verbs;

    /** The selected word (verb) for this istance. */
    private final String word;

    /**
     * Constructs a {@code Verb} object by randomly selecting a verb
     * from a list loaded from a file.
     * <p>
     * The file is read only once and cached statically for efficiency.
     *
     * @throws IOException if the verb file cannot be read or accessed
     */ 
    public Verb() throws IOException {
        if (verbs == null) {
            verbs = Files.readAllLines(Paths.get(FILE_PATH));
        }
        this.word = verbs.get(RANDOM.nextInt(verbs.size()));
    }

    /**
     * Returns the verb selected for this instance.
     *
     * @return the selected verb as a {@code String}
     */
    public String getWord() {
        return word;
    }

    /**
     * Returns the string representation of this object,
     * which is the selected verb.
     *
     * @return the selected verb as a {@code String}
     */
    @Override
    public String toString() {
        return word;
    }
}
