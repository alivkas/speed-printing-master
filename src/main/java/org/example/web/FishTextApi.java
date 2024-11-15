package org.example.web;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.example.utils.log.LogsWriterUtils;
import org.example.utils.processing.ResponseProcessingUtils;

import java.io.IOException;

/**
 * Взаимодействие с внешним апи FishTextApi
 */
public class FishTextApi {
    private final String URL = "https://fish-text.ru/get?type=title&format=html";

    private final LogsWriterUtils logsWriter = new LogsWriterUtils();

    /**
     * Получить сгенерированное предложение из GET запроса к FishTextApi
     * @return сгенерированное предложение
     */
    private String getTextFromFishTextApi() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    return response.body().string();
                }
            } else {
                return "Ошибка получения данных из API";
            }
        } catch (IOException e) {
            logsWriter.writeStackTraceToFile(e);
            return "Нет подключения к интернету";
        }
        return null;
    }

    /**
     * Получить обработанный текст
     * @return обрезанный текст от html тегов
     */
    public String getProcessedText() {
        ResponseProcessingUtils responseProcessingUtils = new ResponseProcessingUtils();
        String processedText;
        try {
            processedText = responseProcessingUtils.sanitize(getTextFromFishTextApi());
        } catch (NullPointerException e) {
            logsWriter.writeStackTraceToFile(e);
            return "Нет данных из API";
        }

        return processedText;
    }
}
