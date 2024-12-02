package org.example.training;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;


/**
 *
 * Тестовый класс для проверки методов установки и получения времени тренировки.
 */
class TrainingSettingsTest {
    private TrainingSettings trainingSettings;

    /**
     * Создаем новый экземпляр TrainingSession для установки времени
     */
    @BeforeEach
    public void setUp() {
        trainingSettings = new TrainingSettings();
    }

    /**
     * Устанавливаем время тренировки и проверяем, что оно правильно сохранено
     */
    @Test
    void checkSetTrainingTimeTest() {
        int expectedTime = 30;
        trainingSettings.setTrainingTime(expectedTime);

        Assertions.assertEquals(expectedTime, trainingSettings.getTrainingTime());
    }

    /**
     * Проверяет, что время тренировки по умолчанию равно 0
     */
    @Test
    void checkGetTrainingTimeTest() {
        Assertions.assertEquals(0, trainingSettings.getTrainingTime());
    }
}