package com.nonsense.languageweb;

import com.google.cloud.language.v1.ClassificationCategory;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.ModerateTextRequest;
import com.google.cloud.language.v1.ModerateTextResponse;
// Ensure SyntaxAnalyzer.SyntaxResult is accessible or import Token and PartOfSpeech if used directly
// import com.google.cloud.language.v1.Token;
// import com.google.cloud.language.v1.PartOfSpeech;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// import java.util.Random; // Not directly used here anymore
import java.util.stream.Collectors;

@RestController
public class NonsenseApplication {

    private static final String TEMPLATES_FILE_PATH = "resources/sentence_templates.txt";
    private static final String PAST_TENSE_FILE_PATH = "resources/past.txt";
    private List<String> allTemplateLines;
    private Map<String, String> pastTenseVerbs; // Renamed from verbiPassato

    public NonsenseApplication() {
        // Load templates and past tense verbs at startup
        try {
            allTemplateLines = Files.readAllLines(Paths.get(TEMPLATES_FILE_PATH), StandardCharsets.UTF_8);
        } catch (IOException e) {
            allTemplateLines = new ArrayList<>();
            // Log the error or handle it as preferred
            System.err.println("Error reading templates: " + e.getMessage());
        }

        pastTenseVerbs = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PAST_TENSE_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+"); // Split by one or more whitespace
                if (parts.length >= 2) {
                    pastTenseVerbs.put(parts[0].toLowerCase(), parts[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading past tense verbs: " + e.getMessage());
        }
    }

    @GetMapping("/")
    @ResponseBody
    public String home(@RequestParam(name = "sentence", required = false) String sentence,
                       @RequestParam(name = "tense", required = false, defaultValue = "2") String tense,
                       @RequestParam(name = "selected_template", required = false) String selectedTemplateFullLine) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html><html><head>"
                + "<meta charset=\"UTF-8\"><title>Sentence Analysis & Nonsense Generator</title>"
                + "<link rel=\"stylesheet\" href=\"/css/style.css\">"
                + "</head><body>"
                + "<div class='header-controls'>"
                + "<h1>Sentence Analyzer & Nonsense Generator</h1>"
                + "  <button id='toggleDictButton' class='toggle-dict-button' onclick='toggleDict()'>Manage Dictionaries</button>"
                + "</div>"
                + "<div id='dictSection' class='dictionary-section' style='display: none;'>" // Initially hidden
                + "  <h3>Add Words to Dictionaries:</h3>"
                + "  <div class='dictionary-forms-container'>"
                + "    <form method='post' action='/addN' class='dict-form'>"
                + "      <input type='text' name='word' placeholder='New noun...' required />"
                + "      <button type='submit'>Add Noun</button>"
                + "    </form>"
                + "    <form method='post' action='/addA' class='dict-form'>"
                + "      <input type='text' name='word' placeholder='New adjective...' required />"
                + "      <button type='submit'>Add Adj.</button>"
                + "    </form>"
                + "    <form method='post' action='/addV' class='dict-form'>"
                + "      <input type='text' name='word' placeholder='New verb...' required />"
                + "      <button type='submit'>Add Verb</button>"
                + "    </form>"
                + "  </div>"
                + "</div>"
                + "<script>"
                + "function toggleDict() {"
                + "  var el = document.getElementById('dictSection');"
                + "  var button = document.getElementById('toggleDictButton');"
                + "  if (el.style.display === 'none' || el.style.display === '') {"
                + "    el.style.display = 'flex';" // or 'block' if preferred
                + "    button.textContent = 'Hide Dictionaries';"
                + "    button.classList.add('active');"
                + "  } else {"
                + "    el.style.display = 'none';"
                + "    button.textContent = 'Manage Dictionaries';"
                + "    button.classList.remove('active');"
                + "  }"
                + "}"
                + "</script>"
        );

        html.append("<form method='get' class='main-form'>"
                + "<label>Sentence to analyze:<br>"
                +  "<input id='sentenceInput' type='text' name='sentence' value='" + (sentence != null ? escapeHtml(sentence) : "") + "' placeholder='Enter a sentence to extract words, or leave blank to use only random words...' autofocus/>"
                + "</label>"

                + "<div class='form-row'>"
                + "<label class='form-label-group'>Desired verb tense:<br>"
                + "<select name='tense'>"
                + "<option value='2'" + ("2".equals(tense) ? " selected" : "") + ">Present</option>"
                + "<option value='1'" + ("1".equals(tense) ? " selected" : "") + ">Past</option>"
                + "<option value='4'" + ("4".equals(tense) ? " selected" : "") + ">Future</option>"
                + "</select>"
                + "</label>"

                + "<label class='form-label-group'>Choose a Template (optional):<br>"
                + "<select name='selected_template'>"
                + "<option value=\"\">-- Random (based on sentence) --</option>");

        if (allTemplateLines != null && !allTemplateLines.isEmpty()) {
            for (String templateLine : allTemplateLines) {
                if (templateLine.contains(" | ")) {
                    String displayPart = templateLine.substring(0, templateLine.indexOf(" | ")).trim();
                    // Use the full template line as value to easily retrieve it
                    html.append("<option value=\"" + escapeHtml(templateLine) + "\"" + (templateLine.equals(selectedTemplateFullLine) ? " selected" : "") + ">" + escapeHtml(displayPart) + "</option>");
                }
            }
        }
        html.append("</select></label></div>" // Closes form-row and template select
                + "<button type='submit' class='analyze-button'>Analyze and Generate Nonsense</button></form>");


        List<String> nouns = new ArrayList<>();
        List<String> verbs = new ArrayList<>();
        List<String> adjectives = new ArrayList<>();

        if (sentence != null && !sentence.isBlank()) {
            html.append("<h2>Analysis of Input Sentence:</h2>");
            try {
                SyntaxAnalyzer.SyntaxResult result = SyntaxAnalyzer.analyzeSyntax(sentence);
                nouns = result.nouns;
                verbs = result.verbs;
                adjectives = result.adjectives;
                html.append("<div class='analysis-results'>"
                        + "<p><strong>Nouns:</strong> " + (nouns.isEmpty() ? "None" : escapeHtml(String.join(", ", nouns))) + "</p>"
                        + "<p><strong>Verbs:</strong> " + (verbs.isEmpty() ? "None" : escapeHtml(String.join(", ", verbs))) + "</p>"
                        + "<p><strong>Adjectives:</strong> " + (adjectives.isEmpty() ? "None" : escapeHtml(String.join(", ", adjectives))) + "</p>"
                        + "</div>");

            } catch (Exception e) {
                html.append("<p class='error-message'>Error during language analysis with API: " + escapeHtml(e.getMessage()) + "</p>");
            }
        } else {
            html.append("<p>No sentence entered. The nonsense sentence will be generated with random words from the dictionaries.</p>");
        }


        String templateToUse;
        if (selectedTemplateFullLine != null && !selectedTemplateFullLine.isBlank()) {
            templateToUse = selectedTemplateFullLine;
            html.append("<h2>Manually Chosen Template:</h2><pre class='template-display'>" + escapeHtml(templateToUse.substring(0, templateToUse.indexOf(" | ")).trim()) + "</pre>");
        } else {
            try {
                templateToUse = TemplateSelector.selectCompatibleTemplate(nouns, verbs, adjectives, TEMPLATES_FILE_PATH);
                if (templateToUse == null) {
                    html.append("<p class='warning-message'>No compatible random template found with extracted words. Trying to generate one with more random words.</p>");
                    templateToUse = TemplateSelector.selectCompatibleTemplate(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), TEMPLATES_FILE_PATH);
                    if (templateToUse == null) {
                        html.append("<p class='error-message'>Cannot select a random template even without constraints. Check the templates file.</p>");
                        html.append("</body></html>"); // Close HTML before returning
                        return html.toString();
                    }
                }
                html.append("<h2>Randomly Selected Template:</h2><pre class='template-display'>" + escapeHtml(templateToUse.substring(0, templateToUse.indexOf(" | ")).trim()) + "</pre>");
            } catch (IOException e) {
                html.append("<p class='error-message'>Error reading templates for random selection: " + escapeHtml(e.getMessage()) + "</p>");
                html.append("</body></html>"); // Close HTML before returning
                return html.toString();
            }
        }

