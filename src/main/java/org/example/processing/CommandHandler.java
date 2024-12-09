package org.example.processing;

import org.apache.log4j.Logger;
import org.example.animation.Animation;
import org.example.commons.Commands;
import org.example.commons.Time;
import org.example.database.SessionManager;
import org.example.interfaces.InputOutput;
import org.example.service.UserAuth;
import org.example.service.UserStatistics;
import org.example.service.UserTraining;
import org.example.training.TrainingProcess;
import org.example.web.FishTextApi;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Класс для обработки команд пользователя.
 */
public class CommandHandler {
    private final Logger logger = Logger.getLogger(CommandHandler.class);
    private final SessionManager sessionManager = new SessionManager();
    private final UserStatistics userStatistics = new UserStatistics();
    private final UserAuth userAuth;
    private final UserTraining userTraining;
    private final InputOutput inputOutput;
    private final FishTextApi fishTextApi;

    private TrainingProcess trainingProcess;
    private String currentUsername = null;

    /**
     * Конструктор класса CommandHandler, который получает ссылку на объект fishTextApi
     * и реализацию интерфейса InputOutput, также инициализирует UserTraining и UserAuth
     */
    public CommandHandler(InputOutput inputOutput, FishTextApi fishTextApi) {
        this.inputOutput = inputOutput;
        this.fishTextApi = fishTextApi;

        this.userTraining = new UserTraining(inputOutput);
        this.userAuth = new UserAuth(inputOutput);
    }

    /**
     * Обрабатывает команды, введенные пользователем.
     * @param command Команда, введенная пользователем.
     */
    public void handleCommand(String command) {
        try (Session session = sessionManager.getSession()) {
            final Transaction transaction = session.beginTransaction();
            try {
                switch (command) {
                    case Commands.HELP -> sendHelp();
                    case Commands.SETTINGS -> {
                        askTrainingTime(session);
                        transaction.commit();
                    }
                    case Commands.START -> {
                        startTraining(session);
                        transaction.commit();
                    }
                    case Commands.REGISTRATION -> {
                        register(session);
                        transaction.commit();
                    }
                    case Commands.LOGIN -> login(session);
                    case Commands.INFO   -> {
                        String userInfo = userStatistics.getUserInfo(currentUsername, session);
                        inputOutput.output(userInfo);
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
        } catch (IllegalStateException e) {
            logger.error(e.getMessage(), e);
            inputOutput.output("Нет доступа к базе данных");
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
            /info - Информация о пользователе
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
        } else if (!username.isEmpty()) {
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
        } else if (currentUsername != null) {
            inputOutput.output("Неверный логин или пароль.");
        }
    }

    /**
     * Запрашивает у пользователя время тренировки в минутах и устанавливает его.
     * time - время в минутах
     * @param session текущая сессия
     */
    private void askTrainingTime(Session session) {
        inputOutput.output("Укажите время на тренировку (минуты)");
        try {
            int time = Integer.parseInt(inputOutput.input());
            double millisecondsTime = time * Time.MINUTES_IN_MILLISECONDS;
            if (time <= 0) {
                inputOutput.output("Время тренировки должно быть положительным числом.");
                return;
            }
            userTraining.saveUsersTrainingTime(millisecondsTime, currentUsername, session);
            inputOutput.output("Время тренировки " + time + " минут");
        } catch (NumberFormatException e) {
            logger.error("Некорректный ввод. Введите целое положительное число.", e);
            inputOutput.output("Некорректный ввод. Введите целое положительное число.");
        }
    }

    /**
     * Запускает тренировку на установленное время.
     * @param session текущая сессия
     */
    private void startTraining(Session session) {
        if (userTraining.getUserTrainingTime(currentUsername, session) == 0) {
            inputOutput.output("Установите время тренировки с помощью команды /settings.");
            return;
        }

        Animation animation = new Animation(inputOutput);
        animation.countingDown();

        trainingProcess = new TrainingProcess(
                inputOutput,
                fishTextApi,
                currentUsername);

        trainingProcess.process(session);
    }
}