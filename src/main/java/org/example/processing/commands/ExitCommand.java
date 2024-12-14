package org.example.processing.commands;

import org.example.interfaces.Command;
import org.example.interfaces.InputOutput;
import org.hibernate.Session;

import java.util.Optional;

/**
 * Определение команды /exit
 */
public class ExitCommand implements Command {

    private final InputOutput inputOutput;

    /**
     * Конструктор ExitCommand, который получает ссылку на реализацию InputOutput
     * @param inputOutput реализация интерфейса InputOutput
     */
    public ExitCommand(InputOutput inputOutput) {
        this.inputOutput = inputOutput;
    }

    @Override
    public void execute(Optional<Session> optionalSession) throws IllegalArgumentException {
        if (requiresSession()
                && optionalSession.isEmpty()) {
            throw new IllegalArgumentException("Сессия обязательна для выполнения этой команды");
        }
        inputOutput.output("Выход из приложения.");
        System.exit(0);
    }

    @Override
    public boolean requiresTransaction() {
        return false;
    }

    @Override
    public boolean requiresSession() {
        return false;
    }
}