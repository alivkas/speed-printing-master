package org.example.training;

import org.example.TestInputOutput;
import org.example.interfaces.InputOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Тестовый класс для проверки состояния сессии
 */
public class TrainingSessionTest {
    private TrainingSession trainingSession;
    private TestInputOutput testInputOutput;
    private InputOutput mockInputOutput;
    private final TrainingSettings trainingSettings = new TrainingSettings();
    private static final int TEST_DURATION_MINUTES = 1;

    /**
     * Настройка тестовой среды перед каждым тестом
     * EST_DURATION_MINUTES/60 для сокращения времени ожидания
     */
    @BeforeEach
    public void setUp() {
        testInputOutput = new TestInputOutput();
        trainingSettings.setTrainingTime(TEST_DURATION_MINUTES/60);
        trainingSession = new TrainingSession(trainingSettings, testInputOutput);
    }

    /**
     * Тест на остановку тренировки с ожиданием исключения AWTException
     * Проверяет, что обработка исключения выполнена корректно,
     * а сообщение об ошибке не выводится, если исключение не возникает
     */
    @Test
    void testStopWithErrorHandling_CountDownLatch() {
        mockInputOutput = Mockito.mock(InputOutput.class);
        trainingSession = new TrainingSession( trainingSettings, mockInputOutput);
        CountDownLatch latch = new CountDownLatch(1);
        trainingSettings.setTrainingTime(TEST_DURATION_MINUTES);
        trainingSession.start();

        try {
            latch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Mockito.verify(mockInputOutput, Mockito.times(1)).output("Новая тренировка на 1 минут");
        Mockito.verify(mockInputOutput, Mockito.times(0)).output("Ошибка при работе с роботом.");

        trainingSession.stop();
        Assertions.assertFalse(trainingSession.isActive());
    }

    /**
     * Проверяет, что сессия активна после запуска и выводится сообщение о начале тренировки.
     * В этом тесте устанавливается время тренировки TEST_DURATION_MINUTES минут,
     * но для ускорения тестирования это значение делится на 60, чтобы сократить время ожидания
     * до 1 секунды (т.е. фактически устанавливается 0 минут). Это позволяет быстро проверить,
     * что сессия активна, и выводится сообщение о начале тренировки.
     * После небольшой паузы (1 секунда) проверяется, что сессия становится неактивной
     * и выводится сообщение о завершении тренировки.
     * @throws InterruptedException если поток прерывается во время ожидания
     */
    @Test
    public void testSessionIsActiveAfterStart() throws InterruptedException {
        trainingSettings.setTrainingTime(TEST_DURATION_MINUTES/60);
        trainingSession.start();

        Assertions.assertTrue(trainingSession.isActive());
        Assertions.assertEquals("Новая тренировка на 0" +
                " минут", testInputOutput.getLatestOutput());

        Thread.sleep(1000 + 100);
        Assertions.assertFalse(trainingSession.isActive());
        Assertions.assertEquals("Тренировка Завершена!", testInputOutput.getLatestOutput());
        trainingSession.stop();
    }

    /**
     * Проверяем, что сессия становится неактивной по истечении времени и выводятся сообщение
     */
    @Test
    public void testSessionEndsAfterDuration() throws InterruptedException {
        trainingSession.start();
        Thread.sleep((TEST_DURATION_MINUTES/60 + 100));
        Assertions.assertFalse(trainingSession.isActive());
        Assertions.assertEquals("Тренировка Завершена!", testInputOutput.getLatestOutput());
    }

    /**
     * Проверяем, что сессия останавливается методом stop
     */
    @Test
    public void testSessionActiveAfterBeingStopped() {
        trainingSession.start();
        trainingSession.stop();
        Assertions.assertFalse(trainingSession.isActive());
        Assertions.assertEquals("Тренировка Завершена!", testInputOutput.getLatestOutput());
    }

}