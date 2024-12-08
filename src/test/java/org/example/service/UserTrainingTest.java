package org.example.service;

import org.example.commons.Time;
import org.example.database.DatabaseManager;
import org.example.database.entity.UserEntity;
import org.example.database.dao.UserDao;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


/**
 * Тестовый класс для проверки функциональности класса UserTraining
 */
public class UserTrainingTest {
    private DatabaseManager databaseManager;
    private UserDao userDao;
    private UserTraining userTraining;

    /**
     * Настраивает тестовую среду перед каждым тестом, создавая тестового пользователя
     */
    @BeforeEach
    void setUp() {
        databaseManager = new DatabaseManager();
        userDao = new UserDao();
        userTraining = new UserTraining();

        try (Session session = databaseManager.getSession()) {
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
        try (Session session = databaseManager.getSession()) {
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
        try (Session session = databaseManager.getSession()) {
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

        try (Session session = databaseManager.getSession()) {
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
        try (Session session = databaseManager.getSession()) {
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

        try (Session session = databaseManager.getSession()) {
            userTraining.saveUsersTrainingTime(newTime, "nonExistentUser", session);

            UserEntity updatedUser = userDao.getUserByUsername("testUser", session);
            assertNotNull(updatedUser);
            assertEquals(100000.0, updatedUser.getTime());
        }
    }
}