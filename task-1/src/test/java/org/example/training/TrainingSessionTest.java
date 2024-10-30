package org.example.training;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


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
        assertTrue(trainingSession.isActive());


        Thread.sleep(1100);
        assertFalse(trainingSession.isActive());
    }

    /**
     *Проверяем, что сессия становится неактивной
     */
    @Test
    public void testStop() {
        trainingSession.start();
        assertTrue(trainingSession.isActive());

        trainingSession.stop();
        assertFalse(trainingSession.isActive());
    }

    /**
     * Проверяем состояние сессии
     */
    @Test
    public void testIsActive() {
        assertFalse(trainingSession.isActive());

        trainingSession.start();
        assertTrue(trainingSession.isActive());

        trainingSession.stop();
        assertFalse(trainingSession.isActive());
    }
}
