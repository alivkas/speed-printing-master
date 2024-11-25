package org.example.service;

import org.example.database.DatabaseManager;
import org.example.database.dao.UserDao;
import org.example.database.entity.UserEntity;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 *Тестируем работоспособность класса UserRepository
 */
class UserDaoTest {

    @Mock
    private DatabaseManager databaseManager;

    @Mock
    private Session session;

    @Mock
    private Query<UserEntity> query;

    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<UserEntity> criteriaQuery;

    @Mock
    private Root<UserEntity> root;

    private UserDao userDao;

    /**
     * Настройка мока перед каждым тестом
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(databaseManager.getSession()).thenReturn(session);
        userDao = new UserDao(databaseManager);
    }

    /**
     * Тестирует метод getUserByUsername
     * Проверяет, что метод возвращает пользователя, если он существует в базе данных
     */
    @Test
    void testGetUserByUsernameReturnsUserWhenExists() {
        String username = "testUser";
        UserEntity expectedUser = new UserEntity();
        expectedUser.setUsername(username);

        when(session.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(UserEntity.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(UserEntity.class)).thenReturn(root);
        when(criteriaBuilder.equal(root.get("username"), username)).thenReturn(null); // Настройка условия
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(session.createQuery(criteriaQuery)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(expectedUser);

        UserEntity actualUser = userDao.getUserByUsername(username);

        assertNotNull(actualUser);
        assertEquals(expectedUser.getUsername(), actualUser.getUsername());
    }

    /**
     * Тестирует метод getUserByUsername
     * Проверяет, что метод возвращает null, если пользователь не найден в базе данных
     */
    @Test
    void testGetUserByUsernameReturnsNullWhenNotExists() {
        String username = "nonExistentUser";

        when(session.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(UserEntity.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(UserEntity.class)).thenReturn(root);
        when(criteriaBuilder.equal(root.get("username"), username)).thenReturn(null); // Настройка условия
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(session.createQuery(criteriaQuery)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(null);

        UserEntity actualUser = userDao.getUserByUsername(username);

        assertNull(actualUser);
    }

    /**
     * Тестирует метод getUserByUsername
     * Проверяет, что выбрасывается IllegalArgumentException при передаче null в качестве имени пользователя
     */
    @Test
    void testGetUserByUsernameThrowsExceptionForNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> {
            userDao.getUserByUsername(null);
        });
    }

    /**
     * Тестирует метод getUserByUsername
     * Проверяет, что выбрасывается IllegalArgumentException при передаче пустой строки в качестве имени пользователя
     */
    @Test
    void testGetUserByUsernameThrowsExceptionForEmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> {
            userDao.getUserByUsername("");
        });
    }
}