package org.example;

import org.example.interfaces.InputOutput;

import java.util.Scanner;

/**
 * Точка входа в тренировочное приложение
 */
public class Console implements InputOutput {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Выводить строку
     * @param message строка ввода
     */
    @Override
    public void output(String message) {
        System.out.println(message);
    }

    /**
     * Читает строку из входного потока.
     * @return строка, введенная пользователем.
     */
    @Override
    public String input() {
        return scanner.nextLine();
    }
}