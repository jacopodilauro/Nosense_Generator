package com.nonsense.languageweb;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class TemplateSelector {

    public static String selectCompatibleTemplate(List<String> nouns, List<String> verbs, List<String> adjectives, String templateFilePath) throws IOException {
        List<String> compatible = new ArrayList<>();
        Pattern pat = Pattern.compile("nouns:(\\d+), verbs:(\\d+), adjectives:(\\d+)");

        try (BufferedReader br = new BufferedReader(new FileReader(templateFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Matcher m = pat.matcher(line);
                if (m.find()) {
                    int n = Integer.parseInt(m.group(1));
                    int v = Integer.parseInt(m.group(2));
                    int a = Integer.parseInt(m.group(3));
                    // If the template requires more words than available, it's still "compatible"
                    // because TemplateFiller will add random words.
                    // The original logic was (n >= nouns.size() && v >= verbs.size() && a >= adjectives.size())
                    // which means template needs *at most* as many words as provided.
                    // If we want to ensure the template can be filled *even if more words are needed randomly*,
                    // then any template is "compatible" initially, and TemplateFiller handles shortages.
                    // For now, sticking to a more direct "can be filled with current words OR LESS"
                    if (nouns.size() >= n && verbs.size() >= v && adjectives.size() >= a) {
                        compatible.add(line);
                    } else if (nouns.isEmpty() && verbs.isEmpty() && adjectives.isEmpty()){
                        // If no words are provided by user, all templates are potentially compatible
                        // as TemplateFiller will use random words.
                        compatible.add(line);
                    }
                }
            }
        }
        // If after specific filtering no compatible templates are found,
        // and words were provided, try to find any template if the initial lists were not empty.
        // This part might need refinement based on exact desired fallback behavior.
        // A simpler approach if compatible is empty is just to pick ANY template and let TemplateFiller do its job.
        if (compatible.isEmpty()) {
            try (BufferedReader br = new BufferedReader(new FileReader(templateFilePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.contains(" | nouns:")) { // Basic check for a valid template line
                        compatible.add(line); // Add all templates as a fallback
                    }
                }
            }
        }


        if (compatible.isEmpty()) {
            return null;  // No compatible template found
        }

        return compatible.get(new Random().nextInt(compatible.size()));
    }
}