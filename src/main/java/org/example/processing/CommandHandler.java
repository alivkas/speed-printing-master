package org.example.processing;

import org.example.interfaces.InputOutput;
import org.example.training.TrainingProcess;
import org.example.training.TrainingSession;
import org.example.training.TrainingSettings;

import java.util.logging.Logger;

/**
 * Класс для обработки команд пользователя.
 */
public class CommandHandler {
    protected TrainingSettings trainingSettings;
    protected TrainingSession trainingSession;
    private final InputOutput output;
    private final int secondInMinute = 60;
    private final int millisecondsInSecond = 1000;

    /**
     * Конструктор класса CommandHandler, который инициализирует поле trainingSettings
     */
    public CommandHandler(InputOutput output) {
        this.output = output;
        this.trainingSettings = new TrainingSettings();
    }

    /**
     * Обрабатывает команды, введенные пользователем.
     *
     * @param command Команда, введенная пользователем.
     */
    public void handleCommand(String command) {
        switch (command) {
            case "/help":
                sendHelp();
                break;
            case "/settings":
                askTrainingTime();
                break;
            case "/stop":
                stopTraining();
                break;
            case "/start":
                startTraining();
                break;
            case "/exit":
                output.output("Выход из приложения.");
                System.exit(0);
                break;
            default:
                output.output("Неизвестная команда. Введите /help для списка команд.");
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
        output.output(helpText);
    }

    /**
     * Запрашивает у пользователя время тренировки в минутах и устанавливает его
     * time - время в минутах
     */
    private void askTrainingTime() {
        output.output("Укажите время на тренировку (минуты)");

        try {
            int time = Integer.parseInt(output.input());
            if (time <= 0) {
                output.output("Время тренировки должно быть положительным числом.");
                return;
            }
            trainingSettings.setTrainingTime(time);
            output.output("Время тренировки " + time + " минут");
        } catch (NumberFormatException numberFormatException) {
            output.output("Некорректный ввод. Введите целое положительное число.");
            Logger.getLogger(CommandHandler.class.getName()).warning(numberFormatException.getMessage());
        }
    }

    /**
     * Прерывает тренировку, если она активна.
     */
    private void stopTraining() {
        if (trainingSession != null) {
            trainingSession.stop();
            trainingSession = null;
        } else {
            output.output("Нет активной тренировки.");
        }
    }

    /**
     * Запускает тренировку на установленное время.
     */
    private void startTraining() {
        if (trainingSettings.getTrainingTime() == 0) {
            output.output("Установите время тренировки с помощью команды /settings.");
            return;
        }

        int durationMilliseconds = trainingSettings.getTrainingTime() * secondInMinute * millisecondsInSecond;
        trainingSession = new TrainingSession(durationMilliseconds, output);

        TrainingProcess trainingProcess = new TrainingProcess(this, trainingSession, trainingSettings);
        trainingProcess.process();
    }
}