package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {
    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;

    @InjectMocks
    private TestServiceImpl testService;

    @BeforeEach
    void setUp() {
        when(questionDao.findAll()).thenReturn(List.of(
                new Question("What is 2 + 2", List.of(
                        new Answer("4", true),
                        new Answer("22", false)
                )),
                new Question("What is the capital of France?", List.of(
                        new Answer("London", false),
                        new Answer("Paris", true)
                ))
        ));
    }

    @Test
    void executeTest() {
        testService.executeTest();

        verify(ioService).printLine("");
        verify(ioService).printFormattedLine("Please answer the questions below%n");
        verify(ioService).printLine("What is 2 + 2");
        verify(ioService).printFormattedLine("- %s", "4");
        verify(ioService).printFormattedLine("- %s", "22");
        verify(ioService).printLine("What is the capital of France?");
        verify(ioService).printFormattedLine("- %s", "Paris");
        verify(ioService).printFormattedLine("- %s", "London");

        verify(questionDao).findAll();
    }
}
