package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TestServiceImpl.class)
class TestServiceImplTest {

    @MockBean
    private QuestionDao questionDao;

    @MockBean
    private LocalizedIOService localizedIOService;

    @Autowired
    private TestService testService;

    @Test
    void givenTestService_WhenExecuteTest_ThenReturnTestResults() {
        when(questionDao.findAll()).thenReturn(getTestQuestions());
        when(localizedIOService.readIntForRange(anyInt(), anyInt(), any())).thenReturn(1);

        var student = getTestStudent();
        var results = testService.executeTestFor(student);

        assertThat(results.getStudent()).isEqualTo(student);
        assertThat(results.getRightAnswersCount()).isEqualTo(1);
        assertThat(results.getAnsweredQuestions()).isEqualTo(getTestQuestions());
    }

    private List<Question> getTestQuestions() {
        return List.of(
                new Question("What is 2 + 2", List.of(
                        new Answer("4", true),
                        new Answer("22", false)
                )),
                new Question("What is the capital of France?", List.of(
                        new Answer("London", false),
                        new Answer("Paris", true)
                ))
        );
    }

    private Student getTestStudent() {
        return new Student("John", "Doe");
    }
}
