package org.example.training;

import org.example.interfaces.InputOutput;
import org.example.utils.text.TextInteractionUtils;
import org.example.text.Typo;
import org.example.web.FishTextApi;

/**
 * Процесс тренировки
 */
public class TrainingProcess {

    private final TextInteractionUtils textInteractionUtils = new TextInteractionUtils();
    private final FishTextApi fishTextApi = new FishTextApi();
    private final Typo typo = new Typo();

    private final TrainingSession session;
    private final TrainingSettings settings;
    private final InputOutput inputOutput;

    /**
     * Конструктор TrainingProcess, который передает ссылки на объекты session,
     * settings и реализацию InputOutput
     * @param session ссылка на объект TrainingSession
     * @param settings ссылка на объек TrainingSettings
     * @param inputOutput ссылка на реализацию InputOutput
     */
    public TrainingProcess(TrainingSession session, TrainingSettings settings, InputOutput inputOutput) {
        this.session = session;
        this.settings = settings;
        this.inputOutput = inputOutput;
    }

    /**
     * Производит процесс тренировки
     */
    public void process() {
        int wordsCount = 0;
        session.start();

        while (session.isActive()) {
            String processedText = fishTextApi.getProcessedText();

            inputOutput.output(processedText);
            String input = inputOutput.input();

            if (input.equals("/stop")) {
                session.stop();
                break;
            }
            wordsCount += textInteractionUtils.getWordsCount(input);
            if (!processedText.equals(input)) {
                typo.saveTypo(processedText, input);
            }
        }

        new Result(wordsCount, settings, typo, inputOutput).printResult();
        typo.clearTypo();
    }
}
