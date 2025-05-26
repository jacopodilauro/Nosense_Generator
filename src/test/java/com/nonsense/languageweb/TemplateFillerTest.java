// TemplateFillerTest.java
package com.nonsense.languageweb;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class TemplateFillerTest {

    private List<String> nouns;
    private List<String> verbs;
    private List<String> adjectives;
    private Map<String, String> verbiPassato;

    @BeforeEach
    void setUp() {
        nouns = new ArrayList<>();
        verbs = new ArrayList<>();
        adjectives = new ArrayList<>();
        verbiPassato = new HashMap<>();
        verbiPassato.put("run", "ran");
        verbiPassato.put("eat", "ate");
    }

    @Test
    void fillNullOrEmptyTemplateReturnsEmpty() {
        assertEquals("", TemplateFiller.fill(null, nouns, verbs, adjectives, "present", verbiPassato));
        assertEquals("", TemplateFiller.fill("", nouns, verbs, adjectives, "present", verbiPassato));
    }

    @Test
    void malformedTemplateThrowsException() {
        String badTemplate = "bad format";
        assertThrows(IllegalArgumentException.class, () ->
                TemplateFiller.fill(badTemplate, nouns, verbs, adjectives, "present", verbiPassato)
        );
    }

    @Test
    void fillPresentTense() {
        String template = "1. [noun] [verb] [adjective] | nouns:1, verbs:1, adjectives:1";
        nouns.add("cat");
        verbs.add("meow");
        adjectives.add("loud");
        String result = TemplateFiller.fill(template, nouns, verbs, adjectives, "present", verbiPassato);
        assertEquals("cat meow loud", result);
    }

    @Test
    void fillPastTenseWithMapping() {
        String template = "1. [noun] [verb] | nouns:1, verbs:1, adjectives:0";
        nouns.add("dog");
        verbs.add("run");
        String result = TemplateFiller.fill(template, nouns, verbs, adjectives, "1", verbiPassato);
        assertEquals("dog ran", result);
    }

    @Test
    void fillPastTenseFallback() {
        String template = "1. [noun] [verb] | nouns:1, verbs:1, adjectives:0";
        nouns.add("mouse");
        verbs.add("jump");
        String result = TemplateFiller.fill(template, nouns, verbs, adjectives, "1", verbiPassato);
        assertEquals("mouse jumped", result);
    }

    @Test
    void fillFutureTense() {
        String template = "1. [noun] [verb] | nouns:1, verbs:1, adjectives:0";
        nouns.add("bird");
        verbs.add("fly");
        String result = TemplateFiller.fill(template, nouns, verbs, adjectives, "4", verbiPassato);
        assertEquals("bird will fly", result);
    }
}
