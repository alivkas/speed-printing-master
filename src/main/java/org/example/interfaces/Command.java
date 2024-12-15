package org.example.interfaces;

import org.hibernate.Session;

import java.util.Optional;

/**
 * Определение поведения команды
 */
public interface Command {

    /**
     * Выполнить команду в текущей сессии или без нее.
     * Если сессия не предоставлена и требуется, будет выброшено исключение
     * @param optionalSession сессия, которая может использоваться,
     *                        когда методу она нужна, в противном случае
     *                        сессия не используется
     * @throws IllegalArgumentException если сессия требуется, но не предоставлена
     */
    void execute(Optional<Session> optionalSession) throws IllegalArgumentException;

    /**
     * Узнать, требуется ли транзакция для выполнения команды
     * @return true если требуется транзакция, false если нет
     */
    boolean requiresTransaction();

    /**
     * Узнать, требуется ли сессия для выполнения команды
     * @return true если требуется сессия, false если нет
     */
    boolean requiresSession();
}
