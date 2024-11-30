package org.example.utils.processing;

/**
 * Обработка ответа
 */
public class ResponseProcessingUtils {

    /**
     * Обрезать текст от html тегов
     * @param text полученный из запроса текст
     */
    public String sanitize(String text) {
        return text.replace("<h1>", "")
                .replace("</h1>", "");
    }
}
