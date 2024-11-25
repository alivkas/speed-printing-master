package org.example.utils.log;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Запись логов в файл
 */
public class LogsWriterUtils {
    private final String fileName;

    /**
     * Конструктор, принимающий название файла, в котором будет храниться стек трейс
     * @param fileName название файла
     */
    public LogsWriterUtils(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Записывать стек трейс в отдельный файл
     * @param e отловленная ошибка
     */
    public void writeStackTraceToFile(Exception e) {
        try(PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println("Произошло исключение");
            e.printStackTrace(writer);
        } catch (IOException ex) {
            throw new RuntimeException("Ошибка записи логов в файл trace.log", ex);
        }
    }
}
