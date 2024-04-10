package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TestRunnerServiceImplTest {
    @Mock
    private TestService testService;

    @InjectMocks
    private TestRunnerServiceImpl testRunnerService;

    @Test
    void runTest() {
        testRunnerService.run();

        verify(testService).executeTest();
    }
}
