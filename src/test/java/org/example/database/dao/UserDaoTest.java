package org.example.database.dao;

import org.example.database.DatabaseManager;
import org.example.database.entity.UserEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *Тестируем работоспособность класса UserDao
 */
class UserDaoTest {
    private DatabaseManager databaseManager;
    private UserDao userDao;

    /**
     * Настраивает тестовую среду перед каждым тестом
     * Инициализирует объект DatabaseManager и UserDao,
     * а также добавляет тестового пользователя в базу данных
     */
    @BeforeEach
    void setUp() {
        databaseManager = new DatabaseManager();
        userDao = new UserDao();

        try (Session session = databaseManager.getSession()) {
            Transaction transaction = session.beginTransaction();
            UserEntity user = new UserEntity();
            user.setUsername("testUser");
            session.save(user);
            transaction.commit();
        }
    }

    /**
     * Очищает тестовую среду после каждого теста
     * Удаляет всех пользователей из базы данных, чтобы обеспечить чистоту тестов
     */
    @AfterEach
    void tearDown() {
        try (Session session = databaseManager.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("DELETE FROM UserEntity").executeUpdate();
            transaction.commit();

        }
    }

    /**
     * Тестирует метод getUserByUsername
     * Проверяет, что метод возвращает пользователя, если он существует в базе данных
     */
    @Test
    void testGetUserByUsernameReturnsUserWhenExists() {
        UserEntity user = userDao.getUserByUsername("testUser", databaseManager.getSession());
        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
    }

    /**
     * Тестирует метод getUserByUsername
     * Проверяет, что метод возвращает null, если пользователь не найден в базе данных
     */
    @Test
    void testGetUserByUsernameReturnsNullWhenNotExists() {
        UserEntity user = userDao.getUserByUsername("nonExistentUser",databaseManager.getSession());
        assertNull(user);
    }
}