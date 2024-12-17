package org.example.training;

import org.example.commons.Time;
import org.example.interfaces.InputOutput;
import org.example.text.Typo;

import java.util.List;

/**
 * Результат тренировки
 */
public class Result {

    private final int totalWordsTyped;
    private final int trainingTime;
    private final Typo typo;
    private final InputOutput inputOutput;

    /**
     * Конструктор Result, который передает ссылки на объекты totalWordsTyped, settings,
     * typo, session, реализацию InputOutput и строку username, также инициализирует
     * UserTraining
     * @param totalWordsTyped количество введенных слов
     * @param typo ссылка на объект Typo
     * @param inputOutput ссылка на реализацию InputOutput
     * @param trainingTime время тренировки пользователя
     */
    public Result(int totalWordsTyped,
                  Typo typo,
                  InputOutput inputOutput,
                  int trainingTime) {
        this.typo = typo;
        this.totalWordsTyped = totalWordsTyped;
        this.inputOutput = inputOutput;
        this.trainingTime = trainingTime;
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
    private String getWordsPerMinute() {
        double wordsPerMinute = ((double) totalWordsTyped
                / (trainingTime / Time.MINUTES_IN_MILLISECONDS));
        return String.format("Итоговая скорость печати: %.2f слов/мин.", wordsPerMinute);
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
