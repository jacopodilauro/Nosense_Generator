#!/bin/bash
printf "\r\033[K- NoSenseGenerator Project -\n"
# Percorso assoluto alla chiave JSON
printf "🗝️​ Setting the key..."
export GOOGLE_APPLICATION_CREDENTIALS="$(pwd)/src/main/resources/google-key.json"

# Compila il progetto
printf "\r\033[K🔧 Compiling in progress..."
mvn -q compile

# Esegui la classe Java principale
printf "\r\033[K🚀 Starting program..."
#!/usr/bin/env bash
mvn clean -q compile exec:java \
  -Dexec.mainClass="com.nonsense.LanguageExample"

