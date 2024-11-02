package org.example.text;

import java.util.ArrayList;
import java.util.List;

/**
 * Взаимодействие с текстом
 */
public class TextInteraction {

    /**
     * Сравнить оригинальный текст и написаный человеком
     * @param processedText оригинальный текст
     * @param input текст, написаный человеком
     */
    public boolean compareText(String processedText, String input) {
        return processedText.equals(input);
    }

    /**
     * Считать количество слов введенных пользователем
     * @param input предложение, введенное пользователем
     * @return количество введеных слов
     */
    public int wordsCountIncrease(String input) {
        String[] splitText = input.split(" ");
        return splitText.length;
    }
}
