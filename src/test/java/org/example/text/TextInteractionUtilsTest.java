package org.example.text;

import org.example.text.utils.TextInteractionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Тестирование класса TextInteraction
 */
public class TextInteractionUtilsTest {

    private final TextInteractionUtils textInteractionUtils = new TextInteractionUtils();

    /**
     * Тестирует подсчет количества слов в предложении
     */
    @Test
    public void testGetWordsCount() {
        String text = "This text has 5 words";
        int wordsCount = textInteractionUtils.getWordsCount(text);

        Assertions.assertEquals(5, wordsCount);
    }
}
