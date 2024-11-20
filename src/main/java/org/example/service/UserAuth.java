package org.example.service;

import org.example.database.DatabaseManager;
import org.example.database.entity.UserEntity;
import org.example.interfaces.InputOutput;
import org.hibernate.Session;

/**
 * Аутентификация пользователя
 */
public class UserAuth {

    private final InputOutput inputOutput;
    private String username;

    /**
     * Конструктор UserAuth, который хранит ссылку на инициализацию реализации InputOutput
     * @param inputOutput реализация inputOutput
     */
    public UserAuth(InputOutput inputOutput) {
        this.inputOutput = inputOutput;
    }

    /**
     * Создать запись о пользователе в базе данных
     * @return true - пользователь зарегистрирован успешно, false - ошибка регистрации
     */
    public boolean registerUser(DatabaseManager databaseManager) {
        inputOutput.output("Введите имя пользователя: ");
        String username = inputOutput.input();
        inputOutput.output("Введите пароль: ");
        String password = inputOutput.input();

        try (Session session = databaseManager.getSession()) {
            session.beginTransaction();

            UserEntity existingUser = session.createQuery("from UserEntity where username = :username", UserEntity.class)
                    .setParameter("username", username)
                    .uniqueResult();

            if (existingUser != null) {
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
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Проверить наличие пользователя в базе данных из текущей сессии
     * @return true - пользователь авторизован успешно, false - ошибка авторизации
     */
    public boolean loginUser(DatabaseManager databaseManager) {
        inputOutput.output("Введите имя пользователя: ");
        String username = inputOutput.input();
        inputOutput.output("Введите пароль: ");
        String password = inputOutput.input();

        try (Session session = databaseManager.getSession()) {
            session.beginTransaction();

            UserEntity user = session.createQuery("from UserEntity where username = :username", UserEntity.class)
                    .setParameter("username", username)
                    .uniqueResult();

            if (user != null && user.getPassword().equals(password)) {
                this.username = username;
                session.getTransaction().commit();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getUsername() {
        return username;
    }
}
