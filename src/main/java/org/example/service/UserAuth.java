package org.example.service;

import org.apache.log4j.Logger;
import org.example.database.dao.UserDao;
import org.example.database.entity.UserEntity;
import org.hibernate.Session;

/**
 * Аутентификация пользователя
 */
public class UserAuth {

    private final Logger logger = Logger.getLogger(UserAuth.class);
    private final UserDao userDao;

    /**
     * Конструктор UserAuth, который создает объект UserDao
     */
    public UserAuth() {
        this.userDao = new UserDao();
    }

    /**
     * Создать запись о пользователе в базе данных
     * @param username имя пользователя
     * @param password пароль
     * @param session текущая сессия
     * @return true - пользователь зарегистрирован успешно, false - ошибка регистрации
     */
    public boolean registerUser(String username, String password, Session session) {
        try {
            if (userDao.getUserByUsername(username, session) != null) {
                return false;
            } else {
                UserEntity newUser = new UserEntity();
                newUser.setUsername(username);
                newUser.setPassword(password);
                newUser.setAverageTime(0.0);
                newUser.setTrainingCount(0);
                newUser.setTime(0.0);

                session.save(newUser);
                return true;
            }
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            logger.info(e.getMessage());
        }
        return false;
    }

    /**
     * Авторизовать пользователя в системе
     * @param username имя пользователя
     * @param password пароль
     * @param session текущая сессия
     * @return true - пользователь авторизован успешно, false - ошибка авторизации
     */
    public boolean loginUser(String username, String password, Session session) {
        try {
            UserEntity user = userDao.getUserByUsername(username, session);
            return user != null
                    && user.getPassword().equals(password);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            logger.info(e.getMessage());
        }
        return false;
    }
}
