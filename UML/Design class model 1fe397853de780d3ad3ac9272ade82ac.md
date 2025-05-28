# Design class model

@startuml
skinparam handwritten true



together {
title \n\n<size:40><font color=black>DESIGN CLASS MODEL\n\n\n\n\n\n\n

class "**NonsenseApplication**" {
+ TEMPLATES_FILE_PATH : String
+ PAST_TENSE_FILE_PATH : String
+ allTemplates : List<String>
+ pastTenseVerb : Map<String, String>
--
+ NonsenseApplication()
+ home(): String
+ addWord(file: String, word: String): String
+ getWords(s: String): String
+ addAll(s: String): String
+ appendToFile(fileName: String, word: String, type: String): String
+ escapeHtml(s: String): String
}

class "**Languagewebapplication**" {
+ main(String[] args) : void
}


class "**SyntaxAnalyzer**" {
+ analyzeSyntax(SyntaxResult) : SyntaxResult
}

class "**TemplateFiller**" {
+ fill(String template, List<String> nouns, List<String> verbs, List<String> adjectives, String tense, Map<String, String> verbPasttense) : String
}

class "**TemplateSelector**" {
+ selectCompatibleTemplate(List<String> nouns, List<String> verbs, List<String> adjectives, String templatesFilePath) : String
}

class "**SyntaxResult**" {
+ nouns : List<String>
+ verbs : List<String>
+ adjectives : List<String>
}

class "**Adjective**" {
FILE_PATH : String
RANDOM : Random
+ adjectives : List<String>
+ word : String
--
+ Adjective()
+ getWord() : String
+ toString() : String
}

class "**Noun**" {
FILE_PATH : String
RANDOM : Random
+ nouns : List<String>
+ word : String
--
+ Noun()
+ getWord() : String
+ toString() : String
}

class "**Verb**" {
FILE_PATH : String
RANDOM : Random
+ verbs : List<String>
+ word : String
--
+ Verb()
+ getWord() : String
+ toString() : String
}

"**NonsenseApplication**" .down.> "**SyntaxAnalyzer**"
"**NonsenseApplication**" .down.> "**TemplateFiller**"
"**NonsenseApplication**" .down.> "**TemplateSelector**"

"**SyntaxAnalyzer**" .down.> "**SyntaxResult**"

"**TemplateFiller**" --o "**Adjective**"
"**TemplateFiller**" --o "**Noun**"
"**TemplateFiller**" --o "**Verb**"

@enduml
