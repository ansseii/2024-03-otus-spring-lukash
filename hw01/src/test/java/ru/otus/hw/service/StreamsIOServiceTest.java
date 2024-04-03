package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintStream;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StreamsIOServiceTest {
    @Mock
    private PrintStream printStream;

    @InjectMocks
    private StreamsIOService ioService;

    @Test
    void printLineTest() {
        var sample = "Test string";
        ioService.printLine(sample);

        verify(printStream).println(sample);
    }

    @Test
    void printFormattedLine() {
        var sample = "Hello, %s!";
        var args = new Object[] {"World"};

        ioService.printFormattedLine(sample, args);

        verify(printStream).printf(sample + "%n", args);
    }
}
