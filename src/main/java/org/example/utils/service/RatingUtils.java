package org.example.utils.service;

/**
 * Построение рейтинга
 */
public class RatingUtils {
    /**
     * Подсчет рейтинга
     * @param sumTrainingCount сумма количества тренировок
     * @param sumTypoCount сумма количества ошибок за тренировки
     * @param sumAverageTime сумма среднего времени тренировки
     * @return число рейтинга
     */
    public double ratingCalculation(int sumTrainingCount, int sumTypoCount, double sumAverageTime) {
        double allTrainingsAverageTime = sumAverageTime / sumTypoCount;
        return ((double) sumTrainingCount / sumTypoCount + 1) * allTrainingsAverageTime;
    }
}
