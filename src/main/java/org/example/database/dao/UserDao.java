package org.example.database.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.commons.LogsFile;
import org.example.database.DatabaseManager;
import org.example.database.entity.UserEntity;
import org.example.utils.log.LogsWriterUtils;
import org.hibernate.Session;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс для работы с пользователями.
 */
public class UserDao {
    private final Logger logger = Logger.getLogger(UserDao.class.getName());
    private final LogsWriterUtils logsWriter = new LogsWriterUtils(LogsFile.FILE_NAME);
    private final DatabaseManager databaseManager;

    public UserDao(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Получает пользователя по имени пользователя из базы данных.
     *
     * @param username Имя пользователя.
     * @return Пользователь, если найден, иначе null.
     */
    public UserEntity getUserByUsername(String username) {
        if (username == null || username.isEmpty()) {
            logger.log(Level.SEVERE, "Имя пользователя не может быть пустым");
            logsWriter.writeStackTraceToFile(new IllegalArgumentException());
        }

        try (Session session = databaseManager.getSession()) {
            CriteriaBuilder builder = session.getEntityManagerFactory().getCriteriaBuilder();
            CriteriaQuery<UserEntity> criteria = builder.createQuery(UserEntity.class);
            Root<UserEntity> root = criteria.from(UserEntity.class);
            criteria.select(root).where(builder.equal(root.get("username"), username));

            return session.createQuery(criteria).uniqueResult();
        }
    }
}