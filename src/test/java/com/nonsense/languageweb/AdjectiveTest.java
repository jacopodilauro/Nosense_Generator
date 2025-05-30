// AdjectiveTest.java
package com.nonsense.languageweb;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

class AdjectiveTest {

    private static final Path RESOURCES_DIR = Paths.get("resources");
    private static final Path ORIGINAL_ADJ_FILE = RESOURCES_DIR.resolve("adjectives.txt");
    private static final Path TEMP_ADJ_FILE = RESOURCES_DIR.resolve("adjectives-temp.txt");

    @BeforeEach
    void setUp() {
        try {
            if (!Files.exists(RESOURCES_DIR)) {
                Files.createDirectories(RESOURCES_DIR);
            }

            // Se esiste il file originale, lo salviamo in temporaneo
            if (Files.exists(ORIGINAL_ADJ_FILE)) {
                Files.copy(ORIGINAL_ADJ_FILE, TEMP_ADJ_FILE, StandardCopyOption.REPLACE_EXISTING);
            } else {
                // Se non esiste, creiamo direttamente il file temporaneo con contenuto di test
                Files.write(TEMP_ADJ_FILE, List.of("rosso", "grande", "veloce"));
            }

            // Sovrascriviamo il file originale per i test
            Files.copy(TEMP_ADJ_FILE, ORIGINAL_ADJ_FILE, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            fail("Errore nel setup: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        try {
            // Ripristiniamo il file originale da quello temporaneo
            if (Files.exists(TEMP_ADJ_FILE)) {
                Files.copy(TEMP_ADJ_FILE, ORIGINAL_ADJ_FILE, StandardCopyOption.REPLACE_EXISTING);
                Files.deleteIfExists(TEMP_ADJ_FILE);
            }
        } catch (IOException ignored) {}
    }

    @Test
    void testGetWordNotNullOrEmpty() throws IOException {
        Adjective adj = new Adjective();  // Usa il file adjectives.txt
        String word = adj.getWord();
        assertNotNull(word);
        assertFalse(word.isBlank());
    }

    @Test
    void testToStringEqualsWord() throws IOException {
        Adjective adj = new Adjective();
        assertEquals(adj.getWord(), adj.toString());
    }
}


