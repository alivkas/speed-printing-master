package org.example.service;

import org.example.database.DatabaseManager;
import org.example.database.entity.UserEntity;
import org.example.interfaces.InputOutput;
import org.example.utils.log.LogsWriterUtils;
import org.hibernate.Session;
import org.hibernate.TransactionException;


/**
 * Класс для управления данными о тренировках пользователей
 */
public class UserTrainingRepository {
    DatabaseManager databaseManager;
    public UserRepository userRepository;


    public UserTrainingRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.userRepository = new UserRepository(databaseManager);
    }

    /**
     * Добавляет новую тренировку в базу данных для заданного пользователя
     *
     * @param username Имя пользователя
     * @param time     Время тренировки
     * @return true, если запись добавлена успешно, иначе false
     */
    public boolean addNewTrainingSession(String username, double time, double averageTime) {
        try (Session session = databaseManager.getSession()) {
            session.beginTransaction();
            if (username != null) {
                UserEntity user = userRepository.getUserByUsername(username);
                if (user != null) {
                    user.setTrainingCount(user.getTrainingCount() + 1);
                    user.setTime(user.getTime() + time);
                    user.setAverageTime(user.getAverageTime() + averageTime);
                    session.merge(user);
                    session.getTransaction().commit();

                    return true;
                }
            }
            return false;
        } catch (TransactionException e) {
            e.printStackTrace();
            return false;
        }
    }
}