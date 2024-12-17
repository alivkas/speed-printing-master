package org.example.web;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.log4j.Logger;
import org.example.interfaces.InputOutput;
import org.example.utils.processing.ResponseProcessingUtils;

import java.io.IOException;
import java.net.UnknownHostException;


/**
 * Взаимодействие с внешним апи FishTextApi
 */
public class FishTextApi {
    private final static String URL = "https://fish-text.ru/get?type=title&format=html";

    private final Logger logger = org.apache.log4j.Logger.getLogger(FishTextApi.class);
    private final InputOutput inputOutput;

    /**
     * Конструктор FishTextApi, который получает ссылку на реализацию InputOutput
     * @param inputOutput реализация интерфейса InputOutput
     */
    public FishTextApi(InputOutput inputOutput) {
        this.inputOutput = inputOutput;
    }

    /**
     * Получить сгенерированное предложение из GET запроса к FishTextApi
     * @return сгенерированное предложение
     */
    private String getGeneratedText() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    return response.body().string();
                }
            }
        } catch (UnknownHostException e) {
            logger.error(e.getMessage(), e);
            inputOutput.output("Нет подключения к интернету");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            inputOutput.output("Ошибка чтения строки");
        }
        return null;
    }

    /**
     * Получить обработанный текст
     * @return обрезанный текст от html тегов
     */
    public String getProcessedText() {
        ResponseProcessingUtils responseProcessingUtils = new ResponseProcessingUtils();
        String processedText = null;
        try {
            processedText = responseProcessingUtils.sanitize(getGeneratedText());
        } catch (NullPointerException e) {
            logger.error(e.getMessage(), e);
            inputOutput.output("Нет данных из API");
        }

        return processedText;
    }
}
