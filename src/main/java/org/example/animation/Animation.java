package org.example.animation;

import org.example.commons.LogsFile;
import org.example.interfaces.InputOutput;
import org.example.utils.log.LogsWriterUtils;

/**
 * Анимация в выводе
 */
public class Animation {
    private final LogsWriterUtils logsWriter = new LogsWriterUtils(LogsFile.FILE_NAME);
    private final InputOutput inputOutput;
    private static final int COUNT = 3;

    /**
     * Конструктор, который передает ссылку на реализацию InputOutput
     * @param inputOutput реализация InputOutput
     */
    public Animation(InputOutput inputOutput) {
        this.inputOutput = inputOutput;
    }

    /**
     * Обратный отсчет до начала тренировки
     */
    public void countingDown() {
        for (int i = COUNT; i >= 1; i--) {
            inputOutput.output(i + "...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logsWriter.writeStackTraceToFile(e);
                throw new RuntimeException("Поток занят", e);
            }
        }
    }
}
