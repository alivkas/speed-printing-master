package org.example.processing;

import org.example.training.TrainingProcess;
import org.example.training.TrainingSession;
import org.example.training.TrainingSettings;

import java.util.Scanner;

/**
 * Класс для обработки команд пользователя.
 */
public class CommandHandler {
    private final TrainingSettings trainingSettings;
    private TrainingSession trainingSession;

    /**
     * Конструктор класса CommandHandler.
     */
    public CommandHandler() {
        this.trainingSettings = new TrainingSettings();
    }

    /**
     * Обрабатывает команды, введенные пользователем.
     * @param command Команда, введенная пользователем.
     */
    public void handleCommand(String command) {
        switch (command) {
            case "/help":
                sendHelp();
                break;
            case "/settings":
                setTrainingTime();
                break;
            case "/stop":
                stopTraining();
                break;
            case "/start":
                startTraining();
                break;
            case "/exit":
                System.out.println("Выход из приложения.");
                System.exit(0);
                break;
            default:
                System.out.println("Неизвестная команда. Введите /help для списка команд.");
        }
    }

    /**
     * Список доступных команд
     */
    private void sendHelp() {
        String helpText = "/help - Все команды\n" +
                "/settings - Настройки тренировки\n" +
                "/start - Начать тренировку\n" +
                "/stop - Прервать тренировку\n" +
                "/exit - Завершить приложение\n";
        System.out.println(helpText);
    }

    /**
     * Запрос у пользователя времени тренировки и установки его.
     */
    private void setTrainingTime() {
        System.out.println("Укажите время на тренировку (минуты)");

        Scanner scanner = new Scanner(System.in);

        try {
            int time = Integer.parseInt(scanner.nextLine());
            if (time <= 0)
                throw new NumberFormatException();
            trainingSettings.setTrainingTime(time); // Устанавливаем время в минутах
            System.out.println("Время тренировки " + time + " минут");
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Введите корректное число");
        }
    }

    /**
     * Прерывание тренировки, если она активна.
     */
    private void stopTraining() {
        if (trainingSession != null) {
            trainingSession.stop();
            trainingSession = null;
        } else {
            System.out.println("Нет активной тренировки.");
        }
    }

    /**
     * Запуск тренировки на установленное время.
     */
    private void startTraining() {
        if (trainingSettings.getTrainingTime() == 0) {
            System.out.println("Установите время тренировки с помощью команды /settings.");
            return;
        }

        int durationMinutes = trainingSettings.getTrainingTime() * 60 * 1000;
        trainingSession = new TrainingSession(durationMinutes);

        TrainingProcess trainingProcess = new TrainingProcess(this, trainingSession, trainingSettings);
        trainingProcess.process();
    }
}