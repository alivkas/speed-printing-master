package org.example.processing;


import org.example.database.DatabaseManager;
import org.example.interfaces.InputOutput;
import org.example.service.UserAuth;
import org.example.training.TrainingSession;
import org.example.training.TrainingSettings;
import org.example.web.FishTextApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

/**
 * Проверяем команды
 */
public class CommandHandlerTest {
    private InputOutput inputOutputMock;
    private CommandHandler commandHandler;
    private TrainingSettings trainingSettings;
    private FishTextApi fishTextApiMock;

    @BeforeEach
    void setUp() {
        inputOutputMock = mock(InputOutput.class);
        fishTextApiMock = mock(FishTextApi.class);
        when(inputOutputMock.input()).thenReturn("");

        commandHandler = new CommandHandler(inputOutputMock, fishTextApiMock);
        trainingSettings = new TrainingSettings();

        commandHandler.trainingSettings = trainingSettings;
    }

    /**
     * Обработка команды "/help" должна вывести текст справки
     */
    InputOutput mockOutput = mock(InputOutput.class);
    DatabaseManager mockDatabaseManager = mock(DatabaseManager.class);

    @Mock
    private UserAuth userAuthMock;
    private CommandHandler commandHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commandHandler = new CommandHandler(mockOutput, mockDatabaseManager);
        commandHandler.userAuth = userAuthMock;
    }
    @Test
    void handleCommand_Help_ShouldOutputHelpText() {
        CommandHandler commandHandler = new CommandHandler(mockOutput, mockDatabaseManager);
        String helpText = """
            /help - Все команды
            /register - зарегистрироваться
            /login - войти в систему
            /settings - Настройки тренировки
            /start - Начать тренировку
            /stop - Прервать тренировку
            /exit - Завершить приложение
            """;

        commandHandler.handleCommand("/help");
        verify(inputOutputMock).output(helpText);
    }

    /**
     * Тест для проверки запуска тренировки с установленным временем.
     * Проверяет, что сессия и процесс тренировки создаются,
     * api отдает ответ без исключения
     * и метод output вызывается хотя бы 1 раз.
     */
    @Test
    public void testStartTrainingWithSettingTime() {
        trainingSettings.setTrainingTime(1);

        when(fishTextApiMock.getProcessedText())
                .thenReturn("Some text");

        when(inputOutputMock.input()).thenReturn("");
        commandHandler.handleCommand("/start");

        assertNotNull(commandHandler.trainingSession);
        assertNotNull(commandHandler.trainingProcess);

        verify(inputOutputMock, atLeastOnce()).output(anyString());
    }
    /**
     * Обработка команды "/settings" с правельным вводом, должна установить время тренировки
     */
    @Test
    void correct_Time_Test() {
        CommandHandler commandHandler = new CommandHandler(mockOutput, mockDatabaseManager);
        when(mockOutput.input()).thenReturn("30");
    public void testNoInternetConnection() {
        trainingSettings.setTrainingTime(1);

        when(fishTextApiMock.getProcessedText())
                .thenThrow(new RuntimeException("Нет подключения к интернету"));

        Exception exception = assertThrows(RuntimeException.class, () ->
                commandHandler.handleCommand("/start"));

        assertEquals("Нет подключения к интернету", exception.getMessage());
    }

    /**
     * Обработка команды "/settings" с правильным вводом, должна установить время тренировки.
     */
    @Test
    public void correct_Time_Test() {
        when(inputOutputMock.input()).thenReturn("30");
        commandHandler.handleCommand("/settings");
        verify(inputOutputMock).output("Укажите время на тренировку (минуты)");
        verify(inputOutputMock).output("Время тренировки 30 минут");
    }

    /**
     * Обработка команды "/settings" с неправильным вводом, должна вывести сообщение об ошибке.
     */
    @Test
    void not_correct_Time_Test() {
        CommandHandler commandHandler = new CommandHandler(mockOutput, mockDatabaseManager);
        when(mockOutput.input()).thenReturn("abc");

    public void not_correct_Time_Test() {
        when(inputOutputMock.input()).thenReturn("abc");
        commandHandler.handleCommand("/settings");
        verify(inputOutputMock).output("Укажите время на тренировку (минуты)");
        verify(inputOutputMock).output("Некорректный ввод. Введите целое положительное число.");
    }

    /**
     * Обработка команды "/settings" с отрицательным вводом, должна вывести сообщение об ошибке.
     */
    @Test
    public void negative_Time_Test() {
        when(inputOutputMock.input()).thenReturn("-5");

        commandHandler.handleCommand("/settings");
        verify(inputOutputMock).output("Укажите время на тренировку (минуты)");
        verify(inputOutputMock).output("Время тренировки должно быть положительным числом.");
    }

    /**
     * Обработка команды "/start" без установленного времени тренировки должна вывести сообщение об ошибке.
     */
    @Test
    public void handleCommand_Start_WithoutTrainingTime_OutputsError() {
        trainingSettings.setTrainingTime(0);
        commandHandler.handleCommand("/start");
        verify(inputOutputMock).output("Установите время тренировки с помощью команды /settings.");
    }

    /**
     * Обработка неизвестной команды должна вывести сообщение об ошибке и предложение ввести /help.
     */
    @Test
    public void testHandleUnknownCommand() {
        String unknownCommand = "unknown_command";
        commandHandler.handleCommand(unknownCommand);
        verify(inputOutputMock).output("Неизвестная команда. Введите /help для списка команд.");
    }
    @Test
    void handleCommand_register_success() {
        when(userAuthMock.registerUser(mockDatabaseManager)).thenReturn(true);
        commandHandler.handleCommand("/register");
        verify(userAuthMock).registerUser(mockDatabaseManager);
        verify(mockOutput).output("Регистрация прошла успешно! Войдите в аккаунт.");
    }


    @Test
    void handleCommand_register_failure() {
        when(userAuthMock.registerUser(mockDatabaseManager)).thenReturn(false);
        commandHandler.handleCommand("/register");
        verify(userAuthMock).registerUser(mockDatabaseManager);
        verify(mockOutput).output("Пользователь с таким именем уже существует.");
    }


    @Test
    void handleCommand_login_success() {
        when(userAuthMock.loginUser(mockDatabaseManager)).thenReturn(true);
        when(userAuthMock.getUsername()).thenReturn("testuser");
        commandHandler.handleCommand("/login");
        verify(userAuthMock).loginUser(mockDatabaseManager);
        verify(mockOutput).output("Вход выполнен!");
        assertEquals("testuser", commandHandler.currentUsername);
    }


    @Test
    void handleCommand_login_failure() {
        when(userAuthMock.loginUser(mockDatabaseManager)).thenReturn(false);
        commandHandler.handleCommand("/login");
        verify(userAuthMock).loginUser(mockDatabaseManager);
        verify(mockOutput).output("Неверный логин или пароль.");
        assertNull(commandHandler.currentUsername);
    }


}
