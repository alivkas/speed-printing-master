package org.example.processing.factory;

import org.example.commons.CommandsConst;
import org.example.database.context.CurrentUserContext;
import org.example.interfaces.Command;
import org.example.interfaces.InputOutput;
import org.example.processing.commands.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Фабрика команд
 */
public class CommandFactory {

    private final Map<String, Command> commands = new HashMap<>();

    /**
     * Конструктор CommandFactory, который сохраняет все команды в хранилище
     * @param inputOutput реализация интерфейса InputOutput
     * @param currentUserContext контекст текущего пользователя
     */
    public CommandFactory(InputOutput inputOutput, CurrentUserContext currentUserContext) {
        commands.put(CommandsConst.SETTINGS, new SettingsCommand(inputOutput, currentUserContext));
        commands.put(CommandsConst.INFO, new InfoCommand(inputOutput, currentUserContext));
        commands.put(CommandsConst.HELP, new HelpCommand(inputOutput));
        commands.put(CommandsConst.EXIT, new ExitCommand(inputOutput));
        commands.put(CommandsConst.LOGIN, new LoginCommand(inputOutput, currentUserContext));
        commands.put(CommandsConst.REGISTRATION, new RegistrationCommand(inputOutput));
        commands.put(CommandsConst.STOP, new StopCommand(inputOutput));
        commands.put(CommandsConst.START, new StartCommand(inputOutput, currentUserContext));
    }

    /**
     * Получить команду из хранилища при помощи принимаемой извне команды
     * @param command команда извне
     * @return выполнение команды
     */
    public Command getCommandByCommand(String command) {
        return commands.get(command);
    }
}
