package org.example.service;


import org.apache.log4j.Logger;
import org.example.database.dao.UserDao;
import org.example.database.entity.UserEntity;
import org.hibernate.Session;
import org.example.commons.Time;


/**
 * Класс для получения статистики пользователя
 */
public class UserStatistics {

    private final Logger logger = Logger.getLogger(UserStatistics.class);
    private final UserDao userDao = new UserDao();

    /**
     * Получить статистику пользователя по имени
     * @param username имя пользователя
     * @param session текущая сессия
     * @return строка с информацией о пользователе
     */
    public String getUserInfo(String username, Session session) {
        try {
            UserEntity user = userDao.getUserByUsername(username,  session);
            if (user != null) {
                return String.format("""
                       Информация о пользователе:
                       Имя пользователя: %s
                       Время тренировок: %.1f минут
                       Количество тренировок: %d
                       Среднее время слов в минуту: %.2f
                       """,
                        user.getUsername(),
                        user.getTime() / Time.MINUTES_IN_MILLISECONDS,
                        user.getTrainingCount(),
                        user.getAverageTime());
            }
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
        }
        return "Для получения информации о пользователе" +
                " необходимо авторизоваться";
    }
}