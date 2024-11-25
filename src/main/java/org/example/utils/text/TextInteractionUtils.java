package org.example.utils.text;

/**
 * Взаимодействие с текстом
 */
public class TextInteractionUtils {

    /**
     * Получить количество слов введенных пользователем
     * @param input предложение, введенное пользователем
     * @return количество введеных слов
     */
    public int getWordsCount(String input) {
        String[] splitText = input.split(" ");
        return splitText.length;
    }
}
