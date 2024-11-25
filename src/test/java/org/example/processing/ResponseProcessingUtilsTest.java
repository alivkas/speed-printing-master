package org.example.processing;

import org.example.utils.processing.ResponseProcessingUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Тесты для класса ResponseProcessing
 */
public class ResponseProcessingUtilsTest {

    /**
     * Тестировать обрезку текста от html тегов
     */
    @Test
    public void sanitizeTest() {
        ResponseProcessingUtils processing = new ResponseProcessingUtils();
        String text = "<h1>Random text</h1>";
        String actual = processing.sanitize(text);

        Assertions.assertEquals("Random text", actual);
    }
}
