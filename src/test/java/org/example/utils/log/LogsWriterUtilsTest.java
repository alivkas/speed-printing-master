package org.example.utils.log;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Тест класса LogsWriterUtils
 */
class LogsWriterUtilsTest {
    private static final String FILE_NAME = "testTrace.log";
    private LogsWriterUtils logsWriterUtils;

    /**
     * Установить значения для тестов и удалить логи, появившиеся в результате работы других тестов
     *
     */
    @BeforeEach
    public void setUp() {
        logsWriterUtils = new LogsWriterUtils(FILE_NAME);
    }

    /**
     * Удалить файл после окончания теста
     */
    @AfterEach
    public void tearDown() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Тестировать запись логов стек трейса ошибок
     */
    @Test
    public void testWriteStackTraceToFile() throws IOException {
        Exception testException = new Exception("Это тестовое исключение");
        logsWriterUtils.writeStackTraceToFile(testException);

        StringBuilder fileContent = new StringBuilder();
        String line;
        try(BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));) {
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
        }
        Pattern pattern = Pattern.compile("Произошло исключение\\s+java\\.lang\\.Exception: Это тестовое исключение",
                Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(fileContent.toString());

        Assertions.assertTrue(matcher.find(), "Ожидаемый шаблон не найден в файле");
    }
}