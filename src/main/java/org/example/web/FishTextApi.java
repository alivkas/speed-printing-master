package org.example.web;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.example.commons.LogsFile;
import org.example.utils.log.LogsWriterUtils;
import org.example.utils.processing.ResponseProcessingUtils;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Взаимодействие с внешним апи FishTextApi
 */
public class FishTextApi {
    private final static String URL = "https://fish-text.ru/get?type=title&format=html";

    private final LogsWriterUtils logsWriter = new LogsWriterUtils(LogsFile.FILE_NAME);
    private final Logger logger = Logger.getLogger(FishTextApi.class.getName());

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
            logsWriter.writeStackTraceToFile(e);
            logger.log(Level.SEVERE, "Нет подключения к интернету");
        } catch (IOException e) {
            logsWriter.writeStackTraceToFile(e);
            logger.log(Level.SEVERE, "Ошибка чтения строки");
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
            logsWriter.writeStackTraceToFile(e);
            logger.log(Level.SEVERE, "Нет данных из API");
        }

        return processedText;
    }
}
