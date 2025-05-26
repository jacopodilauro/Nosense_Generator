package com.nonsense.languageweb;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@AutoConfigureMockMvc
class NonsenseApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void setUpAll() throws IOException {
        Path resourcesDir = Paths.get("resources");
        if (!Files.exists(resourcesDir)) {
            Files.createDirectories(resourcesDir);
        }
        Files.write(resourcesDir.resolve("sentence_templates.txt"),
                Arrays.asList("The {noun} {verb} the {adjective} {noun}"), StandardOpenOption.CREATE);
        Files.write(resourcesDir.resolve("past.txt"), Arrays.asList("go went"), StandardOpenOption.CREATE);
        Files.write(resourcesDir.resolve("nouns.txt"), Collections.emptyList(), StandardOpenOption.CREATE);
        Files.write(resourcesDir.resolve("adjectives.txt"), Collections.emptyList(), StandardOpenOption.CREATE);
        Files.write(resourcesDir.resolve("verbs.txt"), Collections.emptyList(), StandardOpenOption.CREATE);
    }

    @AfterAll
    static void tearDownAll() throws IOException {
        Path resourcesDir = Paths.get("resources");
        if (Files.exists(resourcesDir)) {
            Files.walk(resourcesDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    @BeforeEach
    void setUpEach() {
        // Setup before each test if needed
    }

    @AfterEach
    void tearDownEach() {
        // Cleanup after each test if needed
    }

    @Test
    void homeEndpointReturnsOk() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Sentence Analyzer & Nonsense Generator")));
    }

    @Test
    void addNounInvalidWord() throws Exception {
        mockMvc.perform(post("/addN").param("word", "123"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Invalid word")));
    }

    @Test
    void addNounSuccessAndDuplicate() throws Exception {
        mockMvc.perform(post("/addN").param("word", "TestNoun"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("added successfully")));
        mockMvc.perform(post("/addN").param("word", "TestNoun"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("already in the dictionary")));
    }

    @Test
    void divideByZeroThrowsException() {
        assertThrows(ArithmeticException.class, () -> {
            int x = 1/0;
        });
    }



    @Test
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void timedTestExample() throws InterruptedException {
        Thread.sleep(100);
    }
}
