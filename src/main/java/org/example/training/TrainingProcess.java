package org.example.training;

import org.example.animation.Animation;
import org.example.text.TextInteraction;
import org.example.processing.CommandHandler;
import org.example.processing.ResponseProcessing;
import org.example.text.Typo;
import org.example.web.Api;

import java.util.Scanner;

/**
 * Процесс тренировки
 */
public class TrainingProcess {

    private final ResponseProcessing responseProcessing;
    private final Api api;
    private final Scanner scanner;
    private final TextInteraction textInteraction;
    private final CommandHandler commandHandler;
    private final TrainingSession session;
    private final TrainingSettings settings;
    private final Typo typo;
    private final Animation animation;

    /**
     * Конструктор TrainingProcess, который инициализирует поля api, responseProcessing, scanner, textInteraction,
     * typo, animaation и передает ссылки на объекты commandHandler, session, settings
     * @param commandHandler ссылка на объект CommandHandler
     * @param session ссылка на объект TrainingSession
     * @param settings ссылка на объек TrainingSettings
     */
    public TrainingProcess(CommandHandler commandHandler,
                           TrainingSession session,
                           TrainingSettings settings) {
        this.api = new Api();
        this.responseProcessing = new ResponseProcessing();
        this.scanner = new Scanner(System.in);
        this.textInteraction = new TextInteraction();
        this.typo = new Typo();
        this.animation = new Animation();

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
            String apiText = api.getApi();
            String processedText = responseProcessing.cutText(apiText);

            System.out.println(processedText);
            String input = scanner.nextLine();

            if (input.equals("/stop")) {
                commandHandler.handleCommand("/stop");
                break;
            }
            wordsCount += textInteraction.wordsCountIncrease(input);
            if (!textInteraction.compareText(processedText, input)) {
                typo.saveTypo(processedText, input);
            }
        }

        new Result(wordsCount, settings, typo).printResult();
        typo.clearTypoCount();
        typo.clearTypo();
    }
}
