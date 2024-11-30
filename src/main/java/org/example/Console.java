package org.example;

import org.example.interfaces.InputOutput;

import java.util.Scanner;

/**
 * Точка входа в тренировочное приложение
 */
public class Console implements InputOutput {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void output(String message) {
        System.out.println(message);
    }

    @Override
    public String input() {
        return scanner.nextLine();
    }
}