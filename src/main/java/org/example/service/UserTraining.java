package org.example.service;

import org.apache.log4j.Logger;
import org.example.commons.Time;
import org.example.database.dao.UserDao;
import org.example.database.entity.UserEntity;
import org.example.interfaces.InputOutput;
import org.hibernate.Session;

/**
 * Класс для управления данными о тренировках пользователей
 */
public class UserTraining {

    private final Logger logger = Logger.getLogger(UserTraining.class);
    private final UserDao userDao;
    private final InputOutput inputOutput;

    /**
     * Конструктор UserTraining, который получает ссылку на реализацию InputOutput
     * @param inputOutput реализация интерфейса InputOutput
     * @param userDao ссылка на объект userDao, взаимодействующий с бд
     */
    public UserTraining(InputOutput inputOutput, UserDao userDao) {
        this.inputOutput = inputOutput;
        this.userDao = userDao;
    }

    /**
     * Обновляет данные о тренировке в базе данных для указанного пользователя
     * @param username     Имя пользователя
     * @param totalWords   Все введенные слова
     * @param session сессия базы данных
     */
    public void updateTrainingData(String username,
                                   int totalWords,
                                   Session session) {
        double trainingTime = getUserTrainingTime(username, session);
        double averageTime = (double) totalWords / (trainingTime / Time.MINUTES_IN_MILLISECONDS);
        addNewTrainingSession(username, averageTime, session);
    }

    /**
     * Добавляет новую тренировку в базу данных для заданного пользователя
     *
     * @param username Имя пользователя
     * @param averageTime среднее время тренировки
     * @param session сессия базы данных
     */
    private void addNewTrainingSession(String username,
                                       double averageTime,
                                       Session session) {
        try {
            if (username != null) {
                UserEntity user = userDao.getUserByUsername(username, session);
                if (user != null) {
                    user.setTrainingCount(user.getTrainingCount() + 1);
                    user.setAverageTime(user.getAverageTime() + averageTime);
                    session.merge(user);
                }
            }
        } catch (IllegalArgumentException e) {
            logger.error("Имя пользователя не может быть пустым", e);
            inputOutput.output("Имя пользователя не может быть пустым");
        }
    }

    /**
     * Сохранить время тренировки пользователя
     * @param time время тренировки
     * @param username имя текущего пользователя
     * @param session текущая сессия
     */
    public void saveUsersTrainingTime(int time, String username, Session session) {
        try {
            if (username != null) {
                UserEntity user = userDao.getUserByUsername(username, session);
                if (user != null) {
                    user.setTime(time);
                    session.merge(user);
                }
            }
        } catch (IllegalArgumentException e) {
            logger.error("Имя пользователя не может быть пустым", e);
            inputOutput.output("Имя пользователя не может быть пустым");
        }
    }

    /**
     * Получить время тренировки пользователя
     * @param username имя пользователя
     * @param session текущая сессия
     * @return время тренировки
     */
    public int getUserTrainingTime(String username, Session session) {
        try {
            if (username != null) {
                UserEntity user = userDao.getUserByUsername(username, session);
                return user.getTime();
            }
        } catch (IllegalArgumentException e) {
            logger.error("Имя пользователя не может быть пустым", e);
            inputOutput.output("Имя пользователя не может быть пустым");
        }
        return 0;
    }
}