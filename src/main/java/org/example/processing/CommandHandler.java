package org.example.processing;

import org.example.animation.Animation;
import org.example.commons.Commands;
import org.example.commons.LogsFile;
import org.example.database.DatabaseManager;
import org.example.interfaces.InputOutput;
import org.example.service.UserAuth;
import org.example.training.TrainingProcess;
import org.example.training.TrainingSettings;
import org.example.utils.log.LogsWriterUtils;
import org.example.utils.processing.TimeProcessingUtils;
import org.example.web.FishTextApi;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Класс для обработки команд пользователя.
 */
public class CommandHandler {
    protected TrainingSettings trainingSettings = new TrainingSettings();
    private final LogsWriterUtils logsWriter = new LogsWriterUtils(LogsFile.FILE_NAME);
    private final DatabaseManager databaseManager = new DatabaseManager();
    private final TimeProcessingUtils timeProcess = new TimeProcessingUtils();
    protected final UserAuth userAuth;
    protected TrainingProcess trainingProcess;
    private final InputOutput inputOutput;
    private final FishTextApi fishTextApi;
    protected String currentUsername = null;

    /**
     * Конструктор класса CommandHandler, который получает ссылку на объект fishTextApi
     * и реализацию интерфейса InputOutput. Инициализирует UserAuth
     */
    public CommandHandler(InputOutput inputOutput, FishTextApi fishTextApi) {
        this.inputOutput = inputOutput;
        this.fishTextApi = fishTextApi;

        this.userAuth = new UserAuth();
    }

    /**
     * Обрабатывает команды, введенные пользователем.
     * @param command Команда, введенная пользователем.
     */
    public void handleCommand(String command) {
        try (Session session = databaseManager.getSession()) {
            final Transaction transaction = session.beginTransaction();
            try {
                switch (command) {
                    case Commands.HELP -> sendHelp();
                    case Commands.SETTINGS -> {
                        askTrainingTime();
                    }
                    case Commands.START -> {
                        startTraining(session);
                        transaction.commit();
                    }
                    case Commands.REGISTRATION -> {
                        register(session);
                        transaction.commit();
                    }
                    case Commands.LOGIN -> {
                        login(session);
                        transaction.commit();
                    }
                    case Commands.STOP -> inputOutput.output("Нет активной тренировки.");
                    case Commands.EXIT -> {
                        inputOutput.output("Выход из приложения.");
                        System.exit(0);
                    }
                    default -> inputOutput.output("Неизвестная команда. Введите /help для списка команд.");
                }
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException("Ошибка транзакции", e);
            }
        }
    }

    /**
     * Выводит список доступных команд.
     */
    private void sendHelp() {
        String helpText = """
            /help - Все команды
            /registration - зарегистрироваться
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
     * @param session текущая сессия
     */
    private void register(Session session) {
        inputOutput.output("Введите имя пользователя: ");
        String username = inputOutput.input();
        inputOutput.output("Введите пароль: ");
        String password = inputOutput.input();

        boolean isSuccess = userAuth.registerUser(username, password, session);

        if (isSuccess) {
            inputOutput.output("Регистрация прошла успешно! Войдите в аккаунт.");
        } else {
            inputOutput.output("Пользователь с таким именем уже существует.");
        }
    }

    /**
     * Выполняет вход пользователя в систему
     * @param session текущая сессия
     */
    private void login(Session session) {
        inputOutput.output("Введите имя пользователя: ");
        String username = inputOutput.input();
        inputOutput.output("Введите пароль: ");
        String password = inputOutput.input();

        boolean isSuccess = userAuth.loginUser(username, password, session);

        if (isSuccess) {
            inputOutput.output("Вход выполнен!");
            currentUsername = username;
            trainingSettings.setTrainingTime(0);
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
            int millisecondsTime = timeProcess.minutesToMilliseconds(time);
            if (time <= 0) {
                inputOutput.output("Время тренировки должно быть положительным числом.");
                return;
            }
            trainingSettings.setTrainingTime(millisecondsTime);
            inputOutput.output("Время тренировки " + time + " минут");
        } catch (NumberFormatException e) {
            inputOutput.output("Некорректный ввод. Введите целое положительное число.");
            logsWriter.writeStackTraceToFile(e);
        }
    }

    /**
     * Запускает тренировку на установленное время.
     * @param session текущая сессия
     */
    private void startTraining(Session session) {
        if (trainingSettings.getTrainingTime() == 0) {
            inputOutput.output("Установите время тренировки с помощью команды /settings.");
            return;
        }

        Animation animation = new Animation(inputOutput);
        animation.countingDown();

        trainingProcess = new TrainingProcess(
                trainingSettings,
                inputOutput,
                fishTextApi,
                currentUsername);

        trainingProcess.process(session);
    }
}