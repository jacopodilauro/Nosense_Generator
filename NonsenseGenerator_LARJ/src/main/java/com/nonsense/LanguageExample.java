package com.nonsense;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.cloud.language.v1.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Math;

/**
 * Class using the Google Cloud Language APIs to:
 * <ul>
 *   <li>Analyze the syntactic structure of a user-entered sentence</li>
 *   <li>Categorize words into nouns, verbs, and adjectives</li>
 *   <li>Select a template from a file based on the number of detected words</li>
 *   <li>Generate and save a randomized sentence by replacing placeholders in the template</li>
 *   <li>Apply the user’s chosen verb tense</li>
 *   <li>Perform content moderation on the generated output</li>
 * </ul>
 */
public class LanguageExample {
    /**
     * Application entry point.
     * <p>
     * Ask if the user want to add terms in the dictionary.
     * Prompts the user to enter a sentence, analyzes its syntax,
     * splits words into categories, picks a compatible template,
     * fills in placeholders with real words (including verb tense adjustment),
     * writes the result to a file, and finally checks for any sensitive content.
     * </p>
     *
     * @param args command-line arguments (not used)
     * @throws Exception if an error occurs while creating the LanguageServiceClient
     */
    public static void main(String[] args) throws Exception {
        // Acquire a sentence from user input
        Scanner scanner = new Scanner(System.in);
        String ty;
        System.out.println("\r\033[KDo you want to add terms to the dictionary? y/n");
        String select = scanner.nextLine();
        if (select.equals("y")) {
            do {
                System.out.println("What do you add? Quit[q] | Verb[v] | Adjective[a] | Noun[n]");
                ty = scanner.nextLine();
                if (!ty.equals("q")) {
                    System.out.println("insert");
                    String word = scanner.nextLine();
                    switch (ty) {
                        case "v":
                            try (FileWriter fw = new FileWriter("resources/verbs.txt", true);
                                 BufferedWriter bw = new BufferedWriter(fw);
                                 PrintWriter out = new PrintWriter(bw)) {

                                out.println(word);
                                System.out.println("Word successfully added to verb.txt!");

                            } catch (IOException e) {
                                System.out.println("Errore durante la scrittura su verb.txt: " + e.getMessage());
                            }
                            break;
                        case "a":
                            try (FileWriter fw = new FileWriter("resources/adjectives.txt", true);
                                 BufferedWriter bw = new BufferedWriter(fw);
                                 PrintWriter out = new PrintWriter(bw)) {

                                out.println(word);
                                System.out.println("Parola aggiunta con successo a adjectives.txt!");

                            } catch (IOException e) {
                                System.out.println("Errore durante la scrittura su adjectives.txt: " + e.getMessage());
                            }
                            break;
                        case "n":
                            try (FileWriter fw = new FileWriter("resources/nouns.txt", true);
                                 BufferedWriter bw = new BufferedWriter(fw);
                                 PrintWriter out = new PrintWriter(bw)) {

                                out.println(word);
                                System.out.println("Parola aggiunta con successo a nouns.txt!");

                            } catch (IOException e) {
                                System.out.println("Errore durante la scrittura su nouns.txt: " + e.getMessage());
                            }
                            break;
                    }
                }
            } while (!ty.equals("q"));
        }

        System.out.print("\r\033[KEnter a sentence to analyze: ");
        String userInput = scanner.nextLine();
        System.out.print("Analyzing the sentence...");

        // Build the document with the user's text
        Document doc = Document.newBuilder()
                .setContent(userInput)
                .setType(Document.Type.PLAIN_TEXT)
                .build();

        // Lists for word categorization
        List<String> nouns = new ArrayList<>();
        List<String> verbs = new ArrayList<>();
        List<String> adjectives = new ArrayList<>();

        // Call Google Cloud Syntax Analysis service
        try (LanguageServiceClient language = LanguageServiceClient.create()) {
            AnalyzeSyntaxResponse response = language.analyzeSyntax(doc);
            for (Token token : response.getTokensList()) {
                String word = token.getText().getContent();
                PartOfSpeech.Tag tag = token.getPartOfSpeech().getTag();

                switch (tag) {

                    case NOUN:
                        nouns.add(word);
                        break;
                    case VERB:
                        if (!word.equals("will")) {
                            verbs.add(word);
                        }
                        break;
                    case ADJ:
                        adjectives.add(word);
                        break;
                    default:
                        // Ignore
                        break;
                }
            }
        }
        int nounsDim = nouns.size();
        int verbsDim = verbs.size();
        int adjDim = adjectives.size();

        // Print the results
        System.out.println("\r\033[KSentence structure:");
        System.out.println("Names: " + nounsDim + " " + nouns);
        System.out.println("Verbs: " + verbsDim + " " + verbs);
        System.out.println("Adjectives: " + adjDim + " " + adjectives + "\n");


        String percorsoFile = "resources/sentence_templates.txt";
// Pattern to extract the precomputed counts
        Pattern pattern = Pattern.compile("nouns:(\\d+), verbs:(\\d+), adjectives:(\\d+)");
        List<String> frasiCompatibili = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(percorsoFile))) {
            String riga;
            while ((riga = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(riga);
                if (matcher.find()) {
                    int n = Integer.parseInt(matcher.group(1));
                    int v = Integer.parseInt(matcher.group(2));
                    int a = Integer.parseInt(matcher.group(3));
                    if (n >= nounsDim && v >= verbsDim && a >= adjDim) {
                        frasiCompatibili.add(riga);
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Randomly select a compatible template
        int rdm = (int) (Math.random() * frasiCompatibili.size());

        String template = frasiCompatibili.get(rdm);

        // DA ELIMINARE
        System.out.println(template + "\n");
// Extract counts from the template
        int n = 0, v = 0, a = 0;
        Matcher matcher = pattern.matcher(template);
        if (matcher.find()) {
            n = Integer.parseInt(matcher.group(1));
            v = Integer.parseInt(matcher.group(2));
            a = Integer.parseInt(matcher.group(3));
        }
        // Extract sentence part from the template
        int puntoIndex = template.indexOf(". ");
        int pipeIndex = template.indexOf(" | ");
        String sentence = template.substring(puntoIndex + 2, pipeIndex);
        // Fill missing words via generators
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

        Collections.shuffle(nouns);
        Collections.shuffle(verbs);
        Collections.shuffle(adjectives);


        int i = 0;

// Load past-tense verbs map for transformation
        Map<String, String> verbiPassato = new HashMap<>();

// Load past.txt
        try (BufferedReader br = new BufferedReader(new FileReader("resources/past.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 2) {
                    verbiPassato.put(parts[0].toLowerCase(), parts[1]);
                }
            }
        }


//VERB TENSE


        System.out.println("Verb tenses: past [1] | present [2] | future [4]");
        System.out.print("Choose the verb tense: ");
        int tempo = scanner.nextInt();
        scanner.close();

        if (tempo == 1) {
            for (int j = 0; j < verbs.size(); j++) {
                String vrb = verbs.get(j).toLowerCase();
                if (verbiPassato.containsKey(vrb)) {
                    verbs.set(j, verbiPassato.get(vrb));
                }
            }
        }

// Replace placeholders in the sentence
        while (i < n) {
            sentence = sentence.replaceFirst("\\[noun\\]", nouns.get(i));
            i++;
        }
        i = 0;
        while (i < v) {
            String verb = verbs.get(i);
            if (tempo == 4) {
                if (verb.equals("is") || verb.equals("are")) {
                    sentence = sentence.replaceFirst("\\[verb\\]", "will be");
                } else {
                    sentence = sentence.replaceFirst("\\[verb\\]", "will " + verb);
                }
            } else {
                sentence = sentence.replaceFirst("\\[verb\\]", verb);
            }
            i++;
        }
        i = 0;

        while (i < a) {
            sentence = sentence.replaceFirst("\\[adjective\\]", adjectives.get(i));
            i++;
        }
        System.out.println(sentence);
        // Print and save the generated sentence in .txt
        try (java.io.FileWriter writer = new java.io.FileWriter("output_sentence.txt", true)) { // true = append
            writer.write(sentence + "\n");
            System.out.println("\nFrase salvata in 'output_sentence.txt'");
        } catch (IOException e) {
            System.err.println("\nErrore durante il salvataggio della frase: " + e.getMessage());
        }


        // Content moderation on the generated sentence
        double soglia = 0.00;

        try (LanguageServiceClient language = LanguageServiceClient.create()) {
            Document moderationDoc = Document.newBuilder()
                    .setContent(sentence)
                    .setType(Document.Type.PLAIN_TEXT)
                    .build();

            ModerateTextRequest moderationRequest = ModerateTextRequest.newBuilder()
                    .setDocument(moderationDoc)
                    .build();

            ModerateTextResponse moderationResponse = language.moderateText(moderationRequest);

            List<ClassificationCategory> filtered = new ArrayList<>();
            for (ClassificationCategory category : moderationResponse.getModerationCategoriesList()) {
                if (category.getConfidence() > soglia) {
                    filtered.add(category);
                }
            }

            if (filtered.isEmpty()) {
                System.out.println("\n✅ Nessun contenuto problematico rilevato.");
            } else {
                System.out.println("\n⚠️ Contenuto potenzialmente sensibile rilevato (sopra la soglia):");
                for (ClassificationCategory category : filtered) {
                    System.out.printf("- Categoria: %s (confidenza: %.2f)\n", category.getName(), category.getConfidence());
                }
            }
        }


    }
}
