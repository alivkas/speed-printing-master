package org.example.database;

import org.example.database.logger.HibernateLoggingConfigurator;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

/**
 * Управление базой данных
 */
public class DatabaseManager {

    private final SessionManager sessionManager = new SessionManager();

    /**
     * Получить текущую сессию
     * @return сессия
     */
    public Session getSession() {
        return sessionManager.getSession();
    }

    /**
     * Конструктор DatabaseManager, который отключает логи Hibernate
     * и инициализирует запуск базы данных
     */
    public DatabaseManager() {
        HibernateLoggingConfigurator configurator = new HibernateLoggingConfigurator();
        configurator.disableHibernateLogging();
        startDb();
    }

    /**
     * Запускать базу данных
     */
    private void startDb() {
        Configuration configuration = new Configuration().configure();
        sessionManager.startSession(configuration);
    }
}