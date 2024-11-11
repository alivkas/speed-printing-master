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
     * Сохраняет сообщение в переменной
     * @param message cообщение, которое нужно вывести
     */
    @Override
    public void output(String message) {
        latestOutput = message;
    }

    /**
     * Возвращает строку, представляющую ввод пользователя
     *
     * @return cтрока, введенная пользователем
     */
    @Override
    public String input() {
        return input;
    }

}
