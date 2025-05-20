// SyntaxAnalyzerTest.java
package com.nonsense.languageweb;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Questo test è disabilitato perché SyntaxAnalyzer usa il
 * Google Cloud LanguageServiceClient, che richiede credenziali
 * e accesso di rete. Sbloccalo quando avrai a disposizione un mock
 * o le credenziali giuste.
 */
class SyntaxAnalyzerTest {

    @Test
    @Disabled("Richiede credenziali Google Cloud e accesso di rete")
    void testAnalyzeSyntax() throws Exception {
        // Esempio di test, da riattivare quando si mocka LanguageServiceClient:
        // SyntaxAnalyzer.SyntaxResult r = SyntaxAnalyzer.analyzeSyntax("The quick brown fox jumps over the lazy dog");
        // assertTrue(r.nouns.contains("fox"));
        // assertTrue(r.verbs.contains("jumps"));
        // assertTrue(r.adjectives.contains("quick"));
    }
}
