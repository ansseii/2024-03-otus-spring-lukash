package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            ioService.printLine(question.text());
            question.answers().forEach(answer -> ioService.printFormattedLine("-- %s", answer.text()));
            var maxOption = question.answers().size();
            int guess = ioService.readIntForRange(1, maxOption,
                    "Incorrect input. Only numbers from 1 to %d are allowed".formatted(maxOption));
            var isAnswerValid = isAnswerValid(question.answers(), guess);
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private boolean isAnswerValid(List<Answer> answers, int guess) {
        return answers.get(guess - 1).isCorrect();
    }
}
