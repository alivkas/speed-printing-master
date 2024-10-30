package org.example.training;

/**
 * Класс для хранения настроек тренировки.
 */
public class TrainingSettings {
    private int trainingTime;

    /**
     * Установка времени на тренировку
     * @param minutes время на тренировку
     */
    public void setTrainingTime(int minutes){
        this.trainingTime = minutes;
    }

    /**
     * Получение установленного времени на тренировку
     * @return время на тренеровку
     */
    public int getTrainingTime() {
        return trainingTime;
    }
}
