package org.example.interfaces;

import org.hibernate.Session;
/**
 * Функциональный интерфейс для выполнения операций в рамках транзакции
 */
@FunctionalInterface
public interface TransactionalOperation {
    /**
     * Метод для выполнения операций с сессией
     *
     * @param session текущая сессия
     */
    void execute(Session session);
}
