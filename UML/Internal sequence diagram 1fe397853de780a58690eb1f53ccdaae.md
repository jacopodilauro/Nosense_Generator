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
