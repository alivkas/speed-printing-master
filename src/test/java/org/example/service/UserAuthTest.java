package org.example.service;

import org.example.database.DatabaseManager;
import org.example.database.entity.UserEntity;
import org.example.interfaces.InputOutput;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserAuthTest {

    private InputOutput inputOutputMock;
    private DatabaseManager databaseManagerMock;
    private Session sessionMock;
    private UserAuth userAuth;

    @BeforeEach
    public void setUp() {
        inputOutputMock = mock(InputOutput.class);
        databaseManagerMock = mock(DatabaseManager.class);
        sessionMock = mock(Session.class);
        userAuth = new UserAuth(inputOutputMock);

        when(databaseManagerMock.getSession()).thenReturn(sessionMock);
    }

    /**
     * Проверяем успешную регистрацию нового пользователя
     * В этом тесте имитируется ввод имени пользователя и пароля
     * а также поведение базы данных, чтобы убедиться, что новый пользователь
     * будет зарегистрирован, если имя пользователя еще не существует
     */
    @Test
    public void testRegisterUserSuccess() {
        when(inputOutputMock.input()).thenReturn("testUser", "password123");

        org.hibernate.query.Query<UserEntity> queryMock = mock(org.hibernate.query.Query.class);

        when(sessionMock.createQuery(anyString(), eq(UserEntity.class))).thenReturn(queryMock);
        when(queryMock.setParameter(anyString(), anyString())).thenReturn(queryMock);
        when(queryMock.uniqueResult()).thenReturn(null);

        Transaction transactionMock = mock(Transaction.class);

        when(sessionMock.getTransaction()).thenReturn(transactionMock);

        boolean result = userAuth.registerUser(databaseManagerMock);

        assertTrue(result);
        verify(sessionMock).save(any(UserEntity.class));
        verify(transactionMock).commit();
    }

    /**
     * Тест для проверки регистрации пользователя, который уже существует
     * Ожидается, что метод вернет false и не будет вызван метод save
     */
    @Test
    public void testRegisterUserFailure_UserExists() {
        when(inputOutputMock.input()).thenReturn("existingUser", "password123");

        UserEntity existingUser = new UserEntity();
        existingUser.setUsername("existingUser");

        org.hibernate.query.Query<UserEntity> queryMock = mock(org.hibernate.query.Query.class);

        Transaction transactionMock = mock(Transaction.class);

        when(sessionMock.createQuery(anyString(), eq(UserEntity.class))).thenReturn(queryMock);

        when(queryMock.setParameter(anyString(), anyString())).thenReturn(queryMock);

        when(queryMock.uniqueResult()).thenReturn(existingUser);

        when(sessionMock.getTransaction()).thenReturn(transactionMock);

        boolean result = userAuth.registerUser(databaseManagerMock);

        assertFalse(result);
        verify(sessionMock, never()).save(any(UserEntity.class));
        verify(transactionMock, never()).commit();
    }

    /**
     * Тестирует успешный вход пользователя в систему
     * Проверяем, что метод UserAuth возвращает true
     * Также проверяем, что имя пользователя сохраняется в объекте
     * В конце теста проверяем, что метод
     *  Transaction.commit() был вызван, что подтверждает завершение транзакции
     */
    @Test
    public void testLoginUserSuccess() {
        when(inputOutputMock.input()).thenReturn("testUser", "password123");

        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        user.setPassword("password123");

        org.hibernate.query.Query<UserEntity> queryMock = mock(org.hibernate.query.Query.class);

        Transaction transactionMock = mock(Transaction.class);

        when(sessionMock.createQuery(anyString(), eq(UserEntity.class))).thenReturn(queryMock);

        when(queryMock.setParameter(anyString(), anyString())).thenReturn(queryMock);

        when(queryMock.uniqueResult()).thenReturn(user);

        when(sessionMock.getTransaction()).thenReturn(transactionMock);

        boolean result = userAuth.loginUser(databaseManagerMock);

        assertTrue(result);
        assertEquals("testUser", userAuth.getUsername());

        verify(transactionMock).commit();
    }
    /**
     * Тестирует сценарий неудачного входа пользователя с неправильным паролем
     * Создается объект UserEntity с правильными данными
     * Проверяется, что метод loginUser возвращает false и имя пользователя остается null
     * так как введенный пароль неверен
     */
    @Test
    public void testLoginUserFailure_IncorrectPassword() {
        when(inputOutputMock.input()).thenReturn("testUser", "wrongPassword");

        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        user.setPassword("password123");

        org.hibernate.query.Query<UserEntity> queryMock = mock(org.hibernate.query.Query.class);

        when(sessionMock.createQuery(anyString(), eq(UserEntity.class))).thenReturn(queryMock);
        when(queryMock.setParameter(anyString(), anyString())).thenReturn(queryMock);
        when(queryMock.uniqueResult()).thenReturn(user); // Пользователь существует

        boolean result = userAuth.loginUser(databaseManagerMock);

        assertFalse(result);
        assertNull(userAuth.getUsername());
    }

}