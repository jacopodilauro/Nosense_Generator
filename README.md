# NOSENSE GENERATOR
Introduzione

## Struttura del Progetto
- `src/` : Contiene il codice sorgente del programma.
- `test/` : Include i test unitari.
- `Documenti/` : Documentazione dettagliata, inclusi manuali e specifiche tecniche.
- `pom.xml` : File di configurazione per Maven.

## Requisiti
- **Java**: Versione 17 o superiore.
- **Maven**: Versione 3.6 o superiore.

Verifica le versioni installate con:

```
java -version
mvn -v
```

## Installazione
Clona il repository:
```
git clone "link"
cd Nonsense Generator
```

Compila il progetto: 
```
mvn clean install
```

Esegui i test (facoltativi)
```
mvn test
```

Avvio del programma
Il programma è runnabile attraverso il comando maven:

```
mvn exec:java
``` 

in alternativo è possibile eseguire il JAR situato nella cartella nome_cartella


