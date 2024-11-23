package org.example;

import org.example.processing.CommandHandler;

/**
 * Запускает тренировочное приложение.
 */
public class Main {
    public static void main(String[] args) {
        Console app = new Console();
        CommandHandler commandHandler = new CommandHandler(app);
        String info = """
                Добро пожаловать в тренировочное приложение! Введите команду /help для просмотра всех команд
                """;
        app.output(info);
        while (true) {
            app.output("Введите команду: ");
            String command = app.input();
            commandHandler.handleCommand(command);
        }
    }
}
