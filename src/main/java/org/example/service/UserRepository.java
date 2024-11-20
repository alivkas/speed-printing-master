package org.example.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.database.DatabaseManager;
import org.example.database.entity.UserEntity;
import org.hibernate.Session;

/**
 * Класс для работы с пользователями.
 */
public class UserRepository {
    private final DatabaseManager databaseManager;

    public UserRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Получает пользователя по имени пользователя из базы данных.
     *
     * @param username Имя пользователя.
     * @return Пользователь, если найден; иначе null.
     */
    public UserEntity getUserByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
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