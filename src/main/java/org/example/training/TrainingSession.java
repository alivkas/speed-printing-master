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

    private final int duration;
    private Timer timer;
    private final InputOutput inputOutput;

    /**
     * Конструктор, который инициализирует длительность тренировки
     * и получает ссылку на реализацию InputOutput
     *
     * @param duration длительность тренировки в миллисекундах
     * @param inputOutput вывода информации
     */
    public TrainingSession(int duration, InputOutput inputOutput) {
        this.duration = duration;
        this.inputOutput = inputOutput;
    }

    /**
     * Запускает сессию тренировки с установленным временем
     */
    public void start() {
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
                    logsWriter.writeStackTraceToFile(e);
                    inputOutput.output("Ошибка при работе с роботом.");
                }
                stop();
            }
        }, duration);
        inputOutput.output ("Новая тренировка на " + duration / 60000 + " минут");
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