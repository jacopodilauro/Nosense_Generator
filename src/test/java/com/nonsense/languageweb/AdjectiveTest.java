// AdjectiveTest.java
package com.nonsense.languageweb;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

class AdjectiveTest {

    @BeforeAll
    static void initAll() {
        // Prepara un file di risorse di test con aggettivi
        try {
            Path dir = Paths.get("resources");
            if (!Files.exists(dir)) Files.createDirectories(dir);
            Files.write(Paths.get("resources/adjectives.txt"), List.of("rosso", "grande", "veloce"));
        } catch (IOException e) {
            fail("Setup fallito: " + e.getMessage());
        }
    }

    @AfterAll
    static void tearDownAll() {
        try {
            Files.deleteIfExists(Paths.get("resources/adjectives.txt"));
        } catch (IOException ignored) {}
    }

    @Test
    void testGetWordNotNullOrEmpty() throws IOException {
        Adjective adj = new Adjective();
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

