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
 * Класс для управления данными о тренировках пользователей
 */
public class UserTraining {
    private final Logger logger = Logger.getLogger(UserDao.class.getName());
    private final LogsWriterUtils logsWriter = new LogsWriterUtils(LogsFile.FILE_NAME);
    private final DatabaseManager databaseManager;
    public UserDao userDao;

    /**
     * Конструктор UserTraining, который создает объект UserDao и получает ссылку на databaseManager
     * @param databaseManager ссылка на управление бд
     */
    public UserTraining(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.userDao = new UserDao(databaseManager);
    }

    /**
     * Обновляет данные о тренировке в базе данных для указанного пользователя
     * @param username     Имя пользователя
     * @param time         Время тренировки
     * @param totalWords   Все введенные слова
     */
    public void updateTrainingData(String username, int time, int totalWords) {
        double averageTime = (double) totalWords / time;
        addNewTrainingSession(username, time, averageTime);
    }

    /**
     * Добавляет новую тренировку в базу данных для заданного пользователя
     *
     * @param username Имя пользователя
     * @param time     Время тренировки
     * @return true, если запись добавлена успешно, иначе false
     */
    private boolean addNewTrainingSession(String username, double time, double averageTime) {
        try (Session session = databaseManager.getSession()) {
            session.beginTransaction();
            if (username != null) {
                UserEntity user = userDao.getUserByUsername(username);
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
            logger.log(Level.SEVERE, "Ошибка транзакции");
            logsWriter.writeStackTraceToFile(e);
            return false;
        }
    }
}