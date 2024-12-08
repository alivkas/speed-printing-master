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
     * @param averageTime среднее время тренировки
     * @param session сессия базы данных
     */
    private void addNewTrainingSession(String username,
                                          double time,
                                          double averageTime,
                                          Session session) {
        if (username != null) {
            UserEntity user = userDao.getUserByUsername(username, session);
            if (user != null) {
                user.setTrainingCount(user.getTrainingCount() + 1);
                user.setTime(user.getTime() + time);
                user.setAverageTime(user.getAverageTime() + averageTime);
                session.merge(user);

                System.out.println(user.getUsername());
                System.out.println(user.getTime());
                System.out.println(user.getAverageTime());
                System.out.println(user.getTrainingCount());
            }
        }
    }
}