package org.example.processing;

import org.example.database.SessionManager;
import org.example.interfaces.InputOutput;
import org.example.interfaces.TransactionalOperation;
import org.example.web.FishTextApi;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

/**
 * Проверяем команды
 */
public class CommandHandlerTest {
    private InputOutput inputOutputMock;
    private CommandHandler commandHandler;
    private FishTextApi fishTextApiMock;
    private SessionManager sessionManagerMock;
    private Session sessionMock;
    /**
     * Инициализируем тестовую среду
     */
    @BeforeEach
    void setUp() {
        inputOutputMock = mock(InputOutput.class);
        fishTextApiMock = mock(FishTextApi.class);
        sessionManagerMock = mock(SessionManager.class);
        sessionMock = mock(Session.class);

        doAnswer(invocation -> {
            TransactionalOperation operation = invocation.getArgument(0);
            operation.execute(sessionMock);
            return null;
        }).when(sessionManagerMock)
                .executeInTransaction(any(TransactionalOperation.class));

        commandHandler = new CommandHandler(inputOutputMock, fishTextApiMock, sessionManagerMock);
    }

    /**
     * Проверяет, что команда "/help" выводит корректный текст справки
     */
    @Test
    void handleCommand_Help_ShouldOutputHelpText() {
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
        when(inputOutputMock.input()).thenReturn("1000");
        commandHandler.handleCommand("/settings");

        when(fishTextApiMock.getProcessedText())
                .thenReturn("Some text");

        when(inputOutputMock.input())
                .thenReturn("");
        commandHandler.handleCommand("/start");

        verify(inputOutputMock, atLeastOnce()).output(anyString());
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
     * Проверяет обработку некорректного ввода времени в команде "/settings"
     * Ожидается вывод сообщения об ошибке
     */
    @Test
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
        when(inputOutputMock.input()).thenReturn("0");
        commandHandler.handleCommand("/settings");
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
}
