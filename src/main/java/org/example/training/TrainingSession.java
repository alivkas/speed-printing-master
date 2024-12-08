package org.example.training;

import org.example.commons.LogsFile;
import org.example.commons.Time;
import org.example.interfaces.InputOutput;
import org.example.service.UserTraining;
import org.example.utils.log.LogsWriterUtils;
import org.hibernate.Session;

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
    private final UserTraining userTraining = new UserTraining();

    private Timer timer;
    private final InputOutput inputOutput;

    /**
     * Создает сессию тренировки.
     * Инициализирует параметры тренировки и устанавливает
     * статус сессии как неактивную
     *
     * @param inputOutput  Объект для вывода информации.
     */
    public TrainingSession(InputOutput inputOutput) {
        this.inputOutput = inputOutput;
    }

    /**
     * Запускает сессию тренировки с установленным временем
     *
     */
    public void start(Session session, String username) {
        isActive.set(true);
        timer = new Timer();

        int durationMilliseconds = (int) userTraining.getUserTrainingTime(username, session);

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