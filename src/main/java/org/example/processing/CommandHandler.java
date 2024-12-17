package org.example.processing;

import org.example.database.SessionManager;
import org.example.database.context.CurrentUserContext;
import org.example.interfaces.Command;
import org.example.interfaces.InputOutput;
import org.example.processing.factory.CommandFactory;
import java.util.Optional;

/**
 * Класс для перехвата команд пользователя.
 */
public class CommandHandler {
    private final CurrentUserContext currentUserContext = new CurrentUserContext();
    private final CommandFactory commandFactory;
    private final SessionManager sessionManager;
    private final InputOutput inputOutput;

    /**
     * Конструктор класса CommandHandler, который получает ссылку реализацию inputOutput
     * и sessionManager. Инициализирует CommandFactory
     * @param inputOutput реализация интерфейса InputOutput
     * @param sessionManager ссылка на управление сессиями
     */
    public CommandHandler(InputOutput inputOutput,
                          SessionManager sessionManager) {
        this.inputOutput = inputOutput;
        this.sessionManager = sessionManager;
        this.commandFactory = new CommandFactory(inputOutput, currentUserContext);
    }

    /**
     * Обрабатывает команды, введенные пользователем.
     * @param command Команда, введенная пользователем.
     */
    public void handleCommand(String command) {
        try {
            Command cmd = commandFactory.getCommandByCommand(command);
            if (cmd.requiresTransaction()) {
                sessionManager.executeInTransaction(session ->
                        cmd.execute(Optional.of(session)));
            } else if (cmd.requiresSession()) {
                sessionManager.executeInSession(session ->
                        cmd.execute(Optional.of(session)));
            } else {
                cmd.execute(Optional.empty());
            }
        } catch (NullPointerException e) {
            inputOutput.output("Неизвестная команда");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Сессия обязательна для выполнения этой команды", e);
        }
    }
}