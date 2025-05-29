// VerbTest.java
package com.nonsense.languageweb;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

class VerbTest {

    @BeforeAll
    static void initAll() {
        try {
            Path dir = Paths.get("resources");
            if (!Files.exists(dir)) Files.createDirectories(dir);
            Files.write(Paths.get("resources/verbs.txt"), List.of("mangiare", "correre", "dormire"));
        } catch (IOException e) {
            fail("Setup fallito: " + e.getMessage());
        }
    }

    @AfterAll
    static void tearDownAll() {
        try {
            Files.deleteIfExists(Paths.get("resources/verbs.txt"));
        } catch (IOException ignored) {}
    }

    @Test
    void testGetWordNotNullOrEmpty() throws IOException {
        Verb verb = new Verb();
        String word = verb.getWord();
        assertNotNull(word);
        assertFalse(word.isBlank());
    }

    @Test
    void testToStringEqualsWord() throws IOException {
        Verb verb = new Verb();
        assertEquals(verb.getWord(), verb.toString());
    }
}
