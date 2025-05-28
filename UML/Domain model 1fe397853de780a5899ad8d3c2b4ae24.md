# Domain model

@startuml
allowmixing
object user
object SyntaxAnalyzer
object "Google Cloud API"
object SyntaxResult
object TemplateSelector
object LanguagewebApplication
together {
object TemplateFiller
database "template.txt"
}

user "1    " --> "1 " SyntaxAnalyzer : "inserisce frase \nda analizzare"
SyntaxAnalyzer "1  " --> "1    " "Google Cloud API" : "invia la \nfrase"
SyntaxAnalyzer "1" <-- "1 " "Google Cloud API" : "invia un'analisi \ngrammaticale \ndei token"
SyntaxAnalyzer "1" --> "1  " SyntaxResult : restituisce
note left of SyntaxResult : oggetto con 3 liste \nnouns \nverbs \nadjectives
TemplateSelector "1" --> "0..*" "template.txt" : "legge e seleziona \nun template \ncompatibile"
TemplateSelector "1" --> "1     " TemplateFiller : "passa il \ntemplate \nselezionato"
SyntaxResult "1" --> "1  " TemplateSelector : "passa le sue \ntre liste"
SyntaxResult "1" --> "1" TemplateFiller : "passa le sue \ntre liste"
TemplateFiller "1    " <-- "1   " LanguagewebApplication : "passa il \ntempo verbale \nselezionato"
TemplateFiller "1 " <-- "1" LanguagewebApplication : "passa la \nmappa dei \nverbi coniugati"
TemplateFiller "1" --> "1" user : "restituisce la \nfrase generata"

@enduml