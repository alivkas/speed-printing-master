package org.example.commons;

/**
 * Класс хранения констант для команд
 */
public class CommandsConst {
    /**
     * Команда /help (вывод всех доступных команд)
     */
    public final static String HELP = "/help";
    /**
     * Команда /settings (установка настройки для тренировки)
     */
    public final static String SETTINGS = "/settings";
    /**
     * Команда /stop (остановка тренировки)
     */
    public final static String STOP = "/stop";
    /**
     * Команда /start (начало тренировки)
     */
    public final static String START = "/start";
    /**
     * Команда /exit (выход из программы)
     */
    public final static String EXIT = "/exit";
    /**
     * Команда /registration (регистрация пользователя в системе)
     */
    public final static String REGISTRATION = "/registration";
    /**
     * Команда /login (авторизация пользователя в системе)
     */
    public final static String LOGIN = "/login";
    /**
     * Команда /info (вывод информации о пользователе)
     */
    public final static String INFO = "/info";


    private CommandsConst() {
    }
}
