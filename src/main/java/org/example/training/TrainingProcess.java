package org.example.training;

import org.example.animation.Animation;
import org.example.text.utils.TextInteractionUtils;
import org.example.processing.CommandHandler;
import org.example.processing.utils.ResponseProcessingUtils;
import org.example.text.Typo;
import org.example.web.FishTextApi;

import java.util.Scanner;

/**
 * Процесс тренировки
 */
public class TrainingProcess {

    private final ResponseProcessingUtils responseProcessingUtils = new ResponseProcessingUtils();
    private final TextInteractionUtils textInteractionUtils = new TextInteractionUtils();
    private final FishTextApi fishTextApi = new FishTextApi();
    private final Typo typo = new Typo();
    private final Animation animation = new Animation();
    private final Scanner scanner;
    private final CommandHandler commandHandler;
    private final TrainingSession session;
    private final TrainingSettings settings;

    /**
     * Конструктор TrainingProcess, который передает ссылки на объекты commandHandler, session, settings
     * @param commandHandler ссылка на объект CommandHandler
     * @param session ссылка на объект TrainingSession
     * @param settings ссылка на объек TrainingSettings
     */
    public TrainingProcess(CommandHandler commandHandler, TrainingSession session, TrainingSettings settings) {
        this.scanner = new Scanner(System.in);

        this.commandHandler = commandHandler;
        this.session = session;
        this.settings = settings;
    }

    /**
     * Производит процесс тренировки
     */
    public void process() {
        animation.countingDown();

        int wordsCount = 0;
        session.start();

        while (session.isActive()) {
            String apiText = fishTextApi.getTextFromFishTextApi();
            String processedText = responseProcessingUtils.sanitize(apiText);

            System.out.println(processedText);
            String input = scanner.nextLine();

            if (input.equals("/stop")) {
                commandHandler.handleCommand("/stop");
                break;
            }
            wordsCount += textInteractionUtils.getWordsCount(input);
            if (!processedText.equals(input)) {
                typo.saveTypo(processedText, input);
            }
        }

        new Result(wordsCount, settings, typo).printResult();
        typo.clearTypoAndTypoCount();
    }
}
