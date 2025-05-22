package com.nonsense.languageweb;

/** Import all from v1 for simplicity here. */
import com.google.cloud.language.v1.*; 

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class that uses the Google Cloud Natural Language API
 * to analyze the syntax of a given sentence.
 * <p>
 * It extracts and categorizes words into nouns, verbs, and adjectives.
 */
public class SyntaxAnalyzer {

    /**
     * A container class to store the results of the syntax analysis.
     * <p>
     * It holds separate lists of identified nouns, verbs, and adjectives.
     */
    public static class SyntaxResult {
        /** List of identified nouns in the sentence. */
        public final List<String> nouns;

        /** List of identified verbs in the sentence. */
        public final List<String> verbs;

        /** List of identified adjectives in the sentence. */
        public final List<String> adjectives;

        /**
         * Constructs a SyntaxResult with the given word lists.
         *
         * @param nouns      list of nouns
         * @param verbs      list of verbs
         * @param adjectives list of adjectives
         */
        public SyntaxResult(List<String> nouns, List<String> verbs, List<String> adjectives) {
            this.nouns = nouns;
            this.verbs = verbs;
            this.adjectives = adjectives;
        }
    }

    /**
     * Analyzes the syntax of the given sentence using Google Cloud's Natural Language API.
     * <p>
     * Extracts and classifies words into nouns, verbs (excluding "will"), and adjectives.
     *
     * @param sentence the input sentence to analyze
     * @return a {@link SyntaxResult} containing categorized words
     * @throws Exception if the language service fails or the API call encounters an error
     */
    public static SyntaxResult analyzeSyntax(String sentence) throws Exception {
        List<String> nouns = new ArrayList<>();
        List<String> verbs = new ArrayList<>();
        List<String> adjectives = new ArrayList<>();

        try (LanguageServiceClient language = LanguageServiceClient.create()) {
            Document doc = Document.newBuilder()
                    .setContent(sentence)
                    .setType(Document.Type.PLAIN_TEXT)
                    .build();

            AnalyzeSyntaxResponse response = language.analyzeSyntax(doc);

            for (Token token : response.getTokensList()) {
                String word = token.getText().getContent();
                PartOfSpeech.Tag tag = token.getPartOfSpeech().getTag();
                switch (tag) {
                    case NOUN: 
                        nouns.add(word); 
                        break; 
                    case VERB: 
                        if (!word.equalsIgnoreCase("will")) verbs.add(word);  
                        break;
                    case ADJ: 
                        adjectives.add(word); 
                        break;
                    default: 
                        /** Ignore other parts of speech. */
                        break;
                }
            }
        }
        return new SyntaxResult(nouns, verbs, adjectives);
    }
}
