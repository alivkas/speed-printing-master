package org.example.training;

/**
 * Класс для хранения настроек тренировки
 */
public class TrainingSettings {

    /**
     * Время тренировки
     */
    private int trainingTime;

    /**
     * Устанавливает время тренировки в миллисекундах
     * @param milliseconds время тренировки
     */
    public void setTrainingTime(int milliseconds) {
        this.trainingTime = milliseconds;
    }

    /**
     * Возвращает установленное время тренировки в миллисекундах
     * @return время тренировки в миллисекундах
     */
    public int getTrainingTime() {
        return trainingTime;
    }
}