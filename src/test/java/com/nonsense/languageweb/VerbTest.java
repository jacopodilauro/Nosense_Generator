// VerbTest.java
package com.nonsense.languageweb;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VerbTest {

    private static final Path verbsFile = Paths.get("resources/verbs.txt");
    private static final List<String> sample = Arrays.asList("run", "jump", "play");

    @BeforeAll
    static void setUp() throws IOException {
        Files.createDirectories(verbsFile.getParent());
        Files.write(verbsFile, sample);
    }

    @AfterAll
    static void tearDown() throws IOException {
        Files.deleteIfExists(verbsFile);
    }

    @Test
    void testGetWordFromList() throws IOException {
        Verb v = new Verb();
        assertTrue(sample.contains(v.getWord()), "Il verbo deve provenire dal file verbs.txt");
    }
}
