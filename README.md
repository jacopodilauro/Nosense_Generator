# Nonsense Generator

## Introduction
The Nonsense Generator is a Java-based web application designed to create "nonsense" sentences. It achieves this by analyzing user-provided text to extract relevant words (nouns, verbs, and adjectives) and then using these, or randomly selected words from its dictionaries to eventually fill blank spaces, to fill pre-defined sentence templates. The application makes use of the Google Cloud Language API for advanced linguistic analysis, including syntax analysis and content moderation, ensuring generated sentences are within acceptable content boundaries.

## Project Structure

The project is organized into the following directories:

* `src/main/java/com/nonsense/lanquageweb/`: contains all the Java source code for the application.
* `/resources/`: contains application resources.
* `src/test/java/com/nonsense/lanquageweb/`: houses the unit tests to ensure the application's functionality.
* `Documents/`: provides detailed documentation, including User Manual, User Stories, System Test Report, Unit Test, Java Doc (in the `doc` folder).
* `pom.xml`: the Maven Project Object Model file, which manages the project's dependencies and build process.

## Requirements

To set up and run the Nonsense Generator, ensure you have the following installed:

* Java Development Kit (JDK) 17 or higher. You can check your version by running `java -version` in your terminal.
* Maven 3.6+. Verify your installation with `mvn -v`.

## Setting Environment Variables
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

## Installation

Follow these steps to get the project up and running:

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/jacopodilauro/Nosense_Generator
    cd Nosense_Generator
    ```

2.  **Compile the project**:
    This command compiles the Java source code and packages the application.
    ```bash
    mvn spring-boot:run
    ```

3.  **Run tests (optional)**:
    To ensure all components are working as expected, you can execute the unit tests.
    ```bash
    mvn test
    ```
4. **Access the Application**:
   Once the application has started successfully (you should see a message similar to `Started LanguagewebApplication in X seconds` in the terminal log), it will be accessibile via a web browser at:
   ```bash
    https://localhost:8080
    ```


