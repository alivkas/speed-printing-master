package org.example.service;

import org.example.commons.LogsFile;
import org.example.database.DatabaseManager;
import org.example.database.dao.UserDao;
import org.example.database.entity.UserEntity;
import org.example.utils.log.LogsWriterUtils;
import org.hibernate.Session;
import org.hibernate.TransactionException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Аутентификация пользователя
 */
public class UserAuth {
    private final Logger logger = Logger.getLogger(UserDao.class.getName());
    private final LogsWriterUtils logsWriter = new LogsWriterUtils(LogsFile.FILE_NAME);

    private final DatabaseManager databaseManager;
    private final UserDao userDao;

    private String username;

    /**
     * Конструктор UserAuth, который создает объект UserDao и получает ссылку на databaseManager
     * @param databaseManager ссылка на управление бд
     */
    public UserAuth(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.userDao = new UserDao(databaseManager);
    }

    /**
     * Создать запись о пользователе в базе данных
     * @param username имя пользователя
     * @param password пароль
     * @return true - пользователь зарегистрирован успешно, false - ошибка регистрации
     */
    public boolean registerUser(String username, String password) {
        try (Session session = databaseManager.getSession()) {
            session.beginTransaction();

            if (userDao.getUserByUsername(username) != null) {
                return false;
            } else {
                UserEntity newUser = new UserEntity();
                newUser.setUsername(username);
                newUser.setPassword(password);
                newUser.setAverageTime(0.0);
                newUser.setTrainingCount(0);
                newUser.setTime(0.0);

                session.save(newUser);
                session.getTransaction().commit();
                return true;
            }
        } catch (TransactionException e) {
            logger.log(Level.SEVERE, "Ошибка транзакции");
            logsWriter.writeStackTraceToFile(e);
            return false;
        }
    }

    /**
     * Проверить наличие пользователя в базе данных из текущей сессии
     * @param username имя пользователя
     * @param password пароль
     * @return true - пользователь авторизован успешно, false - ошибка авторизации
     */
    public boolean loginUser(String username, String password) {
        try (Session session = databaseManager.getSession()) {
            session.beginTransaction();
            UserEntity user = userDao.getUserByUsername(username);

            if (user != null
                    && user.getPassword().equals(password)) {
                this.username = username;
                session.getTransaction().commit();
                return true;
            } else {
                return false;
            }
        } catch (TransactionException e) {
            logger.log(Level.SEVERE, "Ошибка транзакции");
            logsWriter.writeStackTraceToFile(e);
            return false;
        }
    }

    public String getUsername() {
        return username;
    }
}
