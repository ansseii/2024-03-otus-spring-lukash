package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ResultServiceImpl.class)
class ResultServiceImplTest {

    @MockBean
    private LocalizedIOService ioService;

    @MockBean
    private TestConfig testConfiguration;

    @MockBean
    private TestResult testResult;

    @Autowired
    private ResultServiceImpl resultService;

    @Test
    @DisplayName("Test show result functionality when test passed")
    void givenResultService_WhenPassedTest_ThenPrintSuccessMessage() {
        when(testResult.getStudent()).thenReturn(new Student("John", "Doe"));
        when(testResult.getRightAnswersCount()).thenReturn(5);
        when(testConfiguration.getRightAnswersCountToPass()).thenReturn(4);

        resultService.showResult(testResult);

        verify(ioService).printLineLocalized("ResultService.passed.test");
    }

    @Test
    @DisplayName("Test show result functionality when test failed")
    void givenResultService_WhenFailedTest_ThenPrintFailureMessage() {
        when(testResult.getStudent()).thenReturn(new Student("John", "Doe"));
        when(testResult.getRightAnswersCount()).thenReturn(3);
        when(testConfiguration.getRightAnswersCountToPass()).thenReturn(4);

        resultService.showResult(testResult);

        verify(ioService).printLineLocalized("ResultService.fail.test");
    }
}
