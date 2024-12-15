package org.example.training;

import org.example.commons.CommandsConst;
import org.example.database.dao.UserDao;
import org.example.interfaces.InputOutput;
import org.example.service.UserStatistics;
import org.example.service.UserTraining;
import org.example.utils.text.TextInteractionUtils;
import org.example.text.Typo;
import org.example.web.FishTextApi;
import org.hibernate.Session;

/**
 * Процесс тренировки
 */
public class TrainingProcess {

    private final TextInteractionUtils textInteractionUtils = new TextInteractionUtils();
    private final Typo typo = new Typo();
    private final UserStatistics userStatistics = new UserStatistics();
    private final UserDao userDao = new UserDao();
    private final UserTraining userTraining;
    private final FishTextApi fishTextApi;
    private final InputOutput inputOutput;

    private TrainingSession session;

    /**
     * Конструктор TrainingProcess, который передает ссылки на объект fishTextApi,
     * строку username, реализацию интерфейса InputOutput и инициализирует TrainingSession
     * c UserTraining
     * @param inputOutput ссылка на реализацию InputOutput
     */
    public TrainingProcess(InputOutput inputOutput) {
        this.inputOutput = inputOutput;
        this.session = new TrainingSession(inputOutput);
        this.fishTextApi = new FishTextApi(inputOutput);
        this.userTraining = new UserTraining(userDao);
    }

    /**
     * Производит процесс тренировки пользователя
     * @param sessionDb текущая сессия
     * @param username имя текущего пользователя
     */
    public void process(Session sessionDb, String username) {
        int trainingTime = userTraining.getUserTrainingTime(username, sessionDb);
        int wordsCount = 0;
        session.start(trainingTime);

        while (session.isActive()) {
            String processedText = fishTextApi.getProcessedText();
            if (processedText == null) {
                stopTraining();
                break;
            }

            inputOutput.output(processedText);
            String input = inputOutput.input();

            if (input.equals(CommandsConst.STOP)) {
                stopTraining();
                break;
            }
            wordsCount += textInteractionUtils.getWordsCount(input);
            if (!processedText.equals(input)) {
                typo.saveTypo(processedText, input);
            }
        }

        userStatistics.saveUserRating(username, sessionDb);
        userTraining.updateTrainingData(
                username,
                wordsCount,
                typo.countNumberOfTypos(),
                sessionDb);

        Result result = new Result(wordsCount,
                typo,
                inputOutput,
                trainingTime);
        result.printResult();
        typo.clearTypo();
    }

    /**
     * Прерывает тренировку, если она активна.
     */
    private void stopTraining() {
        if (session != null) {
            session.stop();
            session = null;
        }
    }
}
