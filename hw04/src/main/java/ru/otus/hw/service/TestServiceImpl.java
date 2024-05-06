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

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            ioService.printLine(question.text());
            question.answers().forEach(answer -> ioService.printFormattedLine("-- %s", answer.text()));
            var maxOption = question.answers().size();
            int guess = ioService.readIntForRange(1, maxOption,
                    ioService.getMessage("TestService.incorrect.input.error.message", maxOption));
            var isAnswerValid = isAnswerValid(question.answers(), guess);
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private boolean isAnswerValid(List<Answer> answers, int guess) {
        return answers.get(guess - 1).isCorrect();
    }
}
