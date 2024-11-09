package org.example.training;

import org.example.interfaces.InputOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;


/**
 * Тестовый класс для проверки состояния сессии
 */
public class TrainingSessionTest {
    private TrainingSession trainingSession;
    private TestInputOutput testInputOutput;

    private static final int TEST_DURATION_MS = 1000;

    /**
     * Создаем новый экземпляр TrainingSession для установки времени
     */
    @BeforeEach
    public void setUp() {
        testInputOutput = new TestInputOutput();
        trainingSession = new TrainingSession(TEST_DURATION_MS, testInputOutput);
    }

    /**
     * Проверяем, что сессия активна после запуска
     */
    @Test
    public void testStart() {
        trainingSession.start();
        Assertions.assertTrue(trainingSession.isActive());
    }

    /**
     * Проверяем, что сессия становится неактивной по истечении времени
     */
    @Test
    public void testSessionEndsAfterDuration() throws InterruptedException {
        trainingSession.start();
        Thread.sleep(TEST_DURATION_MS + 100);
        Assertions.assertFalse(trainingSession.isActive());
    }

    /**
     * Проверяем, что сессия останавливается методом stop
     */
    @Test
    public void testStop() {
        trainingSession.start();
        trainingSession.stop();
        Assertions.assertFalse(trainingSession.isActive());
    }

    /* /**
     * Проверяем, что метод isActive возвращает правильное значение
     */
    @Test
    public void testIsActive() throws InterruptedException {
        Assertions.assertFalse(trainingSession.isActive());

        trainingSession.start();
        Assertions.assertTrue(trainingSession.isActive());

        trainingSession.stop();
        Thread.sleep(1010);
        Assertions.assertFalse(trainingSession.isActive());
    }

    private static class TestInputOutput implements InputOutput {
        private String input;
        private String latestOutput;


        @Override
        public void output(String message) {
            latestOutput = message;
        }

        @Override


        public String input() {
            return input;
        }

    }
}