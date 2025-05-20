package com.nonsense.languageweb;
import com.google.cloud.language.v1.*; // Import all from v1 for simplicity here

import java.util.ArrayList;
import java.util.List;

public class SyntaxAnalyzer {

    public static class SyntaxResult {
        public final List<String> nouns;
        public final List<String> verbs;
        public final List<String> adjectives;

        public SyntaxResult(List<String> nouns, List<String> verbs, List<String> adjectives) {
            this.nouns = nouns;
            this.verbs = verbs;
            this.adjectives = adjectives;
        }
    }

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
                    case NOUN: nouns.add(word); break; // Simplified switch
                    case VERB: { if (!word.equalsIgnoreCase("will")) verbs.add(word); } break;
                    case ADJ: adjectives.add(word); break;
                    default: // No action for other tags
                        break;
                }
            }
        }
        return new SyntaxResult(nouns, verbs, adjectives);
    }
}