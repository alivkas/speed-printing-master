package org.example;

import org.example.database.SessionManager;
import org.example.database.dao.UserDao;
import org.example.processing.CommandHandler;
import org.example.web.FishTextApi;

/**
 * Запускает тренировочное приложение.
 */
public class Main {
    public static void main(String[] args) {
        Console app = new Console();
        FishTextApi fishTextApi = new FishTextApi(app);
        UserDao userDao = new UserDao();
        CommandHandler commandHandler = new CommandHandler(app, fishTextApi, userDao);
        String info = """
                Добро пожаловать в тренировочное приложение! Введите команду /help для просмотра всех команд
                Зарегистрируйтесь (/registration), войдите в существующий аккаунт (/login) или продолжите как гость
                """;
        app.output(info);
        while (true) {
            app.output("Введите команду: ");
            String command = app.input();
            commandHandler.handleCommand(command);
        }
    }
}
