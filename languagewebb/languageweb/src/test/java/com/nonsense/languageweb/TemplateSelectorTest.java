// TemplateSelectorTest.java
package com.nonsense.languageweb;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TemplateSelectorTest {

    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("templates", ".txt");
        List<String> lines = Arrays.asList(
                "nouns:1, verbs:1, adjectives:1. A [noun] [verb] [adjective] | foo",
                "nouns:2, verbs:0, adjectives:0. Two [noun]s | bar"
        );
        Files.write(tempFile, lines);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testSelectCompatibleWithEnoughWords() throws IOException {
        List<String> nouns = Arrays.asList("apple", "banana");
        List<String> verbs = Collections.singletonList("eat");
        List<String> adjs = Collections.singletonList("sweet");

        String chosen = TemplateSelector.selectCompatibleTemplate(nouns, verbs, adjs, tempFile.toString());
        assertNotNull(chosen);
        // Con due nouns disponibili può anche scegliere la seconda
        assertTrue(chosen.contains("nouns:") );
    }

    @Test
    void testSelectFallbackWhenNoneCompatible() throws IOException {
        List<String> nouns = Collections.emptyList();
        List<String> verbs = Collections.emptyList();
        List<String> adjs  = Collections.emptyList();

        String chosen = TemplateSelector.selectCompatibleTemplate(nouns, verbs, adjs, tempFile.toString());
        assertNotNull(chosen);
    }
}
