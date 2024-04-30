package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.TestRunnerService;

@RequiredArgsConstructor
@ShellComponent("Commands for test run")
public class CommandShell {

    private final TestRunnerService testRunnerService;

    @ShellMethod(value = "start", key = {"s", "start"})
    public void beginTest() {
        testRunnerService.run();
    }
}
