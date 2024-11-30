package org.example.utils.processing;

import org.example.commons.Time;

/**
 * Обработка времени
 */
public class TimeProcessingUtils {

    /**
     * Перевести минуты в миллисекунды
     * @param minute минуты
     * @return переведенные минуты в миллисекунды
     */
    public int minutesToMilliseconds(int minute) {
        return minute * Time.MILLISECONDS;
    }
}
