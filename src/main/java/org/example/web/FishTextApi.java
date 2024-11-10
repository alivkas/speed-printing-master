package org.example.web;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Взаимодействие с внешним апи FishTextApi
 */
public class FishTextApi {

    private final String url = "https://fish-text.ru/get?type=title&format=html";

    /**
     * Получить сгенерированное предложение из GET запроса к FishTextApi
     * @return сгенерированное предложение
     */
    public String getTextFromFishTextApi() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка вывода генерации предложения", e);
        }
        return null;
    }
}
