package org.example.utils.log;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

/**
 * Тест класса LogsWriterUtils
 */
class LogsWriterUtilsTest {
    private final String fileName = "trace.log";
    private LogsWriterUtils logsWriterUtils;

    /**
     * Установить значения для тестов
     */
    @BeforeEach
    public void setUp() {
        logsWriterUtils = new LogsWriterUtils();
    }

    /**
     * Удалить файл после окончания тестов
     */
    @AfterEach
    public void tearDown() {
        File file = new File(fileName);
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

        File file = new File(fileName);

        StringBuilder fileContent = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            fileContent.append(line).append("\n");
        }
        reader.close();

        Assertions.assertTrue(fileContent.toString().contains("Произошло исключение"));
        Assertions.assertTrue(fileContent.toString().contains("Это тестовое исключение"));
    }
}