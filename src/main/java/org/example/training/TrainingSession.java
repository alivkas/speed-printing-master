package org.example.training;

import org.example.interfaces.InputOutput;
import org.example.utils.log.LogsWriterUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * Управление сессией тренировки
 */
public class TrainingSession {
    private final LogsWriterUtils logsWriter = new LogsWriterUtils();
    private final AtomicBoolean isActive = new AtomicBoolean(false);
    private final int secondInMinute = 60;
    private final int millisecondsInSecond = 1000;

    private final int duration;
    private Timer timer;
    private final InputOutput inputOutput;

    /**
     * Создает сессию тренировки
     * Инициализирует параметры тренировки и устанавливает
     * статус сессии как неактивную
     *
     * @param settings Параметры тренировки
     * @param inputOutput  Объект для вывода информации.
     */
    public TrainingSession(TrainingSettings settings, InputOutput inputOutput) {
        this.settings = settings;
        this.output = inputOutput;
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
                    logsWriter.writeStackTraceToFile(e);
                    inputOutput.output("Ошибка при работе с роботом.");
                }
                stop();
            }
        }, durationMilliseconds);
        inputOutput.output ("Новая тренировка на " + settings.getTrainingTime() + " минут");
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