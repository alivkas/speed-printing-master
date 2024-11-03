package org.example.training;

import org.example.text.Typo;

import java.util.Map;

/**
 * Результат тренировки
 */
public class Result {

    private final int totalWordsTyped;
    private final TrainingSettings settings;
    private final Typo typo;

    /**
     * Конструктор Result
     * @param totalWordsTyped количество введенных слов
     * @param settings ссылка на обект TrainingSettings
     * @param typo ссылка на объект Typo
     */
    public Result(int totalWordsTyped, TrainingSettings settings, Typo typo) {
        this.typo = typo;
        this.totalWordsTyped = totalWordsTyped;
        this.settings = settings;
    }

    /**
     * Вывести результат тренировки
     */
    public void printResult() {
        String wordsPerMinute = getWordsPerMinute();
        int typoCount = typo.getTypoCount();

        System.out.println(wordsPerMinute);
        System.out.println("Количество ошибок: " + typoCount);
        System.out.println("Ошибки:");
        getTyposWithMarks();
    }

    /**
     * Получить количество слов в минуту
     * @return количество слов в минуту с припиской "Итоговая скорость печати: "
     */
    private String getWordsPerMinute() {
        double wordsPerMinute = ((double) totalWordsTyped / (settings.getTrainingTime()));
        return String.format("Итоговая скорость печати: %.2f слов/мин.", wordsPerMinute);
    }

    /**
     * Получить все опечатки с пометками их положения
     * @return
     */
    private void getTyposWithMarks() {
        Map<String, String> typos = typo.markTypo();

        for (Map.Entry<String, String> typoWithMarks : typos.entrySet()) {
            String typoKey = typoWithMarks.getKey();
            String marksValue = typoWithMarks.getValue();

            System.out.printf("%s\n%s%n", typoKey, marksValue);
        }
    }
}
