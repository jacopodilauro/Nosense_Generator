package com.nonsense.languageweb;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

class VerbTest {

    private static final Path VERBS_FILE = Paths.get("resources/verbs.txt");
    private static List<String> originalContent;
    private static boolean fileExisted;

    @BeforeAll
    static void initAll() {
        try {
            Files.createDirectories(VERBS_FILE.getParent());

            // Salva il contenuto solo se il file esiste
            if (Files.exists(VERBS_FILE)) {
                originalContent = Files.readAllLines(VERBS_FILE);
                fileExisted = true;
            } else {
                fileExisted = false;
            }

            // Sovrascrive con contenuto di test
            Files.write(VERBS_FILE, List.of("mangiare", "correre", "dormire"));
        } catch (IOException e) {
            fail("Setup fallito: " + e.getMessage());
        }
    }

    @AfterAll
    static void tearDownAll() {
        try {
            
            if (fileExisted && originalContent != null) {
                // Ripristina il contenuto originale
                Files.write(VERBS_FILE, originalContent);
                
            } else {
                // Elimina il file se non esisteva prima
                Files.deleteIfExists(VERBS_FILE);
            }
        } catch (IOException e) {
            System.err.println("Errore nel ripristino del file verbs.txt: " + e.getMessage());
        }
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

