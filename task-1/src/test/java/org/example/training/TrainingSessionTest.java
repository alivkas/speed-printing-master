package org.example.training;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import  org.junit.jupiter.api.Assertions;


/**
 * Тестовый класс для проверки состояния сессии
 */
public class TrainingSessionTest {
    private TrainingSession trainingSession;

    /**
     * Создаем новый экземпляр TrainingSession для установки времени
     */
    @BeforeEach
    public void setUp() {
        trainingSession = new TrainingSession(1000);
    }

    /**
     * Проверяем, что сессия активна в течение заданного времени
     */
    @Test
    public void testStart() throws InterruptedException {
        trainingSession.start();
        Assertions.assertTrue(trainingSession.isActive());


        Thread.sleep(1100);
        Assertions.assertFalse(trainingSession.isActive());
    }

    /**
     *Проверяем, что сессия становится неактивной
     */
    @Test
    public void testStop() {
        trainingSession.start();
        Assertions.assertTrue(trainingSession.isActive());

        trainingSession.stop();
        Assertions.assertFalse(trainingSession.isActive());
    }

    /**
     * Проверяем состояние сессии
     */
    @Test
    public void testIsActive() {
        Assertions.assertFalse(trainingSession.isActive());

        trainingSession.start();
        Assertions.assertTrue(trainingSession.isActive());

        trainingSession.stop();
        Assertions.assertFalse(trainingSession.isActive());
    }
}
