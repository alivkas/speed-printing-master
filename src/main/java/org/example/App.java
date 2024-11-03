package org.example;

import org.example.processing.CommandHandler;

import java.util.Scanner;

/**
 * Класс для запуска тренировочного приложения.
 */
public class App {
    private final CommandHandler commandHandler;
    private final Scanner scanner;

    /**
     * Конструктор класса App.
     */
    public App() {
        commandHandler = new CommandHandler();
        scanner = new Scanner(System.in);
    }

    /**
     * Метод для запуска приложения.
     */
    public void run() {
        System.out.println("Добро пожаловать в тренировочное приложение!");

        while (true) {
            System.out.print("Введите команду: ");
            String input = scanner.nextLine();
            commandHandler.handleCommand(input);
        }
    }
}