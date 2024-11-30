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

        this.userAuth = new UserAuth(databaseManager);
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
            case Commands.REGISTRATION -> register();
            case Commands.LOGIN -> login();
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
     */
    private void register() {
        try (Session session = databaseManager.getSession()) {
            Transaction transaction = session.beginTransaction();
            inputOutput.output("Введите имя пользователя: ");
            String username = inputOutput.input();
            inputOutput.output("Введите пароль: ");
            String password = inputOutput.input();

            boolean isSuccess = userAuth.registerUser(username, password);

            if (isSuccess) {
                transaction.commit();
                inputOutput.output("Регистрация прошла успешно! Войдите в аккаунт.");
            } else {
                transaction.rollback();
                inputOutput.output("Пользователь с таким именем уже существует.");
            }
        } catch (TransactionException e) {
            logsWriter.writeStackTraceToFile(e);
            throw new TransactionException("Ошибка транзакции");
        }
    }

    /**
     * Выполняет вход пользователя в систему
     */
    private void login() {
        try (Session session = databaseManager.getSession()) {
            Transaction transaction = session.beginTransaction();
            inputOutput.output("Введите имя пользователя: ");
            String username = inputOutput.input();
            inputOutput.output("Введите пароль: ");
            String password = inputOutput.input();

            boolean isSuccess = userAuth.loginUser(username, password);

            if (isSuccess) {
                transaction.commit();
                inputOutput.output("Вход выполнен!");
                currentUsername = username;
                trainingSettings.setTrainingTime(0);
            } else {
                transaction.rollback();
                inputOutput.output("Неверный логин или пароль.");
            }
        } catch (TransactionException e) {
            logsWriter.writeStackTraceToFile(e);
            throw new TransactionException("Ошибка транзакции");
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
     */
    private void startTraining() {
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
                currentUsername,
                databaseManager);

        try (Session session = databaseManager.getSession()) {
            Transaction transaction = session.beginTransaction();
            trainingProcess.process(session);
            transaction.commit();
        } catch (TransactionException e) {
            logsWriter.writeStackTraceToFile(e);
            throw new TransactionException("Ошибка транзакции");
        }
    }
}