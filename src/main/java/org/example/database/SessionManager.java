package org.example.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Управление сессиями
 */
public class SessionManager {
    private SessionFactory sessionFactory;

    /**
     * Конструктор SessionManager, который строит SessionFactory
     */
    public SessionManager() {
        buildSessionFactory();
    }

    /**
     * Получить экземпляр сессии из фабрики сессий
     * @return сессия
     */
    public Session getSession() {
        if (sessionFactory == null) {
            throw new IllegalStateException("SessionFactory не инициализирован");
        }
        try {
            return sessionFactory.openSession();
        } catch (HibernateException e) {
            throw new HibernateException("Ошибка во время взаимодействия с базой данных " +
                    "через Hibernate", e);
        }
    }

    /**
     * Построить sessionFactory, с конфигурацией Hibernate
     */
    private void buildSessionFactory() {
        try {
            Configuration configuration = new Configuration().configure();
            sessionFactory = configuration.buildSessionFactory();
        } catch (HibernateException e) {
            throw new HibernateException("Ошибка при создании SessionFactory", e);
        }
    }
}