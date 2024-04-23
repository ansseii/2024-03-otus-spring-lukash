package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StreamsIOServiceTest {
    @Mock
    private PrintStream out;

    private StreamsIOService ioService;

    private final InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());

    @BeforeEach
    void setUp() {
        ioService = new StreamsIOService(out, in);
    }

    @Test
    void shouldPrintTestStringLine() {
        var sample = "Test string";
        ioService.printLine(sample);

        verify(out).println(sample);
    }

    @Test
    void shouldPrintTestFormattedStringLine() {
        var sample = "Hello, %s!";
        var args = new Object[]{"World"};

        ioService.printFormattedLine(sample, args);

        verify(out).printf(sample + "%n", args);
    }

    @Test
    void shouldReadStringFromStream() {
        assertThat(ioService.readString()).isEqualTo("Hello, World!");
    }

    @Test
    void shouldReadStringWithPrompt() {
        var prompt = "Please, enter something";
        var result = ioService.readStringWithPrompt(prompt);

        verify(out).println(prompt);
        assertThat(result).isEqualTo("Hello, World!");
    }

    @Test
    void shouldReadIntFromStreamWithCorrectRange() {
        ioService = new StreamsIOService(out, new ByteArrayInputStream("5".getBytes()));
        int result = ioService.readIntForRange(0, 10, "Error");

        assertThat(result).isEqualTo(5);
    }

    @Test
    void shouldReadIntFromStreamWithPromptAndWithCorrectRange() {
        ioService = new StreamsIOService(out, new ByteArrayInputStream("5".getBytes()));
        int result = ioService.readIntForRangeWithPrompt(0, 10, "Please enter something:", "Error");

        verify(out).println("Please enter something:");
        assertThat(result).isEqualTo(5);
    }

    @Test
    void shouldPrintErrorMessageWhenReadIntFromStreamWithIncorrectRange() {
        ioService = new StreamsIOService(out, new ByteArrayInputStream("5\n2\n".getBytes()));

        int result = ioService.readIntForRange(0, 4, "Error message");

        verify(out).println("Error message");
        assertThat(result).isEqualTo(2);
    }

    @Test
    void shouldThrowExceptionWhenReadIntFromStream() {
        var values = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        var body = String.join("\n", values);
        ioService = new StreamsIOService(out, new ByteArrayInputStream(body.getBytes()));

        assertThatThrownBy(() -> {
        for (int i = 0; i < values.length; i++) {
            ioService.readIntForRange(100, 101, "Error message");
        }
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Error during reading int value");
    }
}
