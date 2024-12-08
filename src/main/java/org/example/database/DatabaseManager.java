package org.example.database;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

/**
 * Управление базой данных
 */
public class DatabaseManager {

    private final Logger logger = Logger.getLogger(DatabaseManager.class);
    private final SessionManager sessionManager = new SessionManager();

    /**
     * Конструктор DatabaseManager, который отключает логи Hibernate
     * и инициализирует запуск базы данных
     */
    public DatabaseManager() {
        startDb();
    }

    /**
     * Получить текущую сессию
     * @return сессия
     */
    public Session getSession() {
        Session session = null;
        try {
            session = sessionManager.getSession();
        } catch (IllegalStateException e) {
            logger.error(e.getMessage(), e);
            logger.info(e.getMessage());
        }
        return session;
    }

    /**
     * Запускать базу данных
     */
    private void startDb() {
        Configuration configuration = new Configuration().configure();
        sessionManager.buildSessionFactory(configuration);
    }
}