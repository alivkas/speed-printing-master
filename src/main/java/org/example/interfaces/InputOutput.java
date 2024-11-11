package org.example.interfaces;

/**
 * Интерфейс для вывода информации
 */
public interface InputOutput {
    /**
     * Выводит строку
     */
    void output(String message);
    /**
     * Читает строку из входного потока.
     * @return строка, введенная пользователем.
     */
    String input();
}
