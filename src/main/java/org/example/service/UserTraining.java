package org.example.service;

import org.example.commons.Time;
import org.example.database.dao.UserDao;
import org.example.database.entity.UserEntity;
import org.hibernate.Session;

/**
 * Класс для управления данными о тренировках пользователей
 */
public class UserTraining {

    private final UserDao userDao;

    /**
     * Конструктор UserTraining, который получает ссылку на реализацию InputOutput
     * @param userDao ссылка на объект userDao, взаимодействующий с бд
     */
    public UserTraining(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Обновляет данные о тренировке в базе данных для указанного пользователя
     * @param username     Имя пользователя
     * @param totalWords   Все введенные слова
     * @param session сессия базы данных
     * @param typoCount количество ошибок за тренировку
     */
    public void updateTrainingData(String username,
                                   int totalWords,
                                   int typoCount,
                                   Session session) {
        double trainingTime = getUserTrainingTime(username, session);
        double averageTime = (double) totalWords / (trainingTime / Time.MINUTES_IN_MILLISECONDS);
        addNewTrainingSession(username, averageTime, typoCount, session);
    }

    /**
     * Добавляет новую тренировку в базу данных для заданного пользователя
     *
     * @param username Имя пользователя
     * @param averageTime среднее время тренировки
     * @param session сессия базы данных
     * @param typoCount количество ошибок за тренировку
     */
    private void addNewTrainingSession(String username,
                                       double averageTime,
                                       int typoCount,
                                       Session session) {
        UserEntity user = userDao.getUserByUsername(username, session);
        user.setTrainingCount(user.getTrainingCount() + 1);
        user.setAverageTime(user.getAverageTime() + averageTime);
        user.setSumTypoCount(user.getSumTypoCount() + typoCount);
        session.merge(user);
    }

    /**
     * Сохранить время тренировки пользователя
     * @param time время тренировки
     * @param username имя текущего пользователя
     * @param session текущая сессия
     */
    public void saveUsersTrainingTime(int time, String username, Session session) {
        UserEntity user = userDao.getUserByUsername(username, session);
        user.setTime(time);
        user.setSumTime(user.getTime() + time);
        session.merge(user);
    }

    /**
     * Получить время тренировки пользователя
     * @param username имя пользователя
     * @param session текущая сессия
     * @return время тренировки
     */
    public int getUserTrainingTime(String username, Session session) {
        UserEntity user = userDao.getUserByUsername(username, session);
        return user.getTime();
    }
}