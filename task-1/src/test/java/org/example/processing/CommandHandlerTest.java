package org.example.processing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тестовый класс для проверки функциональности класса CommandHandler
 */
public class CommandHandlerTest {
    private CommandHandler commandHandler;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();


    /**
     * Инициализируем экземпляр CommandHandler и перенаправляем стандартный вывод.
     */
    @BeforeEach
    public void setUp() {
        commandHandler = new CommandHandler();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    /**
     * Тестируем команду "/help".
     * Проверяем, что выводится правильный список доступных команд.
     */
    @Test
    public void testHandleCommandHelp() {
        commandHandler.handleCommand("/help");
        String expectedOutput = "/help - Все команды\n" +
                "/settings - Настройки тренировки\n" +
                "/start - Начать тренировку\n" +
                "/stop - Прервать тренировку\n" +
                "/exit - Завершить приложение\n";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));
    }

    /**
     * Тестируем команду "/settings".
     * Проверяем, что выводится запрос на ввод времени тренировки.
     */
    @Test
    public void testHandleCommandSettings() {
        String simulatedInput = "30";
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        commandHandler.handleCommand("/settings");
        assertTrue(outputStreamCaptor.toString().contains("Укажите время на тренировку (минуты)"));
        assertTrue(outputStreamCaptor.toString().contains("Время тренировки 30 минут"));
    }

    /**
     * Тестируем команду "/start".
     * Проверяем, что выводится сообщение о необходимости установить время,
     * если время тренировки не было установлено.
     */
    @Test
    public void testHandleCommandStart() {
        commandHandler.handleCommand("/start");
        assertTrue(outputStreamCaptor.toString().contains("Установите время тренировки с помощью команды /settings."));
    }

    /**
     * Тестируем команду "/stop".
     * Проверяем, что выводится сообщение о завершении тренировки,
     * если активная тренировка не была начата.
     */
    @Test
    public void testHandleCommandStop() {
        commandHandler.handleCommand("/stop");
        assertTrue(outputStreamCaptor.toString().contains("Нет активной тренировки."));
    }


    /**
     * Тестируем обработку неизвестной команды.
     * Проверяем, что выводится сообщение о неизвестной команде.
     */
    @Test
    public void testHandleCommandUnknown() {
        commandHandler.handleCommand("/unknown");
        assertTrue(outputStreamCaptor.toString().contains("Неизвестная команда. Введите /help для списка команд."));
    }


}
