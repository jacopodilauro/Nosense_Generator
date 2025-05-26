package com.nonsense.languageweb;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * The {@code TemplateFiller} class provides functionality to dynamically fill
 * sentence templates with randomly selected nouns, verbs, and adjectives.
 * <p>
 * It also supports verb tense handling for past and future transformations.
 * The expected template format is:
 * <pre>
 *     Some intro. nouns:X, verbs:Y, adjectives:Z | The [noun] will [verb] the [adjective] [noun].
 * </pre>
 * where X, Y, Z are the required number of nouns, verbs, and adjectives respectively.
 */
public class TemplateFiller {

    /**
     * Fills a sentence template with the provided words, conjugating verbs based on the selected tense.
     *
     * @param template       A template string containing placeholders like [noun], [verb], [adjective],
     *                       and a header with the required counts (e.g., "nouns:2, verbs:1, adjectives:1").
     * @param nouns          A list of nouns to use.
     * @param verbs          A list of base-form verbs to use.
     * @param adjectives     A list of adjectives to use.
     * @param tense          The desired tense for verbs: "1" for past, "4" for future, any other value for present.
     * @param verbiPassato   A map of base-form verbs to their past-tense forms.
     * @return               A string with the placeholders replaced and verbs conjugated as specified.
     * @throws IllegalArgumentException if the template format is invalid.
     */
    public static String fill(String template,
                              List<String> nouns,
                              List<String> verbs,
                              List<String> adjectives,
                              String tense,
                              Map<String, String> verbiPassato) {

        if (template == null || template.isEmpty()) {
            return "";
        }

        /** Extract required counts of nouns, verbs, and adjectives from the template. */
        Pattern pat = Pattern.compile("nouns:(\\d+), verbs:(\\d+), adjectives:(\\d+)");
        Matcher m2 = pat.matcher(template);
        
        int n = 0; // Required nouns
        int v = 0; // Required verbs
        int a = 0; // Required adjectives
     
        if(m2.find()){
            n = Integer.parseInt(m2.group(1));
            v = Integer.parseInt(m2.group(2));
            a = Integer.parseInt(m2.group(3));
        }
      
        /** Locate the actual template sentence after ". " and before " | " */
        int dot = template.indexOf(". ");
        int pipe = template.indexOf(" | ");
        if (dot == -1 || pipe == -1 || pipe <= dot + 2) {
            throw new IllegalArgumentException("Template format is incorrect: missing '. ' or ' | ', or in wrong order.");
        }
        
        /** Extract the base sentence that contains placeholders. */
        String basis = template.substring(2+dot, pipe);

        try{
            while (n > nouns.size()) {
            nouns.add(new Noun().getWord());
            }
            while (v > verbs.size()) {
            verbs.add(new Verb().getWord());
            }
            while (a > adjectives.size()) {
            adjectives.add(new Adjective().getWord());
            }
        } catch(IOException e){
        /** If word generation fails, we silently ignore; ideally log or handle this. */
        }

        /** Shuffle the word lists to randomize selection. */
        Collections.shuffle(nouns);
        Collections.shuffle(verbs);
        Collections.shuffle(adjectives);

        /** Conjugate verbs for past tense ("1"). */
        if (tense.equals("1")) { 
            for (int j = 0; j < verbs.size(); j++) {
                String vrb = verbs.get(j).toLowerCase();
                if (verbiPassato.containsKey(vrb)) {
                    verbs.set(j, verbiPassato.get(vrb));
                } else {
                    verbs.set(j, vrb + "ed"); // fallback for unknown verbs
                }
            }
    }

    /** Begin replacing placeholders in the base sentence. */
    String sentenceOut = basis;
    
    /** Replace [noun] placeholders. */
    for (int i=0; i<n; i++) sentenceOut = sentenceOut.replaceFirst("\\[noun\\]", nouns.get(i));
    
    /** Replace [verb] placeholders based on tense. */
    for (int i = 0; i < v; i++) {
      String verb = verbs.get(i);
      if (tense.equals("4")) { 
          if (verb.equals("is") || verb.equals("are")) {
            sentenceOut = sentenceOut.replaceFirst("\\[verb\\]", "will be");
          } else {
              sentenceOut = sentenceOut.replaceFirst("\\[verb\\]", "will " + verb);
          } }
      
      else {
        sentenceOut = sentenceOut.replaceFirst("\\[verb\\]", verb);
      }
    }
  
    /** Replace [adjective] placeholders. */
    for (int i=0; i<a; i++) sentenceOut = sentenceOut.replaceFirst("\\[adjective\\]", adjectives.get(i));
        return sentenceOut;
    }
}
































/*
public class TemplateFiller {

    /**
     * Riempie il template con le parole date e gestisce il tempo verbale.
     * @param template La frase con placeholder tipo [noun], [verb], [adjective]
     * @param nouns Lista di nomi (sostantivi)
     * @param verbs Lista di verbi base (infinito o presente)
     * @param adjectives Lista di aggettivi
     * @param tense Tempo verbale: "present", "past", "future"
     * @param pastTenseMap Mappa verbo-base -> verbo al passato (per "past")
     * @return Frase completa con parole inserite e verbi coniugati
     
    public static String fill(String template,
                              List<String> nouns,
                              List<String> verbs,
                              List<String> adjectives,
                              String tense,
                              Map<String, String> pastTenseMap) {

        if (template == null || template.isEmpty()) {
            return "";
        }

        // Iteratori per scorrere le parole senza ripetizioni troppo complicate
        Iterator<String> nounIt = nouns.iterator();
        Iterator<String> verbIt = verbs.iterator();
        Iterator<String> adjIt = adjectives.iterator();

        // Pattern per trovare [noun], [verb], [adjective]
        Pattern pattern = Pattern.compile("\\[(noun|verb|adjective)]");
        Matcher matcher = pattern.matcher(template);

        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String placeholder = matcher.group(1);
            String replacement = "";

            switch (placeholder) {
                case "noun":
                    replacement = nounIt.hasNext() ? nounIt.next() : "noun";
                    break;
                case "adjective":
                    replacement = adjIt.hasNext() ? adjIt.next() : "adjective";
                    break;
                case "verb":
                    if (!verbIt.hasNext()) {
                        replacement = "verb";
                    } else {
                        String verb = verbIt.next();
                        switch (tense.toLowerCase()) {
                            case "past":
                                replacement = pastTenseMap.getOrDefault(verb, verb + "ed");
                                break;
                            case "future":
                                replacement = "will " + verb;
                                break;
                            case "present":
                            default:
                                replacement = verb;
                                break;
                        }
                    }
                    break;
            }

            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);

        return result.toString();
    }
}
*/
