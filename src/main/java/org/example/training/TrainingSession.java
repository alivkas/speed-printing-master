package org.example.training;

import org.example.commons.LogsFile;
import org.example.commons.Time;
import org.example.interfaces.InputOutput;
import org.example.utils.log.LogsWriterUtils;
import org.example.web.FishTextApi;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Управление сессией тренировки
 */
public class TrainingSession {
    private final LogsWriterUtils logsWriter = new LogsWriterUtils(LogsFile.FILE_NAME);
    private final AtomicBoolean isActive = new AtomicBoolean(false);
    private final Logger logger = Logger.getLogger(TrainingSession.class.getName());

    private Timer timer;
    private final InputOutput inputOutput;
    private final TrainingSettings settings;

    /**
     * Создает сессию тренировки.
     * Инициализирует параметры тренировки и устанавливает
     * статус сессии как неактивную
     *
     * @param settings Параметры тренировки
     * @param inputOutput  Объект для вывода информации.
     */
    public TrainingSession(TrainingSettings settings, InputOutput inputOutput) {
        this.settings = settings;
        this.inputOutput = inputOutput;
    }

    /**
     * Запускает сессию тренировки с установленным временем
     *
     */
    public void start() {
        isActive.set(true);
        timer = new Timer();

        int durationMilliseconds = settings.getTrainingTime();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Robot robot = new Robot();
                    robot.keyPress(KeyEvent.VK_ENTER);
                    robot.keyRelease(KeyEvent.VK_ENTER);
                } catch (AWTException e) {
                    logsWriter.writeStackTraceToFile(e);
                    logger.log(Level.SEVERE, "Ошибка при работе с роботом");
                }
                stop();
            }
        }, durationMilliseconds);
        inputOutput.output ("Новая тренировка на " + settings.getTrainingTime() / Time.MILLISECONDS
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