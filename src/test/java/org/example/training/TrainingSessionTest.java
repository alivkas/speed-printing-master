package org.example.training;

import org.example.interfaces.InputOutput;
import org.example.service.UserTraining;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс для проверки состояния сессии тренировки
 */
public class TrainingSessionTest {
    private TrainingSession trainingSession;
    private InputOutput inputOutputMock;
    private UserTraining userTrainingMock;
    private Session sessionMock;
    private static final double TEST_DURATION_MS = 1000.0;

    /**
     * Инициализация тестовой среды перед каждым тестом
     * Создает моки и экземпляр TrainingSession с замокированным InputOutput и UserTraining
     */
    @BeforeEach
    public void setUp() {
        inputOutputMock = mock(InputOutput.class);
        userTrainingMock = mock(UserTraining.class);
        trainingSession = new TrainingSession(inputOutputMock,userTrainingMock);
        sessionMock = mock(Session.class);

        when(userTrainingMock.getUserTrainingTime("testUser", sessionMock)).thenReturn(TEST_DURATION_MS);
    }

    /**
     * Проверяет, что сессия активна после запуска
     */
    @Test
    public void testStart() {
        trainingSession.start(sessionMock, "testUser");
        assertTrue(trainingSession.isActive());
    }

    /**
     * Проверяет, что сессия становится неактивной по истечении времени
     * Ожидает завершения сессии после заданного времени
     */
    @Test
    public void testSessionEndsAfterDuration() throws InterruptedException {
        trainingSession.start(sessionMock, "testUser");
        Thread.sleep(1000
                + 100);
        assertFalse(trainingSession.isActive());
    }

    /**
     * Проверяет, что сессия останавливается методом stop
     */
    @Test
    public void testStop() {
        trainingSession.start(sessionMock, "testUser");
        trainingSession.stop();
        assertFalse(trainingSession.isActive());
    }
}
