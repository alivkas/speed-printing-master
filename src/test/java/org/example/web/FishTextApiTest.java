package org.example.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Тесты для класса Api
 */
public class FishTextApiTest {

    /**
     * Тестировать получение сгенерированного предложения
     * из FishTextApi
     */
    @Test
    public void testGetTextFromFishTextApi() {
        FishTextApi fishTextApi = new FishTextApi();
        String text = fishTextApi.getTextFromFishTextApi();
        Assertions.assertNotEquals(null, text);
    }
}
