package org.example.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Управление сессиями
 */
public class SessionManager {
    private SessionFactory sessionFactory;

    /**
     * Вернуть текущую сессию
     * @return сессия
     */
    public Session getSession() {
        if (sessionFactory == null) {
            throw new IllegalStateException("SessionFactory не инициализирован");
        }
        return sessionFactory.openSession();
    }

    /**
     * Построить sessionFactory
     * @param configuration конфигурация базы данных
     */
    public void buildSessionFactory(Configuration configuration) {
        sessionFactory = configuration.buildSessionFactory();
    }
}