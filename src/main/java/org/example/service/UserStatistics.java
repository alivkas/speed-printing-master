package org.example.service;

import org.apache.log4j.Logger;
import org.example.database.dao.UserDao;
import org.example.database.entity.UserEntity;
import org.example.utils.service.RatingUtils;
import org.hibernate.Session;
import org.example.commons.Time;

import java.util.Comparator;
import java.util.List;

/**
 * Класс для получения статистики пользователя
 */
public class UserStatistics {
    private final Logger logger = Logger.getLogger(UserStatistics.class);
    private final UserDao userDao = new UserDao();
    private final RatingUtils ratingUtils = new RatingUtils();

    /**
     * Получить статистику пользователя по имени
     * @param username имя пользователя
     * @param session текущая сессия
     * @return строка с информацией о пользователе
     */
    public String getUserInfo(String username, Session session) {
        UserEntity user = userDao.getUserByUsername(username,  session);
        return String.format("""
                       Пользователь: %s
                       Рейтинг: %.2f
                       Время тренировок: %d минут
                       Количество тренировок: %d
                       Среднее время слов в минуту: %.2f слов/мин
                       Количество ошибок: %d
                       """,
                user.getUsername(),
                user.getRating(),
                user.getTime() / Time.MINUTES_IN_MILLISECONDS,
                user.getTrainingCount(),
                user.getAverageTime(),
                user.getSumTypoCount());
    }

    /**
     * Получить рейтинг текущего и остальных пользователей
     * @param username текущий пользователь
     * @param session текущая сессия
     * @return рейтинг по всем пользователям
     */
    public String getUsersRating(String username, Session session) {
        StringBuilder info = new StringBuilder();
        List<UserEntity> allUsers = userDao.getAllUsers(session).stream()
                .sorted(Comparator.comparingDouble(UserEntity::getRating).reversed())
                .toList();

        int currentUserRank = findUserRank(allUsers, username);
        UserEntity currentUser = userDao.getUserByUsername(username, session);

        String currentUserInfo = buildUserInfo(currentUser, currentUserRank, username);

        info.append(currentUserInfo);
        info.append("<------------------------------>\n");

        List<UserEntity> topUsers = allUsers.stream()
                .limit(10)
                .toList();

        for (int i = 0; i < topUsers.size(); i++) {
            UserEntity user = topUsers.get(i);
            String userInfo = buildUserInfo(user, i + 1, username);
            info.append(userInfo);
            info.append("<------------------------------>\n");
        }

        return info.toString();
    }

    private int findUserRank(List<UserEntity> users, String username) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                return i + 1;
            }
        }
        return -1;
    }

    private String buildUserInfo(UserEntity user, int rank, String currentUsername)  {
        String rankInfo = rank > 0 ? rank + " место" : "Место отсутствует";
        return String.format("""
                    Пользователь: %s %s
                    %s —> рейтинг %.2f
                    Количество тренировок: %d
                    Количество ошибок: %d
                    Среднее время: %.0f слов/мин
                    """,
                user.getUsername(),
                currentUsername.equals(user.getUsername()) ? "(вы)" : "",
                rankInfo,
                user.getRating(),
                user.getTrainingCount(),
                user.getSumTypoCount(),
                user.getAverageTime());
    }

    /**
     * Сохранить рейтинг пользователя
     * @param username имя пользователя
     * @param session текущая сессия
     */
    public void saveUserRating(String username, Session session) {
        try {
            if (username != null){
                UserEntity user = userDao.getUserByUsername(username, session);
                if (user != null) {
                    double rating = ratingUtils.ratingCalculation(user.getTrainingCount(),
                            user.getSumTypoCount(),
                            user.getAverageTime());
                    user.setRating(rating);
                    session.merge(user);
                }
            }
        } catch (IllegalArgumentException e) {
            logger.error("Имя пользователя не может быть пустым", e);
        }
    }
}