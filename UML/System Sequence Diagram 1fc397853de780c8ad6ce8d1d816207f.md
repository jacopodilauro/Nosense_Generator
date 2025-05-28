# System Sequence Diagram

@startuml
actor User
participant "NonsenseApplication" as Controller
participant "SyntaxAnalyzer"
participant "TemplateSelector"
participant "TemplateFiller"
participant "Noun"
participant "Adjective"
participant "Verb"
User -> Controller : Get sentence
activate Controller

alt Sentence provided
Controller -> SyntaxAnalyzer : analyzeSyntax(Sentence)
Controller <-- SyntaxAnalyzer : SyntaxResult(nouns, verbs, adjectuves)
end
alt Template selected
Controller -> TemplateSelector : Use provided template
else
Controller -> TemplateSelector : selectCompatibleTemplate(nouns, verbs, adjectives, TEMPLATES_FILE_PATH)
Controller <-- TemplateSelector : templateToUse
end
Controller -> TemplateFiller : fill(templateToUse, nouns, verbs, adjectives, tense, pastTenseVerbs)
TemplateFiller -> TemplateFiller : parse template for noun, verb, adjective counts

alt if missing nouns
loop while nouns.size < required
TemplateFiller -> Noun : new Noun()
TemplateFiller <- Noun: getWord()
TemplateFiller -> TemplateFiller : add word to nouns
end
end

alt if missing verbs
loop while verbs.size < required
TemplateFiller -> Verb : new Verb()
Verb -> TemplateFiller : getWord()
TemplateFiller -> TemplateFiller : add word to verbs
end
end

alt if missing adjectives
loop while adjectives.size < required
TemplateFiller -> Adjective : new Adjective()
TemplateFiller <- Adjective: getWord()
TemplateFiller -> TemplateFiller : add word to adjectives
end
end

TemplateFiller -> TemplateFiller : shuffle all word lists

alt tense = "past"
TemplateFiller -> TemplateFiller : convert each verb using pastTenseMap or fallback
else tense = "future"
TemplateFiller -> TemplateFiller : prepend "will" or use "will be"
else
TemplateFiller -> TemplateFiller : use verbs as-is (present)
end

TemplateFiller -> TemplateFiller : replace [noun], [verb], [adjective] placeholders

Controller <-- TemplateFiller : sentenceOut

@enduml