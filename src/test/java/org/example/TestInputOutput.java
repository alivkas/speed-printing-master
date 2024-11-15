package org.example;

import org.example.interfaces.InputOutput;

/**
 * Вспомогательный класс для тестирования
 * предназначен для имитации ввода и вывода
 */
public class TestInputOutput implements InputOutput {
    private String input;
    private String latestOutput;

    /**
     * Выводить строку
     * @param message строка ввода
     */
    @Override
    public void output(String message) {
        latestOutput = message;
    }

    /**
     * Читать строку из входного потока.
     * @return строка, введенная пользователем.
     */
    @Override
    public String input() {
        return input;
    }
}
