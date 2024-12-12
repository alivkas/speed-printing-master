package org.example.processing;

import org.apache.log4j.Logger;
import org.example.animation.Animation;
import org.example.commons.Commands;
import org.example.commons.Time;
import org.example.database.SessionManager;
import org.example.database.dao.UserDao;
import org.example.interfaces.InputOutput;
import org.example.service.UserAuth;
import org.example.service.UserStatistics;
import org.example.service.UserTraining;
import org.example.training.TrainingProcess;
import org.example.web.FishTextApi;
import org.hibernate.Session;

/**
 * Класс для обработки команд пользователя.
 */
public class CommandHandler {
    private final Logger logger = Logger.getLogger(CommandHandler.class);
    private final UserStatistics userStatistics = new UserStatistics();
    private final UserDao userDao = new UserDao();
    private final SessionManager sessionManager;
    private final UserAuth userAuth;
    private final UserTraining userTraining;
    private final InputOutput inputOutput;
    private final FishTextApi fishTextApi;

    private TrainingProcess trainingProcess;
    private String currentUsername = null;

    /**
     * Конструктор класса CommandHandler, который получает ссылку на объект fishTextApi
     * и реализацию интерфейса InputOutput, также инициализирует UserTraining и UserAuth
     *
     * @param inputOutput реализация интерфейса InputOutput
     * @param fishTextApi ссылка на внешний апи для получения текста
     * @param sessionManager ссылка на управление сессиями
     */
    public CommandHandler(InputOutput inputOutput,
                          FishTextApi fishTextApi,
                          SessionManager sessionManager) {
        this.inputOutput = inputOutput;
        this.fishTextApi = fishTextApi;
        this.sessionManager = sessionManager;

        this.userTraining = new UserTraining(inputOutput, userDao);
        this.userAuth = new UserAuth(inputOutput, userDao);
    }

    /**
     * Обрабатывает команды, введенные пользователем.
     * @param command Команда, введенная пользователем.
     */
    public void handleCommand(String command) {
        try (Session session = sessionManager.getSession()) {
            switch (command) {
                case Commands.HELP -> sendHelp();
                case Commands.SETTINGS -> sessionManager
                        .executeInTransaction(this::askTrainingTime);
                case Commands.START -> sessionManager
                        .executeInTransaction(this::startTraining);
                case Commands.REGISTRATION -> sessionManager
                        .executeInTransaction(this::register);
                case Commands.LOGIN -> login(session);
                case Commands.INFO -> {
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
            int millisecondsTime = time * Time.MINUTES_IN_MILLISECONDS;
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
                currentUsername,
                userDao);

        trainingProcess.process(session);
    }
}