package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        for (var question: questionDao.findAll()) {
            ioService.printLine(question.text());
            question.answers().forEach(answer -> ioService.printFormattedLine("- %s", answer.text()));
        }
    }
}
