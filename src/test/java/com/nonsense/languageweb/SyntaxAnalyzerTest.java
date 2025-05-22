package com.nonsense.languageweb;

import com.nonsense.languageweb.SyntaxAnalyzer.SyntaxResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;

public class SyntaxAnalyzerTest {

    @BeforeAll
    public static void setup() {
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", "src/main/resources/google-key.json");
    }


    @Test
    public void testAnalyzeSyntaxBasicSentence() throws Exception {
        String sentence = "The quick brown fox jumps over the lazy dog";

        SyntaxResult result = SyntaxAnalyzer.analyzeSyntax(sentence);

        List<String> nouns = result.nouns;
        List<String> verbs = result.verbs;
        List<String> adjectives = result.adjectives;

        // Controlla che almeno uno dei gruppi non sia vuoto
        assertFalse(nouns.isEmpty(), "Expected some nouns");
        assertFalse(verbs.isEmpty(), "Expected some verbs");
        assertFalse(adjectives.isEmpty(), "Expected some adjectives");

        // Controlla che contenga parole attese
        assertTrue(nouns.contains("fox") || nouns.contains("dog"));
        assertTrue(verbs.contains("jumps"));
        assertTrue(adjectives.contains("quick") || adjectives.contains("lazy"));
    }

    @Test
    public void testAnalyzeSyntaxEmptyInput() throws Exception {
        String sentence = "";

        SyntaxResult result = SyntaxAnalyzer.analyzeSyntax(sentence);

        assertTrue(result.nouns.isEmpty());
        assertTrue(result.verbs.isEmpty());
        assertTrue(result.adjectives.isEmpty());
    }
}
