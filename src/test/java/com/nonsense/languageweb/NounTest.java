// NounTest.java
package com.nonsense.languageweb;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

class NounTest {

    @BeforeAll
    static void initAll() {
        try {
            Path dir = Paths.get("resources");
            if (!Files.exists(dir)) Files.createDirectories(dir);
            Files.write(Paths.get("resources/nouns.txt"), List.of("cane", "gatto", "albero"));
        } catch (IOException e) {
            fail("Setup fallito: " + e.getMessage());
        }
    }

    @AfterAll
    static void tearDownAll() {
        try {
            Files.deleteIfExists(Paths.get("resources/nouns.txt"));
        } catch (IOException ignored) {}
    }

    @Test
    void testGetWordNotNullOrEmpty() throws IOException {
        Noun noun = new Noun();
        String word = noun.getWord();
        assertNotNull(word);
        assertFalse(word.isBlank());
    }

    @Test
    void testToStringEqualsWord() throws IOException {
        Noun noun = new Noun();
        assertEquals(noun.getWord(), noun.toString());
    }
}
