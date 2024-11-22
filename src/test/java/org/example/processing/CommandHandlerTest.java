package org.example.processing;


import org.example.animation.Animation;
import org.example.interfaces.InputOutput;
import org.example.training.TrainingProcess;
import org.example.training.TrainingSession;
import org.example.training.TrainingSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Проверяем команды
 */
public class CommandHandlerTest {
    private InputOutput mockOutput; // Мок для ввода/вывода
    private CommandHandler commandHandler; // Тестируемый объект
    private TrainingSettings trainingSettings; // Настройки тренировки

    @BeforeEach
    void setUp() {
        mockOutput = mock(InputOutput.class);
        when(mockOutput.input()).thenReturn("");
        commandHandler = new CommandHandler(mockOutput);
        trainingSettings = new TrainingSettings();

        commandHandler.trainingSettings = trainingSettings; // Устанавливаем настройки
    }

    /**
     * Обработка команды "/help" должна вывести текст справки
     */
    @Test
    void handleCommand_Help_ShouldOutputHelpText() {
        String helpText = """
                /help - Все команды
                /settings - Настройки тренировки
                /start - Начать тренировку
                /stop - Прервать тренировку
                /exit - Завершить приложение
                """;

        commandHandler.handleCommand("/help");
        verify(mockOutput).output(helpText);
    }

    /**
     * Тест для проверки запуска тренировки с установленным временем.
     * Проверяет, что сессия и процесс тренировки создаются,
     * и что метод output вызывается хотя бы 1 раз.
     */
    @Test
    void testStartTrainingWithSettingTime() {
        trainingSettings.setTrainingTime(1);

        when(mockOutput.input()).thenReturn("");
        commandHandler.handleCommand("/start");

        assertNotNull(commandHandler.trainingSession);
        assertNotNull(commandHandler.trainingProcess);

        verify(mockOutput, atLeastOnce()).output(anyString());
    }

    /**
     * Обработка команды "/settings" с правильным вводом, должна установить время тренировки.
     */
    @Test
    void correct_Time_Test() {
        when(mockOutput.input()).thenReturn("30");
        commandHandler.handleCommand("/settings");
        verify(mockOutput).output("Укажите время на тренировку (минуты)");
        verify(mockOutput).output("Время тренировки 30 минут");
    }

    /**
     * Обработка команды "/settings" с неправильным вводом, должна вывести сообщение об ошибке.
     */
    @Test
    void not_correct_Time_Test() {
        when(mockOutput.input()).thenReturn("abc");
        commandHandler.handleCommand("/settings");
        verify(mockOutput).output("Укажите время на тренировку (минуты)");
        verify(mockOutput).output("Некорректный ввод. Введите целое положительное число.");
    }

    /**
     * Обработка команды "/settings" с отрицательным вводом, должна вывести сообщение об ошибке.
     */
    @Test
    void negative_Time_Test() {
        when(mockOutput.input()).thenReturn("-5");
        commandHandler.handleCommand("/settings");
        verify(mockOutput).output("Укажите время на тренировку (минуты)");
        verify(mockOutput).output("Время тренировки должно быть положительным числом.");
    }

    /**
     * Обработка команды "/start" без установленного времени тренировки должна вывести сообщение об ошибке.
     */
    @Test
    void handleCommand_Start_WithoutTrainingTime_OutputsError() {
        trainingSettings.setTrainingTime(0);
        commandHandler.handleCommand("/start");
        verify(mockOutput).output("Установите время тренировки с помощью команды /settings.");
    }

    /**
     * Обработка команды "/stop" должна прервать активную тренировку, если она есть.
     */
    @Test
    void testStopTrainingWithActiveSession() {
        TrainingSession mockSession = Mockito.mock(TrainingSession.class);
        commandHandler.trainingSession = mockSession;
        commandHandler.handleCommand("/stop");
        verify(mockSession).stop();
        verify(mockOutput, never()).output("Нет активной тренировки.");
    }

    /**
     * Обработка команды "/stop" должна вывести сообщение, если тренировка не активна.
     */
    @Test
    void testStopTrainingWithoutActiveSession() {
        commandHandler.trainingSession = null;
        commandHandler.handleCommand("/stop");
        verify(mockOutput).output("Нет активной тренировки.");
    }

    /**
     * Обработка неизвестной команды должна вывести сообщение об ошибке и предложение ввести /help.
     */
    @Test
    void testHandleUnknownCommand() {
        String unknownCommand = "unknown_command";
        commandHandler.handleCommand(unknownCommand);
        verify(mockOutput).output("Неизвестная команда. Введите /help для списка команд.");
    }
}
