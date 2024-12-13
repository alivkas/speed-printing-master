package org.example.database;

import org.example.interfaces.SessionOperation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
     * Построить sessionFactory, с конфигурацией Hibernate
     */
    private void buildSessionFactory() {
        try {
            Configuration configuration = new Configuration().configure();
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании SessionFactory", e);
        }
    }

    /**
     * Метод для выполнения операций в сессии с транзакцией
     */
    public void executeInTransaction(SessionOperation sessionOperation) {
        try (Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            try {
                sessionOperation.execute(session);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException("Ошибка транзакции", e);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка сессии");
        }
    }

    /**
     * Метод для выполнения операций в сессии
     */
    public void executeInSession(SessionOperation sessionOperation) {
        try (Session session = sessionFactory.openSession()) {
                sessionOperation.execute(session);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка сессии", e);
        }
    }
}
