package org.example.training;

import org.example.TestInputOutput;
import org.example.database.SessionManager;



import org.example.database.entity.UserEntity;
import org.example.service.UserTraining;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для проверки состояния сессии
 */
public class TrainingSessionTest   {
    private TrainingSession trainingSession;
    private TestInputOutput testInputOutput;
    private SessionManager  sessionManager;
    private UserTraining userTraining;
    private Session session;
    private final String testUsername = "testUser";

    /**
     * Настраивает тестовую среду перед каждым тестом, создавая тестового пользователя
     */
    @BeforeEach
    public void setUp()  {
        testInputOutput = new TestInputOutput();
        trainingSession = new TrainingSession(testInputOutput);
        sessionManager = new SessionManager();
        userTraining = new UserTraining();
        session = sessionManager.getSession();


        session.beginTransaction();
        UserEntity testUser = new UserEntity();
        testUser.setUsername(testUsername);
        testUser.setPassword("testPassword");
        testUser.setTime(0.0);
        session.save(testUser);
        session.getTransaction().commit();
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
     * Проверяет,что сессия активна после запуска и выводится сообщение о начале тренировки
     */
    @Test
    public void testSessionIsActiveAfterStart() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        new Thread(() -> {
            trainingSession.start(session, testUsername);
            latch.countDown();
        }).start();

        latch.await();

        assertTrue(trainingSession.isActive());
        assertEquals("Новая тренировка на 0 минут", testInputOutput.getLatestOutput());

        trainingSession.stop();
        assertFalse(trainingSession.isActive());
        assertEquals("Тренировка Завершена!", testInputOutput.getLatestOutput());
    }

    /**
     * Проверяем, что сессия становится неактивной после завершения времени тренировки
     */
    @Test
    public void testSessionEndsAfterDuration() throws InterruptedException {
        userTraining.saveUsersTrainingTime(1000,testUsername, session);

        CountDownLatch latch = new CountDownLatch(1);

        new Thread(()  ->  {
            trainingSession.start(session, testUsername);
            latch.countDown();
        }).start();

        latch.await();

        Thread.sleep(1500);
        assertFalse(trainingSession.isActive());
        assertEquals("Тренировка Завершена!", testInputOutput.getLatestOutput());
    }

    /**
     * Проверяем, что сессия останавливается методом stop()
     */
    @Test
    public void testSessionActiveAfterBeingStopped() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        new Thread(() -> {
            trainingSession.start(session,testUsername);
            latch.countDown();
        }).start();

        latch.await();

        trainingSession.stop();
        assertFalse(trainingSession.isActive());
        assertEquals("Тренировка Завершена!", testInputOutput.getLatestOutput());
    }
}