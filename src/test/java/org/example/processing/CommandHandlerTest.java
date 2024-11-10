package org.example.processing;

import org.example.interfaces.InputOutput;
import org.example.training.TrainingSession;
import org.example.training.TrainingSettings;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

/**
 * Проверяем команды
 */
public class CommandHandlerTest {
    /**
     * Обработка команды "/help" должна вывести текст справки
     */
    @Test
    void handleCommand_Help_ShouldOutputHelpText() {
        InputOutput mockOutput = mock(InputOutput.class);
        CommandHandler commandHandler = new CommandHandler(mockOutput);
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
     * Обработка команды "/settings" с правельным вводом, должна установить время тренировки
     */
    @Test
    void correct_Time_Test() {
        InputOutput mockOutput = mock(InputOutput.class);
        CommandHandler commandHandler = new CommandHandler(mockOutput);
        when(mockOutput.input()).thenReturn("30");
        commandHandler.handleCommand("/settings");
        verify(mockOutput).output("Укажите время на тренировку (минуты)");
        verify(mockOutput).output("Время тренировки 30 минут");
    }

    /**
     * Обработка команды "/settings" с неправильным вводом, должна вывести сообщение об ошибке
     */
    @Test
    void not_correct_Time_Test() {
        InputOutput mockOutput = mock(InputOutput.class);
        CommandHandler commandHandler = new CommandHandler(mockOutput);
        when(mockOutput.input()).thenReturn("abc");

        commandHandler.handleCommand("/settings");

        verify(mockOutput).output("Укажите время на тренировку (минуты)");
        verify(mockOutput).output("Некорректный ввод. Введите целое положительное число.");
    }

    /**
     * Oбработка команды "/settings" с отрицательным вводом, должна вывести сообщение об ошибке.
     */
    @Test
    void negative_Time_Test() {
        InputOutput mockOutput = mock(InputOutput.class);
        CommandHandler commandHandler = new CommandHandler(mockOutput);
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
        InputOutput mockOutput = mock(InputOutput.class);
        CommandHandler commandHandler = new CommandHandler(mockOutput);
        TrainingSettings mockSettings = mock(TrainingSettings.class);
        when(mockSettings.getTrainingTime()).thenReturn(0);

        commandHandler.handleCommand("/start");

        verify(mockOutput).output("Установите время тренировки с помощью команды /settings.");
    }

    /**
     * Обработка команды "/stop" должна прервать активную тренировку, если она есть.
     */
    @Test
    void testStopTrainingWithActiveSession() {
        InputOutput mockOutput = Mockito.mock(InputOutput.class);
        CommandHandler commandHandler = new CommandHandler(mockOutput);
        TrainingSettings mockSettings = Mockito.mock(TrainingSettings.class);
        TrainingSession mockSession = Mockito.mock(TrainingSession.class);

        when(mockSettings.getTrainingTime()).thenReturn(1);
        commandHandler.trainingSettings = mockSettings;
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
        InputOutput mockOutput = Mockito.mock(InputOutput.class);
        CommandHandler commandHandler = new CommandHandler(mockOutput);
        TrainingSettings mockSettings = Mockito.mock(TrainingSettings.class);
        TrainingSession mockSession = Mockito.mock(TrainingSession.class);

        when(mockSettings.getTrainingTime()).thenReturn(1);
        commandHandler.trainingSettings = mockSettings;
        commandHandler.trainingSession = null;

        commandHandler.handleCommand("/stop");

        verify(mockSession, never()).stop();
        verify(mockOutput).output("Нет активной тренировки.");
    }

    /**
     * Обработка неизвестной команды должна вывести сообщение об ошибке и предложение ввести /help.
     */
    @Test
    void testHandleUnknownCommand() {
        InputOutput mockOutput = Mockito.mock(InputOutput.class);
        CommandHandler commandHandler = new CommandHandler(mockOutput);
        String unknownCommand = "unknown_command";

        commandHandler.handleCommand(unknownCommand);

        verify(mockOutput).output("Неизвестная команда. Введите /help для списка команд.");
    }
}
