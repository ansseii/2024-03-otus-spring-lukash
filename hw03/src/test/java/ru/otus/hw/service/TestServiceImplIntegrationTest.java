package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TestServiceImplIntegrationTest {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private LocalizedMessagesServiceImpl messagesService;

    private TestService testService;

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    @Test
    void executeTestFor_ShouldReturnCorrectResults_WhenAskForQuestions() {
        setupServiceWithUserInput("1\n3\n");

        var student = new Student("John", "Doe");
        var results = testService.executeTestFor(student);

        assertThat(results.getStudent()).isEqualTo(student);
        assertThat(results.getRightAnswersCount()).isEqualTo(1);
    }

    @Test
    void executeTestFor_ShouldPrintCorrectOutput_WhenAskForQuestionsWithCorrectAnswers() throws IOException {
        setupServiceWithUserInput("2\n3\n");
        var resource = resourceLoader.getResource("classpath:expected-output-with-correct-answers.txt");
        var expected = new String(Files.readAllBytes(resource.getFile().toPath()));
        testService.executeTestFor(new Student("John", "Doe"));

        assertThat(out.toString()).hasToString(expected);
    }

    @Test
    void executeTestFor_ShouldPrintCorrectOutput_WhenAskForQuestionsWithWrongAnswers() throws IOException {
        setupServiceWithUserInput("10\n2\n15\n3\n");
        var resource = resourceLoader.getResource("classpath:expected-output-with-incorrect-answers.txt");
        var expected = new String(Files.readAllBytes(resource.getFile().toPath()));
        testService.executeTestFor(new Student("John", "Doe"));

        assertThat(out.toString()).hasToString(expected);
    }

    private void setupServiceWithUserInput(String input) {
        this.testService = new TestServiceImpl(new LocalizedIOServiceImpl(
                messagesService,
                new StreamsIOService(
                        new PrintStream(out),
                        new ByteArrayInputStream(input.getBytes())
                )
        ), questionDao);
    }
}
