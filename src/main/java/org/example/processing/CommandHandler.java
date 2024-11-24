package org.example.processing;

import org.example.animation.Animation;
import org.example.commons.Commands;
import org.example.commons.LogsFile;
import org.example.interfaces.InputOutput;
import org.example.training.TrainingProcess;
import org.example.training.TrainingSession;
import org.example.training.TrainingSettings;
import org.example.utils.log.LogsWriterUtils;
import org.example.web.FishTextApi;

/**
 * Класс для обработки команд пользователя.
 */
public class CommandHandler {
    protected TrainingSettings trainingSettings = new TrainingSettings();
    private final LogsWriterUtils logsWriter = new LogsWriterUtils(LogsFile.FILE_NAME);
    protected TrainingSession trainingSession;
    protected TrainingProcess trainingProcess;
    private final InputOutput inputOutput;
    private final FishTextApi fishTextApi;

    /**
     * Конструктор класса CommandHandler, который получает ссылку на объект fishTextApi
     * и реализацию интерфейса InputOutput
     */
    public CommandHandler(InputOutput inputOutput, FishTextApi fishTextApi) {
        this.inputOutput = inputOutput;
        this.fishTextApi = fishTextApi;
    }

    /**
     * Обрабатывает команды, введенные пользователем.
     * @param command Команда, введенная пользователем.
     */
    public void handleCommand(String command) {
        switch (command) {
            case Commands.HELP -> sendHelp();
            case Commands.SETTINGS -> askTrainingTime();
            case Commands.START -> startTraining();
            case Commands.STOP -> {
                inputOutput.output("Нет активной тренировки.");
            }
            case Commands.EXIT -> {
                inputOutput.output("Выход из приложения.");
                System.exit(0);
            }
            default -> inputOutput.output("Неизвестная команда. Введите /help для списка команд.");
        }
    }

    /**
     * Выводит список доступных команд.
     */
    private void sendHelp() {
        String helpText = """
            /help - Все команды
            /settings - Настройки тренировки
            /start - Начать тренировку
            /stop - Прервать тренировку
            /exit - Завершить приложение
            """;
        inputOutput.output(helpText);
    }

    /**
     * Запрашивает у пользователя время тренировки в минутах и устанавливает его.
     * time - время в минутах
     */
    private void askTrainingTime() {
        inputOutput.output("Укажите время на тренировку (минуты)");
        try {
            int time = Integer.parseInt(inputOutput.input());
            if (time <= 0) {
                inputOutput.output("Время тренировки должно быть положительным числом.");
                return;
            }
            trainingSettings.setTrainingTime(time);
            inputOutput.output("Время тренировки " + time + " минут");
        } catch (NumberFormatException e) {
            inputOutput.output("Некорректный ввод. Введите целое положительное число.");
            logsWriter.writeStackTraceToFile(e);
        }
    }

    /**
     * Запускает тренировку на установленное время.
     */
    private void startTraining() {
        if (trainingSettings.getTrainingTime() == 0) {
            inputOutput.output("Установите время тренировки с помощью команды /settings.");
            return;
        }

        Animation animation = new Animation(inputOutput);
        animation.countingDown();

        trainingSession = new TrainingSession(trainingSettings, inputOutput);
        trainingProcess = new TrainingProcess(trainingSession, trainingSettings, inputOutput, fishTextApi);
        trainingProcess.process();
    }
}