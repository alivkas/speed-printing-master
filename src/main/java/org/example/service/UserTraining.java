package org.example.service;

import org.example.commons.Time;
import org.example.database.dao.UserDao;
import org.example.database.entity.UserEntity;
import org.hibernate.Session;

/**
 * Класс для управления данными о тренировках пользователей
 */
public class UserTraining {
    public UserDao userDao;

    /**
     * Конструктор UserTraining, который создает объект UserDao
     */
    public UserTraining() {
        this.userDao = new UserDao();
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
        if (username != null) {
            UserEntity user = userDao.getUserByUsername(username, session);
            if (user != null) {
                user.setTrainingCount(user.getTrainingCount() + 1);
                user.setAverageTime(user.getAverageTime() + averageTime);
                session.merge(user);
            }
        }
    }

    /**
     * Сохранить время тренировки пользователя
     * @param time время тренировки
     * @param username имя текущего пользователя
     * @param session текущая сессия
     */
    public void saveUsersTrainingTime(double time, String username, Session session) {
        if (username != null) {
            UserEntity user = userDao.getUserByUsername(username, session);
            if (user != null) {
                user.setTime(time);
                session.merge(user);
            }
        }
    }

    /**
     * Получить время тренировки пользователя
     * @param username имя пользователя
     * @param session текущая сессия
     * @return время тренировки
     */
    public double getUserTrainingTime(String username, Session session) {
        if (username != null) {
            UserEntity user = userDao.getUserByUsername(username, session);
            return user.getTime();
        }
        return 0.0;
    }
}