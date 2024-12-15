package org.example.processing.commands;

import org.example.interfaces.Command;
import org.example.interfaces.InputOutput;
import org.hibernate.Session;

import java.util.Optional;

/**
 * Определение команды /help
 */
public class HelpCommand implements Command {

    private final InputOutput inputOutput;

    /**
     * Конструктор HelpCommand, который получает ссылку на реализацию InputOutput
     * @param inputOutput реализация интерфейса InputOutput
     */
    public HelpCommand(InputOutput inputOutput) {
        this.inputOutput = inputOutput;
    }

    @Override
    public void execute(Optional<Session> optionalSession) throws IllegalArgumentException {
        if (requiresSession()
                && optionalSession.isEmpty()) {
            throw new IllegalArgumentException("Сессия обязательна для выполнения этой команды");
        }
        sendHelp();
    }

    @Override
    public boolean requiresTransaction() {
        return false;
    }

    @Override
    public boolean requiresSession() {
        return false;
    }

    /**
     * Выводит список доступных команд.
     */
    private void sendHelp() {
        String helpText = """
            /help - Все команды
            /registration - зарегистрироваться
            /login - войти в систему
            /settings - Настройки тренировки
            /start - Начать тренировку
            /stop - Прервать тренировку
            /exit - Завершить приложение
            /info - Информация о пользователе
            """;
        inputOutput.output(helpText);
    }
}
