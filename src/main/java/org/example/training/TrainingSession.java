package org.example.training;

import org.apache.log4j.Logger;
import org.example.commons.Time;
import org.example.interfaces.InputOutput;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Управление сессией тренировки
 */
public class TrainingSession {
    private final Logger logger = org.apache.log4j.Logger.getLogger(TrainingSession.class);
    private final AtomicBoolean isActive = new AtomicBoolean(false);
    private Timer timer;
    private final InputOutput inputOutput;

    /**
     * Конструктор TrainingSession, который инициализирует UserTraining
     * и получает ссылку на реализацию InputOutput
     *
     * @param inputOutput реализация интерфейса InputOutput
     */
    public TrainingSession(InputOutput inputOutput) {
        this.inputOutput = inputOutput;
    }

    /**
     * Запускает сессию тренировки с установленным временем
     * @param durationMilliseconds длительность в миллисекундах
     */
    public void start(int durationMilliseconds) {
        isActive.set(true);
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Robot robot = new Robot();
                    robot.keyPress(KeyEvent.VK_ENTER);
                    robot.keyRelease(KeyEvent.VK_ENTER);
                } catch (AWTException e) {
                    logger.error("Ошибка при работе с роботом", e);
                    inputOutput.output("Ошибка при работе с роботом");
                }
                stop();
            }
        }, durationMilliseconds);
        inputOutput.output ("Новая тренировка на " + durationMilliseconds / Time.MINUTES_IN_MILLISECONDS
                + " минут");
    }

    /**
     * Останавливает и завершает текущую сессию тренировки
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
            isActive.set(false);
            inputOutput.output("Тренировка Завершена!");
        }
    }

    /**
     * Проверяет, активна ли сессия тренировки
     *
     * @return true, если сессия активна, иначе false
     */
    public boolean isActive() {
        return isActive.get();
    }
}