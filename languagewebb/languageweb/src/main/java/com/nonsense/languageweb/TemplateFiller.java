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
public class TemplateFiller {

    /**
     * Riempie il template con le parole date e gestisce il tempo verbale.
     * @param template La frase con placeholder tipo [noun], [verb], [adjective]
     * @param nouns Lista di nomi (sostantivi)
     * @param verbs Lista di verbi base (infinito o presente)
     * @param adjectives Lista di aggettivi
     * @param tense Tempo verbale: "present", "past", "future"
     * @return Frase completa con parole inserite e verbi coniugati
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
        Pattern pat = Pattern.compile("nouns:(\\d+), verbs:(\\d+), adjectives:(\\d+)");
        Matcher m2 = pat.matcher(template);
        int n = 0; 
        int v = 0;
        int a = 0;
      if(m2.find()){
        n = Integer.parseInt(m2.group(1));
        v = Integer.parseInt(m2.group(2));
        a = Integer.parseInt(m2.group(3));
      }
      
      int dot = template.indexOf(". ");
      int pipe = template.indexOf(" | ");
      if (dot == -1 || pipe == -1 || pipe <= dot + 2) {
        throw new IllegalArgumentException("Il template non ha il formato corretto: manca '. ' o ' | ' oppure l'ordine è sbagliato");
    }
      String basis = template.substring(2+dot, pipe);

      try{
        while (n > nouns.size()) {
          Noun nn = new Noun();
          String nnn = nn.getWord();
          nouns.add(nnn);
      }
      while (v > verbs.size()) {
          Verb vv = new Verb();
          String vvv = vv.getWord();
          verbs.add(vvv);
      }
      while (a > adjectives.size()) {
          Adjective aa = new Adjective();
          String aaa = aa.getWord();
          adjectives.add(aaa);
      }
      } catch(IOException e){

      }

      Collections.shuffle(nouns);
      Collections.shuffle(verbs);
      Collections.shuffle(adjectives);

      if (tense.equals("1")) { // passato
        for (int j = 0; j < verbs.size(); j++) {
            String vrb = verbs.get(j).toLowerCase();
            if (verbiPassato.containsKey(vrb)) {
                verbs.set(j, verbiPassato.get(vrb));
            } else {
                verbs.set(j, vrb + "ed"); // fallback
            }
        }
    }

    // Sostituzione placeholder
    String sentenceOut = basis;
    for (int i=0; i<n; i++) sentenceOut = sentenceOut.replaceFirst("\\[noun\\]", nouns.get(i));
    for (int i = 0; i < v; i++) {
      String verb = verbs.get(i);
      if (tense.equals("4")) { // futuro
          if (verb.equals("is") || verb.equals("are")) {
              sentenceOut = sentenceOut.replaceFirst("\\[verb\\]", "will be");
          } else {
              sentenceOut = sentenceOut.replaceFirst("\\[verb\\]", "will " + verb);
          } }
      
      else {
          sentenceOut = sentenceOut.replaceFirst("\\[verb\\]", verb);
      }
  }
  
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