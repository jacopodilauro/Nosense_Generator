# Nonsense Generator

## Introduction
The Nonsense Generator is a Java-based web application designed to create unique and often humorous "nonsense" sentences. It achieves this by analyzing user-provided text to extract relevant words (nouns, verbs, and adjectives) and then using these, or randomly selected words from its dictionaries, to fill pre-defined sentence templates. The application leverages the Google Cloud Language API for advanced linguistic analysis, including syntax analysis and content moderation, ensuring generated sentences are not only creative but also within acceptable content boundaries.

## Project Structure

The project is organized into the following directories:

* `src/`: Contains all the Java source code for the application.
* `test/`: Houses the unit tests to ensure the application's functionality.
* `Documents/`: Provides detailed documentation, including user manuals and technical specifications.
* `pom.xml`: The Maven Project Object Model file, which manages the project's dependencies and build process.

## Requirements

To set up and run the Nonsense Generator, ensure you have the following installed:

* **Java**: Version 17 or higher. You can check your version by running `java -version` in your terminal.
* **Maven**: Version 3.6 or higher. Verify your installation with `mvn -v`.

## Installation

Follow these steps to get the project up and running:

1.  **Clone the repository**:
    ```bash
    git clone "link_to_your_repository"
    cd Nonsense Generator
    ```
    *(Replace `"link_to_your_repository"` with the actual link to your Git repository.)*

2.  **Compile the project**:
    This command compiles the Java source code and packages the application.
    ```bash
    mvn clean install
    ```

3.  **Run tests (optional)**:
    To ensure all components are working as expected, you can execute the unit tests.
    ```bash
    mvn test
    ```

## Running the Program

The Nonsense Generator can be started in two primary ways:

1.  **Using Maven**:
    This is the recommended method for development and quick execution.
    ```bash
    mvn spring-boot:run
    ```

2.  **Executing the JAR file**:
    After compiling, a JAR file will be generated in the `target/` directory (e.g., `languageweb-0.0.1-SNAPSHOT.jar`). You can run this directly:
    ```bash
    java -jar target/languageweb-0.0.1-SNAPSHOT.jar
    ```
    *(Adjust `languageweb-0.0.1-SNAPSHOT.jar` to the actual name of your generated JAR file if it differs.)*

### Setting Environment Variables (Linux/macOS)

Before running, you might need to set up Google Cloud authentication.

1.  **Download your Google Cloud service account key**: Save it as `google-key.json` (or similar) in a secure location.
2.  **Set the `GOOGLE_APPLICATION_CREDENTIALS` environment variable**:
    ```bash
    export GOOGLE_APPLICATION_CREDENTIALS="/path/to/your/google-key.json"
    ```
    Replace `/path/to/your/google-key.json` with the actual path to your downloaded JSON key file. To make this persistent, add the line to your shell's profile file (e.g., `~/.bashrc`, `~/.zshrc`, or `~/.profile`).

### Setting Environment Variables (Windows)

1.  **Download your Google Cloud service account key**: Save it as `google-key.json` (or similar) in a secure location.
2.  **Set the `GOOGLE_APPLICATION_CREDENTIALS` environment variable**:
    * **Using Command Prompt (temporary)**:
        ```cmd
        set GOOGLE_APPLICATION_CREDENTIALS="C:\path\to\your\google-key.json"
        ```
        Replace `C:\path\to\your\google-key.json` with the actual path to your downloaded JSON key file.
    * **Using PowerShell (temporary)**:
        ```powershell
        $env:GOOGLE_APPLICATION_CREDENTIALS="C:\path\to\your\google-key.json"
        ```
        Replace `C:\path\to\your\google-key.json` with the actual path to your downloaded JSON key file.
    * **Permanently (System Properties)**:
        1.  Right-click "This PC" (or "My Computer") and select "Properties."
        2.  Click "Advanced system settings."
        3.  Click "Environment Variables..."
        4.  Under "System variables," click "New..."
        5.  For "Variable name," enter `GOOGLE_APPLICATION_CREDENTIALS`.
        6.  For "Variable value," enter the full path to your `google-key.json` file (e.g., `C:\Users\YourUser\Downloads\google-key.json`).
        7.  Click "OK" on all windows to apply the changes. You may need to restart your command prompt or PowerShell for the changes to take effect.
