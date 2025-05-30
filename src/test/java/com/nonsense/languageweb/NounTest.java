// NounTest.java
package com.nonsense.languageweb;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

class NounTest {

    private static final Path RESOURCES_DIR = Paths.get("resources");
    private static final Path ORIGINAL_NOUNS_FILE = RESOURCES_DIR.resolve("nouns.txt");
    private static final Path TEMP_NOUNS_FILE = RESOURCES_DIR.resolve("nouns-temp.txt");

    // Questo flag ci dice se il file nouns.txt originale esisteva prima dell'esecuzione dei test
    private boolean originalNounsFileExisted;

    @BeforeEach
    void setUp() {
        try {
            if (!Files.exists(RESOURCES_DIR)) {
                Files.createDirectories(RESOURCES_DIR);
            }

            // 1. Controlla se il file nouns.txt esiste *prima* di ogni modifica
            originalNounsFileExisted = Files.exists(ORIGINAL_NOUNS_FILE);

            // 2. Se esiste, crea una copia temporanea del suo contenuto originale.
            // Questa sarà la copia da ripristinare alla fine.
            if (originalNounsFileExisted) {
                Files.copy(ORIGINAL_NOUNS_FILE, TEMP_NOUNS_FILE, StandardCopyOption.REPLACE_EXISTING);
            } else {
                // Se il file originale non esiste, crea un file temporaneo vuoto
                // o un file temporaneo con contenuto fittizio se necessario per tearDown.
                // In questo caso, lo creiamo solo per assicurare che esista se copy() dovesse fallire
                // per via di un sorgente non esistente, ma la logica di tearDown lo gestirà.
                // Per chiarezza, se non esiste, non abbiamo nulla da salvare prima del test.
            }

            // 3. Scrivi i dati di test nel file nouns.txt principale.
            // Questo assicura che i test operino su un set di dati prevedibile.
            Files.write(ORIGINAL_NOUNS_FILE, List.of("cane", "gatto", "albero"));

        } catch (IOException e) {
            fail("Errore nel setup: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        try {
            // 1. Cancella il file nouns.txt che è stato usato per i test.
            Files.deleteIfExists(ORIGINAL_NOUNS_FILE);

            // 2. Se il file nouns.txt esisteva prima dei test, ripristinalo dalla copia temporanea.
            if (originalNounsFileExisted) {
                Files.copy(TEMP_NOUNS_FILE, ORIGINAL_NOUNS_FILE, StandardCopyOption.REPLACE_EXISTING);
            }
            // 3. Cancella sempre il file temporaneo.
            Files.deleteIfExists(TEMP_NOUNS_FILE);
        } catch (IOException ignored) {
            // Ignora le eccezioni durante il cleanup per non far fallire i test per problemi di pulizia.
            // In un ambiente di produzione, potresti voler loggare l'errore.
        }
    }

    @Test
    void testGetWordNotNullOrEmpty() throws IOException {
        Noun noun = new Noun(); // Assumiamo che Noun legga da nouns.txt
        String word = noun.getWord();
        assertNotNull(word);
        assertFalse(word.isBlank());
    }

    @Test
    void testToStringEqualsWord() throws IOException {
        Noun noun = new Noun();
        assertEquals(noun.getWord(), noun.toString());
    }
}
