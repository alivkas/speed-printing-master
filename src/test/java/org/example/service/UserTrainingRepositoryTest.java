package org.example.service;

import org.example.database.DatabaseManager;
import org.example.database.entity.UserEntity;
import org.example.service.UserRepository;
import org.example.service.UserTrainingRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserTrainingRepositoryTest {

    @Mock
    private DatabaseManager databaseManager;

    @Mock
    private Session session;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Transaction transaction;

    private UserTrainingRepository userTrainingRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(databaseManager.getSession()).thenReturn(session);
        when(session.getTransaction()).thenReturn(transaction);
        userTrainingRepository = new UserTrainingRepository(databaseManager);
        userTrainingRepository.userRepository = userRepository;
    }

    @Test
    void testAddNewTrainingSessionSuccess() {
        String username = "testUser";
        double time = 30.0;
        double averageTime = 15.0;

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setTrainingCount(0);
        user.setTime(0.0);
        user.setAverageTime(0.0);

        when(userRepository.getUserByUsername(username)).thenReturn(user);

        boolean result = userTrainingRepository.addNewTrainingSession(username, time, averageTime);

        assertTrue(result);
        assertEquals(1, user.getTrainingCount());
        assertEquals(time, user.getTime());
        assertEquals(averageTime, user.getAverageTime());
        verify(session).beginTransaction();
        verify(session).merge(user);
        verify(transaction).commit();
    }

    @Test
    void testAddNewTrainingSessionUserNotFound() {
        String username = "nonExistentUser";
        double time = 30.0;
        double averageTime = 15.0;

        when(userRepository.getUserByUsername(username)).thenReturn(null);

        boolean result = userTrainingRepository.addNewTrainingSession(username, time, averageTime);

        assertFalse(result);
        verify(session, never()).merge(any());
        verify(transaction, never()).commit();
    }

    @Test
    void testAddNewTrainingSessionTransactionException() {
        String username = "testUser";
        double time = 30.0;
        double averageTime = 15.0;

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setTrainingCount(0);
        user.setTime(0.0);
        user.setAverageTime(0.0);

        when(userRepository.getUserByUsername(username)).thenReturn(user);
        doThrow(TransactionException.class).when(transaction).commit();

        boolean result = userTrainingRepository.addNewTrainingSession(username, time, averageTime);

        assertFalse(result);
        verify(session).beginTransaction();
        verify(transaction).commit();
    }
}
