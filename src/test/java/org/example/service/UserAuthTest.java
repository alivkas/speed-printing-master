package org.example.service;

import org.example.database.DatabaseManager;
import org.example.database.dao.UserDao;
import org.example.database.entity.UserEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тестирование класса UserAuth
 */
public class UserAuthTest {
    private DatabaseManager databaseManagerMock;
    private Session sessionMock;
    private UserAuth userAuth;
    @Mock
    private UserDao userDaoMock;

    /**
     * Настройка мока перед каждым тестом
     */
    @BeforeEach
    public void setUp() throws IllegalAccessException, NoSuchFieldException {
        MockitoAnnotations.openMocks(this);
        databaseManagerMock = mock(DatabaseManager.class);
        sessionMock = mock(Session.class);
        userDaoMock = mock(UserDao.class);

        when(databaseManagerMock.getSession()).thenReturn(sessionMock);

        userAuth = new UserAuth();
        Field userDaoField = UserAuth.class.getDeclaredField("userDao");
        userDaoField.setAccessible(true);
        userDaoField.set(userAuth, userDaoMock);
    }

    /**
     * Проверяет успешную регистрацию нового пользователя
     * Имитируется ситуация, когда пользователь с указанным именем не существует в базе данных
     * Проверяется, что метод `registerUser` возвращает `true`,  вызывается метод `save`  для сохранения нового пользователя,
     * и вызывается метод `commit` для подтверждения транзакции
     */
    @Test
    public void testRegisterUserSuccess(){
        when(userDaoMock.getUserByUsername("testUser", sessionMock)).thenReturn(null);

        boolean result = userAuth.registerUser("testUser", "password123", sessionMock);

        assertTrue(result, "User should be successfully registered.");
        verify(sessionMock).save(any(UserEntity.class));
    }

    /**
     * Проверяет неудачную регистрацию пользователя, если пользователь с таким именем уже существует
     * Имитируется ситуация, когда пользователь с указанным именем уже существует в базе данных
     * Проверяется, что метод `registerUser` возвращает `false`,  метод `save` не вызывается,
     * и метод `commit` не вызывается.
     */
    @Test
    public void testRegisterUserFailure_UserExists() {
        UserEntity existingUser = new UserEntity();
        existingUser.setUsername("existingUser");

        when(userDaoMock.getUserByUsername("existingUser", sessionMock)).thenReturn(existingUser);

        boolean result = userAuth.registerUser("existingUser", "password123", sessionMock);

        assertFalse(result);
        verify(sessionMock, never()).save(any(UserEntity.class));
    }

    /**
     * Проверяет успешный вход пользователя в систему
     * Имитируется ситуация, когда пользователь с указанным именем и паролем существует в базе данных
     * Проверяется, что метод `loginUser` возвращает `true`, и вызывается метод `commit` для подтверждения транзакции.
     */
    @Test
    public void testLoginUserSuccess() {
        UserEntity existingUser = new UserEntity();
        existingUser.setUsername("existingUser");
        existingUser.setPassword("password123");

        when(userDaoMock.getUserByUsername("existingUser", sessionMock)).thenReturn(existingUser);

        Transaction transactionMock = mock(Transaction.class);
        when(sessionMock.getTransaction()).thenReturn(transactionMock);

        boolean result = userAuth.loginUser("existingUser", "password123", sessionMock);

        assertTrue(result);
        verify(transactionMock, never()).commit();
    }

    /**
     * Проверяет неудачный вход пользователя из-за неправильного пароля
     * Имитируется ситуация, когда пользователь существует, но введенный пароль неверен
     * Проверяется, что метод `loginUser` возвращает `false`, и метод `commit` не вызывается
     */
    @Test
    public void testLoginUserFailure_IncorrectPassword() {
        UserEntity existingUser = new UserEntity();
        existingUser.setUsername("existingUser");
        existingUser.setPassword("correctPassword");

        when(userDaoMock.getUserByUsername("existingUser", sessionMock)).thenReturn(existingUser);

        Transaction transactionMock = mock(Transaction.class);
        when(sessionMock.getTransaction()).thenReturn(transactionMock);

        boolean result = userAuth.loginUser("existingUser", "wrongPassword", sessionMock);

        assertFalse(result);
        verify(transactionMock, never()).commit();
    }
}