        if (templateToUse == null) { // Should ideally be caught by the block above, but as a safeguard
            html.append("<p class='error-message'>Could not select a template. Cannot generate sentence.</p>");
        } else {
            String sentenceOut = TemplateFiller.fill(templateToUse, nouns, verbs, adjectives, tense, pastTenseVerbs);
            html.append("<h2>Generated Nonsense Sentence:</h2><pre class='generated-sentence'>" + escapeHtml(sentenceOut) + "</pre>");

            // Content moderation
            try (LanguageServiceClient language = LanguageServiceClient.create()) {
                Document modDoc = Document.newBuilder()
                        .setContent(sentenceOut)
                        .setType(Document.Type.PLAIN_TEXT)
                        .build();
                ModerateTextRequest req = ModerateTextRequest.newBuilder().setDocument(modDoc).build();
                ModerateTextResponse resp = language.moderateText(req);
                List<ClassificationCategory> issues = resp.getModerationCategoriesList().stream()
                        .filter(cat -> cat.getConfidence() > 0.1) // Confidence threshold
                        .collect(Collectors.toList());

                if (issues.isEmpty()) {
                    html.append("<p class='moderation-status safe'>✅ No problematic content detected in the generated sentence (threshold >10%).</p>");
                } else {
                    html.append("<p class='moderation-status warning'>⚠️ Potentially sensitive content detected in the generated sentence:</p><ul class='moderation-issues'>");
                    for (ClassificationCategory cat : issues) {
                        html.append("<li>" + escapeHtml(cat.getName()) + " (Confidence: " + String.format("%.2f", cat.getConfidence()*100) + "%)</li>");
                    }
                    html.append("</ul>");
                }
            } catch (Exception e) {
                html.append("<p class='error-message'>Error during content moderation: " + escapeHtml(e.getMessage()) + "</p>");
            }
        }

