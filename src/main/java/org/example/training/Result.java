package org.example.training;

import org.example.interfaces.InputOutput;
import org.example.text.Typo;

import java.util.List;

/**
 * Результат тренировки
 */
public class Result {

    private final int totalWordsTyped;
    private final TrainingSettings settings;
    private final Typo typo;
    private final InputOutput inputOutput;

    /**
     * Конструктор Result, который передает ссылки на объекты totalWordsTyped, settings,
     * typo и реализацию InputOutput
     * @param totalWordsTyped количество введенных слов
     * @param settings ссылка на обект TrainingSettings
     * @param typo ссылка на объект Typo
     * @param inputOutput ссылка на реализацию InputOutput
     */
    public Result(int totalWordsTyped, TrainingSettings settings, Typo typo, InputOutput inputOutput) {
        this.typo = typo;
        this.totalWordsTyped = totalWordsTyped;
        this.settings = settings;
        this.inputOutput = inputOutput;
    }

    /**
     * Вывести результат тренировки
     */
    public void printResult() {
        inputOutput.output(getWordsPerMinute());
        inputOutput.output("Количество ошибок: " + typo.countNumberOfTypos());
        inputOutput.output("Ошибки:");
        inputOutput.output(getTyposWithMarks());
    }

    /**
     * Получить количество слов в минуту
     * @return количество слов в минуту с припиской "Итоговая скорость печати: "
     */
    public String getWordsPerMinute() {
        double wordsPerMinute = ((double) totalWordsTyped / (settings.getTrainingTime()));
        return String.format("Итоговая скорость печати: %s слов/мин.", wordsPerMinute);
    }

    /**
     * Получить все опечатки с пометками их положения
     */
    private String getTyposWithMarks() {
        List<String> typos = typo.markTypo();
        StringBuilder result = new StringBuilder();

        for (String typoWithMarks : typos) {
            result.append(typoWithMarks).append("\n");
        }

        return result.toString();
    }
}
