package org.example.training;

import org.example.commons.Commands;
import org.example.database.DatabaseManager;
import org.example.interfaces.InputOutput;
import org.example.service.UserTraining;
import org.example.utils.text.TextInteractionUtils;
import org.example.text.Typo;
import org.example.web.FishTextApi;

/**
 * Процесс тренировки
 */
public class TrainingProcess {

    private final TextInteractionUtils textInteractionUtils = new TextInteractionUtils();
    private final Typo typo = new Typo();
    private final UserTraining userTraining;
    private TrainingSession session;
    private final TrainingSettings settings;
    private final FishTextApi fishTextApi;
    private final InputOutput inputOutput;
    private final String username;
    private final DatabaseManager databaseManager;

    /**
     * Конструктор TrainingProcess, который передает ссылки на объекты session,
     * settings, fishTextApi и реализацию InputOutput
     * @param session ссылка на объект TrainingSession
     * @param settings ссылка на объек TrainingSettings
     * @param inputOutput ссылка на реализацию InputOutput
     * @param fishTextApi ссылка на объект FishTextApi
     * @param databaseManager ссылка на управление бд
     */
    public TrainingProcess(TrainingSession session,
                           TrainingSettings settings,
                           InputOutput inputOutput,
                           FishTextApi fishTextApi,
                           String username,
                           DatabaseManager databaseManager) {
        this.session = session;
        this.settings = settings;
        this.inputOutput = inputOutput;
        this.fishTextApi = fishTextApi;
        this.username = username;
        this.databaseManager = databaseManager;

        this.userTraining = new UserTraining(databaseManager);
    }

    /**
     * Производит процесс тренировки
     */
    public void process() {
        int wordsCount = 0;
        session.start();

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
        userTraining.updateTrainingData(username, settings.getTrainingTime(), wordsCount);

        Result result = new Result(wordsCount, settings, typo, inputOutput);
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
