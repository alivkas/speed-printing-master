package org.example;

import org.example.interfaces.InputOutput;
import org.example.processing.CommandHandler;


import java.util.Scanner;

/**
 * Точка входа в тренировочное приложение
 */
public class Console implements InputOutput {
    private final Scanner scanner;
    private final CommandHandler commandHandler;

    /**
     * Конструктор класса Console, который инициализирует поля commandHandler и scanner
     */
    public Console() {
        scanner = new Scanner(System.in);
        commandHandler = new CommandHandler(this);
    }

    /**
     * Запускать приложение.
     */
    public void run() {
        welcomeMessage();
        processCommands();
    }

    /**
     * Обработка команд введённых пользователем
     */
    private void processCommands() {
        while (true) {
            String command = readCommand();
            commandHandler.handleCommand(command);
        }
    }

    /**
     * Выводит приветствие
     */
    private void welcomeMessage() {
        System.out.println("Добро пожаловать в тренировочное приложение! Введите команду /help для просмотра всех команд");
    }

    /**
     * Читает команду пользователя
     */
    private String readCommand() {
        System.out.println("Введите команду: ");
        return scanner.nextLine();
    }

    /**
     * Выводит строку
     * @param message Строка для вывода.
     */
    @Override
    public void output(String message) {
        System.out.println(message);
    }

    /**
     * Читает строку из входного потока
     * @return Строка, введенная пользователем.
     */
    @Override
    public String input() {
        return scanner.nextLine();
    }
}