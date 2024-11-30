package org.example.database.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.commons.LogsFile;
import org.example.database.DatabaseManager;
import org.example.database.entity.UserEntity;
import org.example.utils.log.LogsWriterUtils;
import org.hibernate.Session;

/**
 * Класс для работы с пользователями.
 */
public class UserDao {
    private final LogsWriterUtils logsWriter = new LogsWriterUtils(LogsFile.FILE_NAME);
    private final DatabaseManager databaseManager;

    /**
     * Конструктор UserDao, который получает ссылку на databaseManager
     * @param databaseManager ссылка на управление бд
     */
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
            logsWriter.writeStackTraceToFile(new IllegalArgumentException());
            throw new IllegalArgumentException("Имя пользователя не может быть пустым");
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