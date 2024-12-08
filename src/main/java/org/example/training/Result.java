package org.example.training;

import org.example.commons.Time;
import org.example.interfaces.InputOutput;
import org.example.service.UserTraining;
import org.example.text.Typo;
import org.hibernate.Session;

import java.util.List;

/**
 * Результат тренировки
 */
public class Result {

    private final UserTraining userTraining = new UserTraining();
    private final int totalWordsTyped;
    private final Typo typo;
    private final InputOutput inputOutput;
    private final String username;
    private final Session session;

    /**
     * Конструктор Result, который передает ссылки на объекты totalWordsTyped, settings,
     * typo, session, реализацию InputOutput и строку username
     * @param totalWordsTyped количество введенных слов
     * @param typo ссылка на объект Typo
     * @param inputOutput ссылка на реализацию InputOutput
     * @param username имя пользователя
     * @param session текущая сессия
     */
    public Result(int totalWordsTyped,
                  Typo typo,
                  InputOutput inputOutput,
                  String username,
                  Session session) {
        this.typo = typo;
        this.totalWordsTyped = totalWordsTyped;
        this.inputOutput = inputOutput;
        this.username = username;
        this.session = session;
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
        double trainingTime = userTraining.getUserTrainingTime(username, session);
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
