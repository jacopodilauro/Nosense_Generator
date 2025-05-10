package com.nonsense;

import com.google.cloud.language.v1.ModerateTextRequest;
import com.google.cloud.language.v1.ModerateTextResponse;
import com.google.cloud.language.v1.ClassificationCategory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.google.cloud.language.v1.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Math;

public class LanguageExample {
    public static void main(String[] args) throws Exception {
        // Acquisisci una frase da input
        Scanner scanner = new Scanner(System.in);
        System.out.print("\r\033[KEnter a sentence to analyze: ");
        String userInput = scanner.nextLine();
        System.out.print("Analyzing the sentence...");
		/*       
        System.out.println("Verb tenses --> past = 1 | present = 2 | future = 4");
        System.out.print("Choose the verb tense: ");
        int tempo = scanner.nextInt();
        scanner.close();
		
		CONTINUO DOPO
		*/

        // Crea il documento con il testo dell'utente
        Document doc = Document.newBuilder()
                .setContent(userInput)
                .setType(Document.Type.PLAIN_TEXT)
                .build();

        // Liste per categorizzare parole
        List<String> nouns = new ArrayList<>();
        List<String> verbs = new ArrayList<>();
        List<String> adjectives = new ArrayList<>();

        // Chiama il servizio di analisi sintattica di Google Cloud
        try (LanguageServiceClient language = LanguageServiceClient.create()) {
            AnalyzeSyntaxResponse response = language.analyzeSyntax(doc);
            for (Token token : response.getTokensList()) {
                String word = token.getText().getContent();
                PartOfSpeech.Tag tag = token.getPartOfSpeech().getTag();

                switch (tag) {

                    case NOUN:  // nomi propri
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
                        // Altri tipi non li consideriamo
                        break;
                }
            }
        }
        int nounsDim = nouns.size();
        int verbsDim = verbs.size();
        int adjDim = adjectives.size();

        // Stampa i risultati
        System.out.println("\r\033[KSentence structure:");
        System.out.println("Names: " + nounsDim + " " + nouns);
        System.out.println("Verbs: " + verbsDim + " " + verbs);
        System.out.println("Adjectives: " + adjDim + " " + adjectives + "\n");


        String percorsoFile = "resources/sentence_templates.txt"; // Sostituisci con il percorso corretto
        // Pattern per estrarre i valori già calcolati
        Pattern pattern = Pattern.compile("nouns:(\\d+), verbs:(\\d+), adjectives:(\\d+)");
        List<String> frasiCompatibili = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(percorsoFile))) {
            String riga;
            while ((riga = br.readLine()) != null) {
                //System.out.println(riga);
                Matcher matcher = pattern.matcher(riga);
                if (matcher.find()) {
                    int n = Integer.parseInt(matcher.group(1));
                    int v = Integer.parseInt(matcher.group(2));
                    int a = Integer.parseInt(matcher.group(3));
                    if (n >= nounsDim && v >= verbsDim && a >= adjDim) {
                        //System.out.println(riga);
                        frasiCompatibili.add(riga);
                        //System.out.printf("nouns=%d, verbs=%d, adjectives=%d ", n, v, a);
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int rdm = (int) (Math.random() * frasiCompatibili.size());
        //int rdm = ThreadLocalRandom.current().nextInt(frasiCompatibili.size());
        String template = frasiCompatibili.get(rdm);

        // DA ELIMINARE
        System.out.println(template + "\n");

        int n = 0, v = 0, a = 0;
        Matcher matcher = pattern.matcher(template);
        if (matcher.find()) {
            n = Integer.parseInt(matcher.group(1));
            v = Integer.parseInt(matcher.group(2));
            a = Integer.parseInt(matcher.group(3));
        }
        int puntoIndex = template.indexOf(". ");         // fine numerazione
        int pipeIndex = template.indexOf(" | ");         // inizio conteggio
        String sentence = template.substring(puntoIndex + 2, pipeIndex);
        //System.out.println(sentence + "\n nouns:"+ n + "  verbs:" + v+ "  adj:"+a);
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
       
       /*
        System.out.println("\nNomi: "+ nounsDim + " "+ nouns);
        System.out.println("Verbi: " +verbsDim +" " + verbs);
        System.out.println("Aggettivi: " +adjDim+ " "+ adjectives);
        */
        int i = 0;


        Map<String, String> verbiPassato = new HashMap<>();

// 1. Carica past.txt
        try (BufferedReader br = new BufferedReader(new FileReader("resources/past.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+"); // separa su spazi o tab
                if (parts.length >= 2) {
                    verbiPassato.put(parts[0].toLowerCase(), parts[1]);
                }
            }
        }


//TEMPI VERBALI


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
            // Salva la frase generata in un file .txt
            try (java.io.FileWriter writer = new java.io.FileWriter("output_sentence.txt", true)) { // true = append
                writer.write(sentence + "\n");
                System.out.println("\nFrase salvata in 'output_sentence.txt'");
            } catch (IOException e) {
                System.err.println("\nErrore durante il salvataggio della frase: " + e.getMessage());
            }


            //frase scurrile o no
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
