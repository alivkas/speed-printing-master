package org.example.database;

import org.example.database.logger.HibernateLoggingConfigurator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Управление базой данных
 */
public class DatabaseManager {
    private  SessionFactory sessionFactory;
    private Session session;


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
     * Вернуть текущую сессию
     * @return сессия
     */
    public Session getSession() {
        if (sessionFactory == null) {
            throw new IllegalStateException("SessionFactory is not initialized");
        }
        return sessionFactory.openSession();
    }

    /**
     * Запускать базу данных
     */
    private void startDb() {
        Configuration configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
        session = sessionFactory.openSession();
    }

}