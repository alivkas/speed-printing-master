package org.example.service;

import org.example.database.dao.UserDao;
import org.example.database.entity.UserEntity;
import org.example.utils.service.UserRatingUtils;
import org.hibernate.Session;

import java.util.List;

/**
 * Класс работы со статистикой пользователя
 */
public class UserStatistics {

    private final static int TOP_LIMIT = 10;

    private final UserDao userDao = new UserDao();
    private final UserRatingUtils userRatingUtils = new UserRatingUtils();

    /**
     * Получить статистику пользователя по имени
     * @param username имя пользователя
     * @param session текущая сессия
     * @return строка с информацией о пользователе
     */
    public String getUserInfo(String username, Session session) {
        UserEntity user = userDao.getUserByUsername(username,  session);
        List<UserEntity> users = userDao.getAllUsers(session);
        int userRank = userRatingUtils.findUserRank(users, username);
        return userRatingUtils.formatUserInfo(user, username, userRank);
    }

    /**
     * Получить рейтинг пользователей
     * @param username имя пользователя
     * @param session текущая сессия
     * @return топ пользователей по возрастанию рейтинга
     */
    public String getUsersRating(String username, Session session) {
        List<UserEntity> allUsers = userDao.getAllUsers(session);
        List<UserEntity> topUsers = userRatingUtils.getTopUsers(allUsers, TOP_LIMIT);
        UserEntity currentUser = userDao.getUserByUsername(username, session);
        int currentUserRank = userRatingUtils.findUserRank(topUsers, username);
        return userRatingUtils.buildUsersRating(topUsers, currentUserRank, currentUser, username);
    }

    /**
     * Сохранить рейтинг пользователя
     * @param username имя пользователя
     * @param session текущая сессия
     */
    public void saveUserRating(String username, Session session) {
        UserEntity user = userDao.getUserByUsername(username, session);
        if (user != null) {
            double rating = userRatingUtils.ratingCalculation(user.getTrainingCount(),
                    user.getSumTypoCount(),
                    user.getAverageTime());
            user.setRating(rating);
            session.merge(user);
        }
    }

    /**
     * Сбросить статистику пользователя
     * @param username имя текущего пользователя
     * @param session текущая сессия
     */
    public void resetUserStatistic(String username, Session session) {
        UserEntity user = userDao.getUserByUsername(username, session);
        user.setTrainingCount(0);
        user.setAverageTime(0.0);
        user.setTime(0);
        user.setSumTypoCount(0);
        user.setSumTime(0);
        user.setRating(0.0);
        session.merge(user);
    }
}