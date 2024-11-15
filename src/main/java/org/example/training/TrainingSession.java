package org.example.training;

import org.example.interfaces.InputOutput;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * Управление сессией тренировки
 * lasting длительность тренировки в миллисекундах
 */
public class TrainingSession {
    private final TrainingSettings settings;
    private Timer timer;
    private final AtomicBoolean isActive;
    private final InputOutput output;
    private final int secondInMinute = 60;
    private final int millisecondsInSecond = 1000;

    /**
     * Конструктор, который инициализирует длительность тренировки и устанавливает статус сессии как неактивную
     *
     * @param output  вывода информации
     */
    public TrainingSession(TrainingSettings settings, InputOutput output) {
        this.settings = settings;
        this.output = output;
        this.isActive = new AtomicBoolean(false);
    }



    /**
     * Запускает сессию тренировки с установленным временем
     */
    public void start() {
        isActive.set(true);
        timer = new Timer();

        int durationMilliseconds = settings.getTrainingTime() * secondInMinute * millisecondsInSecond;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Robot robot = new Robot();
                    robot.keyPress(KeyEvent.VK_ENTER);
                    robot.keyRelease(KeyEvent.VK_ENTER);
                } catch (AWTException e) {
                    Logger.getLogger(TrainingSession.class.getName()).warning(e.getMessage());
                    output.output("Ошибка при работе с роботом.");
                }
                stop();
            }
        }, durationMilliseconds);
        output.output ("Новая тренировка на " + settings.getTrainingTime() + " минут");
    }

    /**
     * Останавливает и завершает текущую сессию тренировки
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
            isActive.set(false);
            output.output("Тренировка Завершена!");
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