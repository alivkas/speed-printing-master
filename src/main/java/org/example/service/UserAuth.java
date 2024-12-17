package org.example.service;

import org.example.database.dao.UserDao;
import org.example.database.entity.UserEntity;
import org.hibernate.Session;

/**
 * Аутентификация пользователя
 */
public class UserAuth {

    private final UserDao userDao;

    /**
     * Конструктор UserAuth, который получает ссылку на реализацию InputOutput
     * @param userDao ссылка на объект userDao, взаимодействующий с бд
     */
    public UserAuth(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Создать запись о пользователе в базе данных
     * @param username имя пользователя
     * @param password пароль
     * @param session текущая сессия
     * @return true - пользователь зарегистрирован успешно, false - ошибка регистрации
     */
    public boolean registerUser(String username, String password, Session session) {
        if (userDao.getUserByUsername(username, session) == null) {
            UserEntity newUser = new UserEntity();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setAverageTime(0.0);
            newUser.setTrainingCount(0);
            newUser.setTime(0);
            newUser.setSumTypoCount(0);
            newUser.setRating(0.0);

            session.save(newUser);
            return true;
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
        UserEntity user = userDao.getUserByUsername(username, session);
        return user != null
                && user.getPassword().equals(password);
    }
}
