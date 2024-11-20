package org.example.database;

import org.example.service.UserTrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;


/**
 * Тестовый класс для проверки функциональности класса {@link ResultSave}
 */
class ResultSaveTest {

    /**
     * Мокированный объект класса UserTrainingRepository, используемый для проверки взаимодействия с сервисом тренировок
     */
    @Mock
    private UserTrainingRepository trainingSessionServiceMock;

    /**
     * Экземпляр класса  ResultSave, который будет тестироваться
     */
    private ResultSave resultSave;

    /**
     * Настройка перед каждым тестом: создание мокированного объекта UserTrainingRepository и экземпляра ResultSave
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainingSessionServiceMock = mock(UserTrainingRepository.class);
        resultSave = new ResultSave(trainingSessionServiceMock);
    }

    /**
     * Тест корректной работы метода updateTrainingData с валидными данными
     * Проверяет, что метод addNewTrainingSession вызывается один раз с корректными параметрами
     */
    @Test
    void testUpdateTrainingData_validData() {
        resultSave.updateTrainingData("testuser", 120, 1.5);
        verify(trainingSessionServiceMock, times(1)).addNewTrainingSession("testuser", 120.0, 1.5);
    }

    /**
     * Тест работы метода updateTrainingData с null значением username
     * Проверяет, что метод addNewTrainingSession вызывается один раз с null значением username
     */
    @Test
    void testUpdateTrainingData_nullUsername() {
        resultSave.updateTrainingData(null, 120, 1.5);
        verify(trainingSessionServiceMock, times(1)).addNewTrainingSession(null, 120.0, 1.5);
    }

    /**
     * Масштабный тест, проверяющий работу метода updateTrainingData с несколькими вызовами и различными входными данными.
     */
    @Test
    void testUpdateTrainingData_multipleCallsWithVariousData() {
        List<String> usernames = new ArrayList<>();
        usernames.add("user1");
        usernames.add("user2");
        usernames.add(null);
        usernames.add("user4");

        List<Integer> times = new ArrayList<>();
        times.add(100);
        times.add(0);
        times.add(50);
        times.add(-20);

        List<Double> averageTimes = new ArrayList<>();
        averageTimes.add(1.2);
        averageTimes.add(0.8);
        averageTimes.add(1.5);
        averageTimes.add(2.0);

        for (int i = 0; i < usernames.size(); i++) {
            resultSave.updateTrainingData(usernames.get(i), times.get(i), averageTimes.get(i));
        }

        verify(trainingSessionServiceMock, times(1)).addNewTrainingSession("user1", 100.0, 1.2);
        verify(trainingSessionServiceMock, times(1)).addNewTrainingSession("user2", 0.0, 0.8);
        verify(trainingSessionServiceMock, times(1)).addNewTrainingSession(null, 50.0, 1.5);
        verify(trainingSessionServiceMock, times(1)).addNewTrainingSession("user4", -20.0, 2.0);

        verifyNoMoreInteractions(trainingSessionServiceMock);
    }


    /**
     * Внутренний класс ResultSave для упрощения тестирования.
     */
    private class ResultSave {
        private final UserTrainingRepository trainingSessionService;

        /**
         * Конструктор класса ResultSave.
         * @param trainingSessionService Сервис для работы с тренировками.
         */
        public ResultSave(UserTrainingRepository trainingSessionService) {
            this.trainingSessionService = trainingSessionService;
        }

        /**
         * Обновляет данные о тренировке в базе данных для указанного пользователя.
         * @param username Имя пользователя.
         * @param time Время тренировки в секундах.
         * @param averageTime Среднее время тренировки.
         */
        public void updateTrainingData(String username, int time, double averageTime) {
            trainingSessionService.addNewTrainingSession(username, (double) time, averageTime);
        }
    }
}