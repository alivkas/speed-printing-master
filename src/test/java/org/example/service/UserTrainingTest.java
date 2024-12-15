package org.example.service;

import org.example.database.dao.UserDao;
import org.example.database.entity.UserEntity;
import org.example.interfaces.InputOutput;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тестирование UserTraining
 */
public class UserTrainingTest {

    private final static String TEST_USER = "testUser";

    private final UserTraining userTraining;

    private final UserDao userDaoMock = mock(UserDao.class);

    private final Session sessionMock = mock(Session.class);

    public UserTrainingTest() {
        userTraining = new UserTraining(userDaoMock);
    }

    /**
     * Тестирует успешное обновление данных о тренировке для существующего пользователя
     */
    @Test
    void testUpdateTrainingData_Success() {
        int totalWords = 10;
        int typoCount = 0;

        UserEntity existingUser = new UserEntity();
        existingUser.setUsername(TEST_USER);
        existingUser.setTrainingCount(2);
        existingUser.setTime(100000);
        existingUser.setAverageTime(50.0);
        existingUser.setSumTypoCount(0);

        when(userDaoMock.getUserByUsername(TEST_USER, sessionMock)).thenReturn(existingUser);

        userTraining.updateTrainingData(TEST_USER, totalWords, typoCount, sessionMock);

        assertEquals(3, existingUser.getTrainingCount());
        assertEquals(56.0, existingUser.getAverageTime());
        verify(userDaoMock, times(2)).getUserByUsername(TEST_USER, sessionMock);
        verify(sessionMock).merge(existingUser);
    }

    /**
     * Тестирует успешное сохранение времени тренировки пользователя
     */
    @Test
    void testSaveUsersTrainingTime_Success() {
        int newTime = 120000;

        UserEntity existingUser = new UserEntity();
        existingUser.setUsername(TEST_USER);
        existingUser.setTime(100000);

        when(userDaoMock.getUserByUsername(TEST_USER, sessionMock)).thenReturn(existingUser);

        userTraining.saveUsersTrainingTime(newTime, TEST_USER, sessionMock);

        assertEquals(newTime, existingUser.getTime());
        verify(userDaoMock).getUserByUsername(TEST_USER, sessionMock);
        verify(sessionMock).merge(existingUser);
    }

    /**
     * Тестирует получение времени тренировки пользователя
     */
    @Test
    void testGetUserTrainingTime_Success() {
        UserEntity existingUser = new UserEntity();
        existingUser.setUsername(TEST_USER);
        existingUser.setTime(100000);

        when(userDaoMock.getUserByUsername(TEST_USER, sessionMock)).thenReturn(existingUser);

        double trainingTime = userTraining.getUserTrainingTime(TEST_USER, sessionMock);

        assertEquals(100000.0, trainingTime);
        verify(userDaoMock).getUserByUsername(TEST_USER, sessionMock);
    }
}
