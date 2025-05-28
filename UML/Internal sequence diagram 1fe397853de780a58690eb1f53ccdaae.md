# Internal sequence diagram

TEMPLATEFILLER

@startuml

participant TemplateFiller

activate TemplateFiller

TemplateFiller -> TemplateFiller : check if template is null or empty

TemplateFiller -> TemplateFiller : extract word counts (nouns, verbs, adjectives)\nfrom metadata using regex

TemplateFiller -> TemplateFiller : extract sentence basis (after ". ", before " | ")

TemplateFiller -> TemplateFiller : check if enough words are available

alt missing nouns
loop add missing nouns
TemplateFiller -> TemplateFiller : generate and add new noun
end
end

alt missing verbs
loop add missing verbs
TemplateFiller -> TemplateFiller : generate and add new verb
end
end

alt missing adjectives
loop add missing adjectives
TemplateFiller -> TemplateFiller : generate and add new adjective
end
end

TemplateFiller -> TemplateFiller : shuffle nouns, verbs, adjectives

alt if tense == "past" (1)
loop for each verb
TemplateFiller -> TemplateFiller : get past form from map\nor fallback to verb + "ed"
end
end

alt if tense == "future" (4)
TemplateFiller -> TemplateFiller : prepend \"will\" to verbs (except \"is\", \"are\")
end

TemplateFiller -> TemplateFiller : replace [noun] placeholders
TemplateFiller -> TemplateFiller : replace [verb] placeholders
TemplateFiller -> TemplateFiller : replace [adjective] placeholders

deactivate TemplateFiller

@enduml

TEMPLATESELECTOR

@startuml

participant TemplateSelector

activate TemplateSelector

TemplateSelector -> TemplateSelector : initialize compatible list
TemplateSelector -> TemplateSelector : compile regex pattern

TemplateSelector -> TemplateSelector : open template file
loop read each line
TemplateSelector -> TemplateSelector : match line with pattern
alt pattern matches
TemplateSelector -> TemplateSelector : extract n, v, a
alt nouns.size >= n \nand verbs.size >= v \nand adjectives.size >= a
TemplateSelector -> TemplateSelector : add line to compatible
else
alt all lists are empty
TemplateSelector -> TemplateSelector : add line to compatible
end
end
end
end

alt compatible is empty
TemplateSelector -> TemplateSelector : reopen template file
loop read each line
alt line contains " | nouns:"
TemplateSelector -> TemplateSelector : add line to compatible
end
end
end

deactivate TemplateSelector
@enduml

NONSENSEAPPLICATION

@startuml

participant "NonsenseApplication" as NA

activate NA

alt sentence != null
NA -> NA : escapeHtml(sentence)
end

alt selectedTemplate != null
NA -> NA : escapeHtml(templateToUse)
end

NA -> NA : appendToFile(...) [chiamata da addNoun/addVerb/addAdjective]

NA -> NA : escapeHtml(word) [usato nei messaggi HTML]

NA -> NA : write sentenceOut to output.txt

deactivate NA
@enduml

SYNTAXANALYZER

@startuml
participant "SyntaxAnalyzer" as Analyzer

Analyzer -> Analyzer: crea liste nouns, verbs, adjectives
Analyzer -> Analyzer: crea LanguageServiceClient (try-with-resources)
Analyzer -> Analyzer: costruisci Document (setContent, setType, build)
Analyzer -> Analyzer: chiama analyzeSyntax(doc)
Analyzer -> Analyzer: ottieni lista Tokens

loop per ogni Token
Analyzer -> Analyzer: getText().getContent()  // parola
Analyzer -> Analyzer: getPartOfSpeech().getTag()  // tag
alt tag == NOUN
Analyzer -> Analyzer: aggiungi parola a nouns
else tag == VERB e parola != "will"
Analyzer -> Analyzer: aggiungi parola a verbs
else tag == ADJ
Analyzer -> Analyzer: aggiungi parola a adjectives
else
Analyzer -> Analyzer: ignora
end
end

Analyzer -> Analyzer: crea e restituisci SyntaxResult(nouns, verbs, adjectives)
@enduml
