package org.example.training;

/**
 * Класс для хранения настроек тренировки
 */
public class
TrainingSettings {

    /**
     * Время тренировки в минутах
     */
    private int trainingTime;

    /**
     * Устанавливает время тренировки в минутах
     * @param minutes Время тренировки
     */
    public void setTrainingTime(int minutes) {
        this.trainingTime = minutes;
    }

    /**
     * Возвращает установленное время тренировки в минутах
     * @return Время тренировки в минутах
     */
    public int getTrainingTime() {
        return trainingTime;
    }
}