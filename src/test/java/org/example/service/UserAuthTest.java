package org.example.service;

import org.example.database.dao.UserDao;
import org.example.database.entity.UserEntity;
import org.example.interfaces.InputOutput;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс для проверки функциональности класса UserAuth
 * Этот класс содержит тесты для методов регистрации и входа пользователей
 */
public class UserAuthTest {

    private UserAuth userAuth;
    private InputOutput inputOutputMock;
    private UserDao userDaoMock;
    private Session sessionMock;

    /**
     * Настройка моков перед каждым тестом
     * Создает экземпляры моков и инициализирует объект userAuth
     */
    @BeforeEach
    void setUp() {
        inputOutputMock = mock(InputOutput.class);
        userDaoMock = mock(UserDao.class);
        sessionMock = mock(Session.class);
        userAuth = new UserAuth(inputOutputMock, userDaoMock);
    }

    /**
     * Тестирует успешную регистрацию пользователя
     * Проверяет, что новый пользователь сохраняется в базе данных,
     * если он не существует
     */
    @Test
    void testRegisterUser_Success() {
        UserEntity expectedUser = new UserEntity();
        expectedUser.setUsername("testuser");
        expectedUser.setPassword("password");
        expectedUser.setAverageTime(0.0);
        expectedUser.setTrainingCount(0);
        expectedUser.setTime(0);

        when(userDaoMock.getUserByUsername("testuser", sessionMock)).thenReturn(null);

        boolean result = userAuth.registerUser("testuser", "password", sessionMock);

        assertTrue(result);
        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(sessionMock).save(userCaptor.capture());
        UserEntity savedUser = userCaptor.getValue();

        assertEquals(expectedUser.getUsername(), savedUser.getUsername());
        assertEquals(expectedUser.getPassword(), savedUser.getPassword());
        assertEquals(expectedUser.getAverageTime(), savedUser.getAverageTime());
        assertEquals(expectedUser.getTrainingCount(), savedUser.getTrainingCount());
        assertEquals(expectedUser.getTime(), savedUser.getTime());
    }

    /**
     * Тестирует регистрацию пользователя, когда пользователь уже существует.
     * Проверяет, что метод возвращает false и не сохраняет нового пользователя.
     */
    @Test
    void testRegisterUser_Failure_UserExists() {
        UserEntity existingUser = new UserEntity();
        existingUser.setUsername("testuser");

        when(userDaoMock.getUserByUsername("testuser", sessionMock)).thenReturn(existingUser);

        boolean result = userAuth.registerUser("testuser", "password", sessionMock);

        assertFalse(result);

        UserEntity newUser = new UserEntity();
        newUser.setUsername("testuser");
        newUser.setPassword("password");
        newUser.setAverageTime(0.0);
        newUser.setTrainingCount(0);
        newUser.setTime(0

        );

        verify(sessionMock, never()).save(newUser);
    }

    /**
     * Тестирует успешный вход пользователя
     * Проверяет, что метод возвращает true, если введенные данные верны
     */
    @Test
    void testLoginUser_Success() {
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setPassword("password");
        when(userDaoMock.getUserByUsername("testuser", sessionMock)).thenReturn(user);

        boolean result = userAuth.loginUser("testuser", "password", sessionMock);

        assertTrue(result);
    }

    /**
     * Тестирует вход пользователя с неправильным паролем
     * Проверяет, что метод возвращает false
     */
    @Test
    void testLoginUser_Failure_WrongPassword() {
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setPassword("wrongpassword");
        when(userDaoMock.getUserByUsername("testuser", sessionMock)).thenReturn(user);

        boolean result = userAuth.loginUser("testuser", "password", sessionMock);

        assertFalse(result);
    }

    /**
     * Тестирует вход пользователя, когда пользователь не найден
     * Проверяет, что метод возвращает false
     */
    @Test
    void testLoginUser_Failure_UserNotFound() {
        when(userDaoMock.getUserByUsername("testuser", sessionMock)).thenReturn(null);
        boolean result = userAuth.loginUser("testuser", "password", sessionMock);

        assertFalse(result);
    }
}