package org.example.processing;

/**
 * Обработка ответа
 */
public class ResponseProcessing {

    /**
     * Обрезать текст от html тегов
     * @param text полученный из запроса текст
     */
    public String cutText(String text) {
        return text.replace("<h1>", "")
                .replace("</h1>", "");
    }
}
