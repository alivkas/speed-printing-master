package org.example.service;

import org.example.database.SessionManager;
import org.example.database.entity.UserEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Тестовый класс для UserStatistics
 */
public class UserStatisticsTest {
    private SessionManager sessionManager;
    private UserStatistics userStatistics;

    /**
     * Настраивает тестовую среду перед каждым тестом, создавая тестового пользователя
     */
    @BeforeEach
    void setUp()  {
        sessionManager = new SessionManager();
        userStatistics = new UserStatistics();

        try (Session session = sessionManager.getSession()) {
            Transaction transaction = session.beginTransaction();
            UserEntity testUser = new UserEntity();
            testUser.setUsername("testUser");
            testUser.setTime(180000.0);
            testUser.setTrainingCount(3);
            testUser.setAverageTime(60.0);
            session.save(testUser);
            transaction.commit();
        }
    }

    /**
     * Очищает тестовую среду после каждого теста, удаляя тестового пользователя
     */
    @AfterEach
    void tearDown()  {
        try (Session session = sessionManager.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("DELETE FROM UserEntity WHERE username = 'testUser'").executeUpdate();
            transaction.commit();
        }
    }

    /**
     * Проверяет получение информации о существующем пользователе
     */
    @Test
    void testGetUserInfo_UserExists() {
        try (Session session = sessionManager.getSession()) {
            String userInfo = userStatistics.getUserInfo("testUser", session);
            String expectedInfo = String.format("""
                       Информация о пользователе:
                       Имя пользователя: testUser
                       Время тренировок: 3,0 минут
                       Количество тренировок: 3
                       Среднее время слов в минуту: 60,00
                       """);
            assertEquals(expectedInfo, userInfo);
        }
    }

    /**
     * Проверяет получение сообщения об отсутствии пользователя
     */
    @Test
    void testGetUserInfo_UserDoesNotExist() {
        try (Session session = sessionManager.getSession()) {
            String userInfo = userStatistics.getUserInfo("nonExistingUser", session);
            assertEquals("Для получения информации о пользователе необходимо авторизоваться", userInfo);
        }
    }
}