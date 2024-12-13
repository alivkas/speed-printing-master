package org.example.interfaces;

import org.hibernate.Session;

/**
 * Функциональный интерфейс для выполнения операций в сессиях
 */
@FunctionalInterface
public interface SessionOperation {

    /**
     * Метод для выполнения операций с сессией
     *
     * @param session текущая сессия
     */
    void execute(Session session);
}
