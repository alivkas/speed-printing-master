package org.example.service;

import org.example.database.SessionManager;
import org.example.database.dao.UserDao;
import org.example.database.entity.UserEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестирование класса UserAuth
 */
public class UserAuthTest {
    private UserDao userDao;
    private UserAuth userAuth;
    private SessionManager sessionManager;

    /**
     * Настраивает тестовую среду перед каждым тестом, создавая тестового пользователя
     */
    @BeforeEach
    void setUp() {
        sessionManager = new SessionManager();
        userDao = new UserDao();
        userAuth = new UserAuth();

        try (Session session = sessionManager.getSession()) {
            Transaction transaction = session.beginTransaction();
            UserEntity testUser = new UserEntity();
            testUser.setUsername("testUser");
            testUser.setPassword("testPassword");
            session.save(testUser);
            transaction.commit();
        }
    }

    /**
     * Очищает тестовую среду после каждого теста, удаляя тестового пользователя
     */
    @AfterEach
    void tearDown() {
        try (Session session = sessionManager.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("DELETE FROM UserEntity WHERE username = 'testUser'").executeUpdate();
            transaction.commit();
        }
    }

    /**
     * Проверяет успешную регистрацию нового пользователя
     */
    @Test
    void testRegisterUserSuccess() {
        try (Session session = sessionManager.getSession()) {
            Transaction transaction = session.beginTransaction();
            boolean result = userAuth.registerUser("testUser2", "anotherPassword", session);
            assertTrue(result);
            transaction.commit();
            UserEntity registeredUser = userDao.getUserByUsername("testUser2", session);
            assertNotNull(registeredUser);
        }
    }

    /**
     * Проверяет неудачную регистрацию пользователя, если пользователь с таким именем уже существует
     */
    @Test
    void testRegisterUserFailure_UserExists() {
        try (Session session = sessionManager.getSession()) {
            Transaction transaction = session.beginTransaction();
            boolean result = userAuth.registerUser("testUser", "anotherPassword", session);
            assertFalse(result);
            transaction.commit();

            UserEntity user = userDao.getUserByUsername("testUser", session);
            assertNotNull(user);
        }
    }

    /**
     * Проверяет успешный вход пользователя в систему
     */
    @Test
    void testLoginUserSuccess() {
        try (Session session = sessionManager.getSession()) {
            boolean result = userAuth.loginUser("testUser", "testPassword", session);
            assertTrue(result);
        }
    }

    /**
     * Проверяет неудачный вход пользователя из-за неправильного пароля
     */
    @Test
    void testLoginUserFailure_IncorrectPassword(){
        try (Session session = sessionManager.getSession()) {
            boolean result = userAuth.loginUser("existingUser", "wrongPassword", session);
            assertFalse(result);
        }
    }
}