        html.append("</body></html>");
        return html.toString();
    }


    @PostMapping("/addN")
    @ResponseBody
    public String addNoun(@RequestParam String word) throws IOException {
        return appendToFile("resources/nouns.txt", word.trim().toLowerCase(), "Noun");
    }

    @PostMapping("/addA")
    @ResponseBody
    public String addAdjective(@RequestParam String word) throws IOException {
        return appendToFile("resources/adjectives.txt", word.trim().toLowerCase(), "Adjective");
    }

    @PostMapping("/addV")
    @ResponseBody
    public String addVerb(@RequestParam String word) throws IOException {
        return appendToFile("resources/verbs.txt", word.trim().toLowerCase(), "Verb");
    }

    private synchronized String appendToFile(String filePath, String word, String type) throws IOException {
        if (word == null || word.isBlank() || !word.matches("[a-zA-Z ]+")) { // Allow spaces in words
            return "<!DOCTYPE html><html><head><meta charset='UTF-8'><link rel='stylesheet' href='/css/style.css'></head><body>"
                    + "<div class='container'><p class='error-message'>❌ Invalid word. Use only letters and spaces.</p><p><a href='/' class='button-link'>⬅️ Back to home</a></p></div></body></html>";
        }
        // Check for duplicates
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.trim().equalsIgnoreCase(word)) {
                    return "<!DOCTYPE html><html><head><meta charset='UTF-8'><link rel='stylesheet' href='/css/style.css'></head><body>"
                            +"<div class='container'><p class='warning-message'>⚠️ " + type + " \"" + escapeHtml(word) + "\" is already in the dictionary.</p><p><a href='/' class='button-link'>⬅️ Back to home</a></p></div></body></html>";
                }
            }
        }

        try (FileWriter fw = new FileWriter(filePath, true); // Corrected: append flag is a boolean
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(word);
        }
        return "<!DOCTYPE html><html><head><meta charset='UTF-8'><link rel='stylesheet' href='/css/style.css'></head><body>"
                +"<div class='container'><p class='success-message'>✅ " + type + " \"" + escapeHtml(word) + "\" added successfully!</p><p><a href='/' class='button-link'>⬅️ Back to home</a></p></div></body></html>";
    }

    // This method is no longer strictly necessary if you build the full HTML each time
    // private String wrapHtml(String body) {
    //    return body;
    // }

    private String escapeHtml(String s) {
        return s == null ? "" : s.replace("&", "&").replace("<", "<").replace(">", ">").replace("\"", "");
    }
}