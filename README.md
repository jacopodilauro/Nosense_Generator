# Nonsense Generator

## Introduction
The Nonsense Generator is a Java-based web application designed to create "nonsense" sentences. It achieves this by analyzing user-provided text to extract relevant words (nouns, verbs, and adjectives) and then using these, or randomly selected words from its dictionaries to eventually fill blank spaces, to fill pre-defined sentence templates. The application makes use of the Google Cloud Language API for advanced linguistic analysis, including syntax analysis and content moderation, ensuring generated sentences are within acceptable content boundaries.

## Project Structure

The project is organized into the following directories:

* `src/main/java/com/nonsense/lanquageweb/`: contains all the Java source code for the application.
* `/resources/`: contains the templates sentence and the dictionary of the various adjectives, nouns and verbs used.
* `src/test/java/com/nonsense/lanquageweb/`: houses the unit tests to ensure the application's functionality.
* `Documents/`: provides detailed documentation, including User Manual, User Stories, System Test Report, Unit Test, JavaDoc (in the `doc` folder open the file `index.html`).
* `pom.xml`: the Maven Project Object Model file, which manages the project's dependencies and build process.

## Requirements

To set up and run the Nonsense Generator, ensure you have the following installed:

* Download the `.json` file of the google API key that you can generate from Google Cloud Console, in the "Credentials" section of the project.
* Java Development Kit (JDK) 17 or higher. You can check your version by running `java -version` in your terminal.
* Maven 3.6+. Verify your installation with `mvn -v`.

## Installation

Follow these steps to get the project up and running:

1.  **Clone/Download the repository**  

A. 

```bash
    git clone https://github.com/jacopodilauro/Nosense_Generator  
 ```
   ```bash
    cd Nosense_Generator
   ```  
B.   
  (a) Visit the repository page on GitHub: https://github.com/jacopodilauro/Nosense_Generator   
  (b) Click the green ”Code” button, then select ”Download ZIP”.  
  (c) Extract the downloaded ZIP file to a location of your choice on your computer.  
  (d) Open a terminal or command prompt and navigate to the directory of the extracted file (e.g., cd path/to/folder/Nosense Generator or similar, depending on how your system extracts archives).

## Setting Environment Variables

Add the google API key .json file inside the `\src\main\resources\` folder and rename it to **google-key.json**.

In the terminal, ensuring you are in the project's root directory, run the following command:
* **Command prompt - CMD**:
  ```bash
   set GOOGLE_APPLICATION_CREDENTIALS=%CD%\src\main\resources\google-key.json 
  ```
  where %CD% is the path of your current folder.

* **PowerShell**:
  ```bash
  $env:GOOGLE_APPLICATION_CREDENTIALS ="$PWD\src\main\resources\google-key.json" 
  ```
  where $PWD is the path of your current folder.

* **Linux**:
  ```bash
    export GOOGLE_APPLICATION_CREDENTIALS="$PWD/src/main/resources/google-key.json"
  ```

## Run th project

Use:
 ```bash
   mvn spring−boot:run
 ```
Once the application has started successfully (you should see a message similar to Started LanguagewebApplication in *X* seconds in the terminal log), it will be accessible via a web
browser at:
https://localhost:8080



