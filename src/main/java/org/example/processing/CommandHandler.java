package org.example.processing;

import org.example.database.DatabaseManager;
import org.example.interfaces.InputOutput;
import org.example.service.UserAuth;
import org.example.training.TrainingProcess;
import org.example.training.TrainingSession;
import org.example.training.TrainingSettings;
import org.example.utils.log.LogsWriterUtils;

/**
 * Класс для обработки команд пользователя.
 */
public class CommandHandler {
    protected TrainingSettings trainingSettings = new TrainingSettings();
    private final LogsWriterUtils logsWriter = new LogsWriterUtils();

    protected TrainingSession trainingSession;
    private TrainingProcess trainingProcess;
    private final InputOutput inputOutput;
    private final DatabaseManager databaseManager;
    protected UserAuth userAuth;
    protected String currentUsername = null;

    /**
     * Конструктор класса CommandHandler, который инициализирует поле trainingSettings
     * и принимает класс, который реализует интерфейс ввода и вывода
     */
    public CommandHandler(InputOutput inputOutput, DatabaseManager databaseManager) {
        this.inputOutput = inputOutput;
        this.databaseManager = databaseManager;
        this.userAuth = new UserAuth(inputOutput);
    }

    /**
     * Обрабатывает команды, введенные пользователем.
     * @param command Команда, введенная пользователем.
     */
    public void handleCommand(String command) {
        switch (command) {
            case "/help" -> sendHelp();
            case "/settings" -> askTrainingTime();
            case "/stop" -> stopTraining();
            case "/start" -> startTraining();
            case "/register" -> register();
            case "/login" -> login();
            case "/exit" -> {
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
            /register - зарегистрироваться
            /login - войти в систему
            /settings - Настройки тренировки
            /start - Начать тренировку
            /stop - Прервать тренировку
            /exit - Завершить приложение
            """;
        inputOutput.output(helpText);
    }

    /**
     * Регистрирует нового пользователя в системе
     */
    private void register() {
        boolean success = userAuth.registerUser(databaseManager);

        if (success) {
            inputOutput.output("Регистрация прошла успешно! Войдите в аккаунт.");
        } else {
            inputOutput.output("Пользователь с таким именем уже существует.");
        }
    }

    /**
     * Выполняет вход пользователя в систему
     */
    private void login() {
        boolean success = userAuth.loginUser(databaseManager);

        if (success) {
            inputOutput.output("Вход выполнен!");
            currentUsername = userAuth.getUsername();
        } else {
            inputOutput.output("Неверный логин или пароль.");
        }
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
     * Прерывает тренировку, если она активна.
     */
    private void stopTraining() {
        if (trainingSession != null) {
            trainingSession.stop();
            trainingSession = null;
        } else {
            inputOutput.output("Нет активной тренировки.");
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

        trainingSession = new TrainingSession(trainingSettings, inputOutput);
        trainingProcess = new TrainingProcess(trainingSession, trainingSettings, inputOutput, currentUsername);
        trainingProcess.process();
    }
}