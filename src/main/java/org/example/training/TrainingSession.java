package org.example.training;

import org.apache.log4j.Logger;
import org.example.commons.Time;
import org.example.interfaces.InputOutput;
import org.example.service.UserTraining;
import org.hibernate.Session;

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
    private final UserTraining userTraining;

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

        this.userTraining = new UserTraining(inputOutput);
    }

    /**
     * Запускает сессию тренировки с установленным временем
     * @param session текущая сессия
     * @param username имя текущего пользователя
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
                    logger.error(e.getMessage(), e);
                    logger.info("Ошибка при работе с роботом");
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