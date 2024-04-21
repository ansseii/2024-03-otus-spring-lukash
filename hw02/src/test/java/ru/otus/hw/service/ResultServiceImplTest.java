package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResultServiceImplTest {

    @Mock
    private IOService ioService;

    @Mock
    private TestConfig testConfiguration;

    @Mock
    private TestResult testResult;

    @InjectMocks
    private ResultServiceImpl resultService;

    @Test
    void showResult_ShouldPrintSuccessMessage_WhenPassTest() {
        when(testResult.getStudent()).thenReturn(new Student("John", "Doe"));
        when(testResult.getRightAnswersCount()).thenReturn(5);
        when(testConfiguration.getRightAnswersCountToPass()).thenReturn(4);

        resultService.showResult(testResult);

        verify(ioService).printLine("Congratulations! You passed test!");
    }

    @Test
    void showResult_ShouldPrintFailMessage_WhenFailTest() {
        when(testResult.getStudent()).thenReturn(new Student("John", "Doe"));
        when(testResult.getRightAnswersCount()).thenReturn(3);
        when(testConfiguration.getRightAnswersCountToPass()).thenReturn(4);

        resultService.showResult(testResult);

        verify(ioService).printLine("Sorry. You fail test.");
    }
}
