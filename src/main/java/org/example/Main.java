package org.example;

import org.example.processing.CommandHandler;
import org.example.web.FishTextApi;

/**
 * Запускает тренировочное приложение.
 */
public class Main {
    public static void main(String[] args) {
        FishTextApi fishTextApi = new FishTextApi();
        Console app = new Console();
        CommandHandler commandHandler = new CommandHandler(app, fishTextApi);
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
