package org.example.training;

import org.example.commons.Commands;
import org.example.database.dao.UserDao;
import org.example.interfaces.InputOutput;
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
    private final UserTraining userTraining;
    private final FishTextApi fishTextApi;
    private final InputOutput inputOutput;
    private final String username;
    private final UserDao userDao;

    private TrainingSession session;

    /**
     * Конструктор TrainingProcess, который передает ссылки на объект fishTextApi,
     * строку username, реализацию интерфейса InputOutput и инициализирует TrainingSession
     * c UserTraining
     * @param inputOutput ссылка на реализацию InputOutput
     * @param fishTextApi ссылка на объект FishTextApi
     * @param username имя текущего пользователя
     */
    public TrainingProcess(InputOutput inputOutput,
                           FishTextApi fishTextApi,
                           String username,
                           UserDao userDao) {
        this.inputOutput = inputOutput;
        this.fishTextApi = fishTextApi;
        this.username = username;
        this.userDao =userDao;

        this.session = new TrainingSession(inputOutput,new UserTraining(inputOutput, userDao));
        this.userTraining = new UserTraining(inputOutput, userDao);
    }

    /**
     * Производит процесс тренировки
     * @param sessionDb текущая сессия
     */
    public void process(Session sessionDb) {
        int wordsCount = 0;
        session.start(sessionDb, username);

        while (session.isActive()) {
            String processedText = fishTextApi.getProcessedText();
            if (processedText == null) {
                stopTraining();
                break;
            }

            inputOutput.output(processedText);
            String input = inputOutput.input();

            if (input.equals(Commands.STOP)) {
                stopTraining();
                break;
            }
            wordsCount += textInteractionUtils.getWordsCount(input);
            if (!processedText.equals(input)) {
                typo.saveTypo(processedText, input);
            }
        }

        userTraining.updateTrainingData(
                username,
                wordsCount,
                sessionDb);

        Result result = new Result(wordsCount,
                typo,
                inputOutput,
                username,
                sessionDb,
                userDao);
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
