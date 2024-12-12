package org.example.database;

import org.example.interfaces.TransactionalOperation;
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
     * Получить экземпляр сессии из фабрики сессий
     * @return сессия
     */
    public Session getSession() {
        try {
            return sessionFactory.openSession();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка во время взаимодействия с базой данных " +
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
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании SessionFactory", e);
        }
    }

    /**
     * Метод для управления транзакцией
     */
    public void executeInTransaction(TransactionalOperation transactionalOperation) {
        try (Session session = getSession()) {
            final Transaction transaction = session.beginTransaction();
            try {
                transactionalOperation.execute(session);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException("Ошибка транзакции", e);
            }
        }
    }
}
