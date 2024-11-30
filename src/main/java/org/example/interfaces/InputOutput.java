package org.example.interfaces;

/**
 * Интерфейс для вывода и ввода информации
 */
public interface InputOutput {

    /**
     * Выводить строку
     * @param message строка ввода
     */
    void output(String message);

    /**
     * Читать строку из входного потока.
     * @return строка, введенная пользователем.
     */
    String input();
}
