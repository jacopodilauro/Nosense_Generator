#!/bin/bat
printf "\r\033[K- Progetto NosenceGenerator -\n"
# Percorso assoluto alla chiave JSON
printf "🗝️​ Imposto la chiave..."
export GOOGLE_APPLICATION_CREDENTIALS="$(pwd)/google-key.json"

# Compila il progetto
printf "\r\033[K🔧 Compilazione in corso..."
mvn -q compile

# Esegui la classe Java principale
printf "\r\033[K🚀 Avvio del programma..."
mvn -q exec:java -Dexec.mainClass="com.nonsense.LanguageExample"
