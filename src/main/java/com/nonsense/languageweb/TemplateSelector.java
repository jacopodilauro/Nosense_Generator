package com.nonsense.languageweb;

import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * The {@code TemplateSelector} class is responsible for selecting a sentence template
 * that is compatible with the provided lists of nouns, verbs, and adjectives.
 * <p>
 * A template is considered compatible if the required number of words (as specified in the template)
 * can be fulfilled using the provided lists. If the lists are empty, all templates are considered potentially compatible,
 * as fallback logic (e.g., in {@code TemplateFiller}) can generate missing words.
 */
public class TemplateSelector {

    /**
     * Selects a random template from the given file that is compatible with the available words.
     * If no perfectly matching template is found, a fallback strategy attempts to load all possible templates.
     *
     * @param nouns            A list of available nouns.
     * @param verbs            A list of available verbs.
     * @param adjectives       A list of available adjectives.
     * @param templateFilePath The file path where templates are stored.
     * @return A compatible template string, or {@code null} if none can be found.
     * @throws IOException if an error occurs while reading the template file.
     */
    public static String selectCompatibleTemplate(  List<String> nouns,
                                                    List<String> verbs, 
                                                    List<String> adjectives, 
                                                    String templateFilePath) throws IOException {
        List<String> compatible = new ArrayList<>();
        
        /** Regular expression to extract required word counts from the template metadata. */
        Pattern pat = Pattern.compile("nouns:(\\d+), verbs:(\\d+), adjectives:(\\d+)");

        /** First pass: try to find compatible templates based on available word counts. */
        try (BufferedReader br = new BufferedReader(new FileReader(templateFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Matcher m = pat.matcher(line);
                if (m.find()) {
                    int n = Integer.parseInt(m.group(1));
                    int v = Integer.parseInt(m.group(2));
                    int a = Integer.parseInt(m.group(3));

                    /** If the template requires more words than available, it's still "compatible"
                     * because TemplateFiller will add random words.
                     * The original logic was (n >= nouns.size() && v >= verbs.size() && a >= adjectives.size())
                     * which means template needs *at most* as many words as provided.
                     * If we want to ensure the template can be filled *even if more words are needed randomly*,
                     * then any template is "compatible" initially, and TemplateFiller handles shortages.
                     * For now, sticking to a more direct "can be filled with current words OR LESS"
                    */
                    if (nouns.size() >= n && verbs.size() >= v && adjectives.size() >= a) {
                        compatible.add(line);
                    } 
                    /** 
                     * If no words are provided by user, all templates are potentially compatible 
                     * as TemplateFiller will use random words. */
                    else if (nouns.isEmpty() && verbs.isEmpty() && adjectives.isEmpty()){

                        compatible.add(line);
                    }
                }
            }
        }
        
        /** If no compatible templates were found in the first pass, fallback to adding all possible templates. */
        if (compatible.isEmpty()) {
            try (BufferedReader br = new BufferedReader(new FileReader(templateFilePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.contains(" | nouns:")) {      // Basic check to include all structured templates.
                        compatible.add(line);              
                    }
                }
            }
        }

        /** Return a randomly selected compatible template, or null if none found. */
        if (compatible.isEmpty()) {
            return null;  
        }

        return compatible.get(new Random().nextInt(compatible.size()));
    }
}
