package org.example.service;


import org.example.database.SessionManager;
import org.example.database.entity.UserEntity;
import org.example.database.dao.UserDao;
import org.example.interfaces.InputOutput;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для проверки функциональности класса UserTraining
 */
public class UserTrainingTest {
    private SessionManager sessionManager;
    private UserDao userDao;
    private UserTraining userTraining;

    @Mock
    private InputOutput inputOutput;

    /**
     * Настраивает тестовую среду перед каждым тестом, создавая тестового пользователя
     */
    @BeforeEach
    void setUp() {
        sessionManager = new SessionManager();
        userDao = new UserDao();
        userTraining = new UserTraining(inputOutput);

        try (Session session = sessionManager.getSession()) {
            Transaction transaction = session.beginTransaction();
            UserEntity testUser = new UserEntity();
            testUser.setUsername("testUser");
            testUser.setTrainingCount(2);
            testUser.setTime(100000.0);
            testUser.setAverageTime(50.0);
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
     * Тестирует успешное обновление данных о тренировке для существующего пользователя
     */
    @Test
    void testUpdateTrainingData_Success() {
        try (Session session = sessionManager.getSession()) {
            int totalWords = 10;
            userTraining.updateTrainingData("testUser", totalWords, session);

            UserEntity updatedUser = userDao.getUserByUsername("testUser", session);
            assertNotNull(updatedUser);
            assertEquals(3, updatedUser.getTrainingCount());
            assertEquals(100000, updatedUser.getTime());
            assertEquals(56.0, updatedUser.getAverageTime());
        }
    }

    /**
     * Тестирует успешное сохранение времени тренировки пользователя
     */
    @Test
    void testSaveUsersTrainingTime_Success() {
        double newTime = 120000.0;

        try (Session session = sessionManager.getSession()) {
            userTraining.saveUsersTrainingTime(newTime, "testUser", session);

            UserEntity updatedUser = userDao.getUserByUsername("testUser", session);
            assertNotNull(updatedUser);
            assertEquals(newTime, updatedUser.getTime());
        }
    }

    /**
     * Тестирует получение времени тренировки пользователя
     */
    @Test
    void testGetUserTrainingTime_Success() {
        try (Session session = sessionManager.getSession()) {
            double trainingTime = userTraining.getUserTrainingTime("testUser", session);

            assertEquals(100000.0, trainingTime);
        }
    }

    /**
     * Тестирует сохранение времени тренировки для несуществующего пользователя
     */
    @Test
    void testSaveUsersTrainingTime_UserNotFound() {
        double newTime = 150000.0;

        try (Session session = sessionManager.getSession()) {
            userTraining.saveUsersTrainingTime(newTime, "nonExistentUser", session);

            UserEntity updatedUser = userDao.getUserByUsername("testUser", session);
            assertNotNull(updatedUser);
            assertEquals(100000.0, updatedUser.getTime());
        }
    }
}