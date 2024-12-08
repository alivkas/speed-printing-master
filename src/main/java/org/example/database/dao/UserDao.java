package org.example.database.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.database.entity.UserEntity;
import org.hibernate.Session;

/**
 * Класс для работы с пользователями.
 */
public class UserDao {

    /**
     * Получает пользователя по имени пользователя из базы данных.
     *
     * @param username Имя пользователя.
     * @param session текущая сессия
     * @return Пользователь, если найден, иначе null.
     */
    public UserEntity getUserByUsername(String username, Session session) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым");
        }

        CriteriaBuilder builder = session.getEntityManagerFactory().getCriteriaBuilder();
        CriteriaQuery<UserEntity> criteria = builder.createQuery(UserEntity.class);
        Root<UserEntity> root = criteria.from(UserEntity.class);
        criteria.select(root).where(builder.equal(root.get("username"), username));

        return session.createQuery(criteria).uniqueResult();
    }
}