package org.example;

import org.example.database.SessionManager;
import org.example.processing.CommandHandler;

/**
 * Запускает тренировочное приложение.
 */
public class Main {
    public static void main(String[] args) {
        Console app = new Console();
        SessionManager sessionManager = new SessionManager();
        CommandHandler commandHandler = new CommandHandler(app, sessionManager);
        String info = """
                Добро пожаловать в тренировочное приложение! Введите команду /help для просмотра всех команд
                Зарегистрируйтесь (/registration) или войдите в существующий аккаунт (/login)
                """;
        app.output(info);
        while (true) {
            app.output("Введите команду: ");
            String command = app.input();
            commandHandler.handleCommand(command);
        }
    }
}
