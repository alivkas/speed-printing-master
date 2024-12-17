package org.example.training;

import org.example.interfaces.InputOutput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс для проверки состояния сессии тренировки
 */
public class TrainingSessionTest {
    private final TrainingSession trainingSession;
    private static final int TEST_DURATION_MS = 1000;

    /**
     * Инициализация тестовой среды перед каждым тестом
     * Создает моки и экземпляр TrainingSession с замокированным InputOutput
     */
    public TrainingSessionTest() {
        InputOutput inputOutputMock = mock(InputOutput.class);
        trainingSession = new TrainingSession(inputOutputMock);
    }

    /**
     * Проверяет, что сессия активна после запуска
     */
    @Test
    public void testStart() {
        trainingSession.start(TEST_DURATION_MS);
        assertTrue(trainingSession.isActive());
    }

    /**
     * Проверяет, что сессия становится неактивной по истечении времени
     * Ожидает завершения сессии после заданного времени
     */
    @Test
    public void testSessionEndsAfterDuration() throws InterruptedException {
        trainingSession.start(TEST_DURATION_MS);
        Thread.sleep(1000
                + 100);
        assertFalse(trainingSession.isActive());
    }

    /**
     * Проверяет, что сессия останавливается методом stop
     */
    @Test
    public void testStop() {
        trainingSession.start(TEST_DURATION_MS);
        trainingSession.stop();
        assertFalse(trainingSession.isActive());
    }
}
