package org.example.service;

import org.example.database.DatabaseManager;
import org.example.database.entity.UserEntity;
import org.example.database.dao.UserDao;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс для проверки функциональности класса UserTraining
 */
class UserTrainingTest {

    private UserTraining userTraining;
    private UserDao userDaoMock;
    private DatabaseManager databaseManagerMock;
    private Session sessionMock;
    private Transaction transactionMock;

    /**
     * Настройка мока перед каждым тестом
     */
    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        userDaoMock = mock(UserDao.class);
        databaseManagerMock = mock(DatabaseManager.class);
        sessionMock = mock(Session.class);
        transactionMock = mock(Transaction.class);

        when(databaseManagerMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.beginTransaction()).thenReturn(transactionMock);
        when(sessionMock.getTransaction()).thenReturn(transactionMock);
        when(sessionMock.merge(any(UserEntity.class))).thenReturn(new UserEntity());

        userTraining = new UserTraining();
        Field databaseManagerField = UserTraining.class.getDeclaredField("databaseManager");
        databaseManagerField.setAccessible(true);
        databaseManagerField.set(userTraining, databaseManagerMock);

        Field userDaoField = UserTraining.class.getDeclaredField("userDao");
        userDaoField.setAccessible(true);
        userDaoField.set(userTraining, userDaoMock);
    }

    /**
     * Тестирует успешное обновление данных о тренировке для существующего пользователя
     * Проверяется корректность обновления полей сущности пользователя после вызова метода updateTrainingData
     */
    @Test
    void testUpdateTrainingData_Success() {
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        user.setTrainingCount(2);
        user.setTime(100.0);
        user.setAverageTime(50.0);

        when(userDaoMock.getUserByUsername("testUser")).thenReturn(user);


        userTraining.updateTrainingData("testUser", 10, 50);

        verify(sessionMock).merge(any(UserEntity.class));
        verify(transactionMock).commit();

        assertEquals(3, user.getTrainingCount());
        assertEquals(110.0, user.getTime());
        assertEquals(55.0, user.getAverageTime());
    }

    /**
     * Тестирует сценарий, когда пользователь не найден в базе данных
     * Проверяется, что метод updateTrainingData не вызывает Session merge и
     * Transaction commit
     * , если пользователь с указанным именем не существует
     */
    @Test
    void testUpdateTrainingData_UserNotFound() {
        when(userDaoMock.getUserByUsername("nonExistingUser")).thenReturn(null);
        userTraining.updateTrainingData("nonExistingUser", 10, 50);

        verify(sessionMock, never()).merge(any(UserEntity.class));
        verify(transactionMock, never()).commit();
    }
}
