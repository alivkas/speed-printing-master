package org.example.service;

import org.example.commons.Time;
import org.example.database.DatabaseManager;
import org.example.database.dao.UserDao;
import org.example.database.entity.UserEntity;
import org.hibernate.Session;

/**
 * Класс для управления данными о тренировках пользователей
 */
public class UserTraining {
    private final DatabaseManager databaseManager;
    public UserDao userDao;

    /**
     * Конструктор UserTraining, который создает объект UserDao и получает ссылку на databaseManager
     * @param databaseManager ссылка на управление бд
     */
    public UserTraining(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.userDao = new UserDao(databaseManager);
    }

    /**
     * Обновляет данные о тренировке в базе данных для указанного пользователя
     * @param username     Имя пользователя
     * @param time         Время тренировки
     * @param totalWords   Все введенные слова
     * @param session сессия базы данных
     */
    public void updateTrainingData(String username,
                                   int time,
                                   int totalWords,
                                   Session session) {
        double averageTime = (double) totalWords / (time / Time.MILLISECONDS);
        addNewTrainingSession(username, time, averageTime, session);
    }

    /**
     * Добавляет новую тренировку в базу данных для заданного пользователя
     *
     * @param username Имя пользователя
     * @param time     Время тренировки
     * @param session сессия базы данных
     */
    private void addNewTrainingSession(String username,
                                          double time,
                                          double averageTime,
                                          Session session) {
        if (username != null) {
            UserEntity user = userDao.getUserByUsername(username);
            if (user != null) {
                user.setTrainingCount(user.getTrainingCount() + 1);
                user.setTime(user.getTime() + time);
                user.setAverageTime(user.getAverageTime() + averageTime);
                session.merge(user);
            }
        }
    }
}