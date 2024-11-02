package org.example.text;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Тестирование класса TextInteraction
 */
public class TextInteractionTest {

    private final TextInteraction textInteraction = new TextInteraction();

    /**
     * Тестирует сравнение оригинального текста и текста пользователя
     */
    @Test
    public void compareTextTest() {
        String originalText = "Random text from api";
        String userText = "Random text from api";

        boolean compare = textInteraction.compareText(originalText, userText);

        Assert.assertTrue(compare);
    }

    /**
     * Тестирует подсчет количества слов в предложении
     */
    @Test
    public void wordsCountIncreaseTest() {
        String text = "This text has 5 words";
        int wordsCount = textInteraction.wordsCountIncrease(text);

        Assert.assertEquals(5, wordsCount);
    }
}
