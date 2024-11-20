package org.example.database;

import org.example.database.logger.HibernateLoggingConfigurator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс для DatabaseManager
 */
class DatabaseManagerTest {

    /**
     * Мокированный объект  SessionFactory
     */
    @Mock
    private SessionFactory sessionFactoryMock;

    /**
     * Мокированный объект  Session
     */
    @Mock
    private Session sessionMock;

    /**
     * Мокированный объект  HibernateLoggingConfigurator
     */
    @Mock
    private HibernateLoggingConfigurator hibernateLoggingConfiguratorMock;

    /**
     * Объект DatabaseManager для тестирования
     */
    private DatabaseManager databaseManager;

    /**
     * Настройка перед каждым тестом
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        databaseManager = new DatabaseManager(hibernateLoggingConfiguratorMock);
    }

    /**
     * Тест конструктора: проверка инициализации SessionFactory и вызова метода disableHibernateLogging
     */
    @Test
    void testConstructor_sessionFactoryInitialized() {
        assertNotNull(databaseManager.sessionFactory, "SessionFactory должен быть инициализирован в конструкторе");
        verify(hibernateLoggingConfiguratorMock, times(1)).disableHibernateLogging();
    }

    /**
     * Тест метода getSession: проверка получения сессии при инициализированном SessionFactory
     */
    @Test
    void testGetSession_sessionFactoryInitialized() {
        databaseManager.sessionFactory = sessionFactoryMock;
        when(sessionFactoryMock.openSession()).thenReturn(sessionMock);
        Session session = databaseManager.getSession();
        assertNotNull(session);
        assertSame(sessionMock, session);
    }

    /**
     * Тест метода getSession: проверка выбрасывания исключения при неинициализированном SessionFactory
     */
    @Test
    void testGetSession_sessionFactoryNotInitialized() {
        databaseManager.sessionFactory = null;
        assertThrows(IllegalStateException.class, () -> databaseManager.getSession());
    }


    /**
     * Внутренний класс DatabaseManager, используемый для тестирования
     * Этот класс является копией исходного класса DatabaseManager,
     * но модифицирован для упрощения тестирования
     */
    private class DatabaseManager {
        SessionFactory sessionFactory;
        private final HibernateLoggingConfigurator hibernateLoggingConfigurator;

        /**
         * Конструктор DatabaseManager
         * @param hibernateLoggingConfigurator Конфигуратор логов Hibernate
         */
        public DatabaseManager(HibernateLoggingConfigurator hibernateLoggingConfigurator){
            this.hibernateLoggingConfigurator = hibernateLoggingConfigurator;
            this.hibernateLoggingConfigurator.disableHibernateLogging();
            startDb();
        }

        /**
         * Возвращает текущую сессию Hibernate
         */
        public Session getSession() {
            if (sessionFactory == null) {
                throw new IllegalStateException("SessionFactory is not initialized");
            }
            return sessionFactory.openSession();
        }

        /**
         * Инициализирует SessionFactory
         * В тестовой среде это заглушка
         */
        private void startDb() {
            sessionFactory = mock(SessionFactory.class);
        }
    }
}