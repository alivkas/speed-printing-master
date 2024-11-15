package org.example.training;

import org.example.TestInputOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;

/**
 * Тестовый класс для проверки состояния сессии
 */
public class TrainingSessionTest {
    private TrainingSession trainingSession;
    private TestInputOutput testInputOutput;
    private TrainingSettings trainingSettings;
    private final int TEST_DURATION_MINUTES = 1;


    /**
     * Настройка тестовой среды перед каждым тестом
     * EST_DURATION_MINUTES/60 для сокращения времени ожидания
     */
    @BeforeEach
    public void setUp() {
        testInputOutput = new TestInputOutput();
        trainingSettings = new TrainingSettings();
        trainingSettings.setTrainingTime(TEST_DURATION_MINUTES/60);
        trainingSession = new TrainingSession(trainingSettings, testInputOutput);
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
    public void
    testSessionEndsAfterDuration() throws InterruptedException {


        trainingSession.start();


        Thread.sleep((TEST_DURATION_MINUTES/60 + 100));
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

    /**
     * Проверяем, что метод isActive возвращает правильное значение
     */
    @Test
    public void testIsActive() throws InterruptedException {
        Assertions.assertFalse(trainingSession.isActive());

        trainingSession.start();

        trainingSession.stop();
        Thread.sleep(1010);
        Assertions.assertFalse(trainingSession.isActive());
    }
}