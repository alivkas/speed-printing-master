package org.example.processing.commands;

import org.example.database.context.CurrentUserContext;
import org.example.database.dao.UserDao;
import org.example.interfaces.Command;
import org.example.interfaces.InputOutput;
import org.example.service.UserStatistics;
import org.example.utils.validation.ValidationUtils;
import org.hibernate.Session;

import java.util.Optional;

/**
 * Определение команды /info
 */
public class InfoCommand implements Command {

    private final ValidationUtils validationUtils = new ValidationUtils();
    private final UserStatistics userStatistics = new UserStatistics();
    private final CurrentUserContext currentUserContext;
    private final InputOutput inputOutput;

    /**
     * Конструктор InfoCommand, который получает ссылку на реализацию InputOutput
     * @param inputOutput реализация интерфейса InputOutput
     * @param currentUserContext контекст текущего пользователя
     */
    public InfoCommand(InputOutput inputOutput, CurrentUserContext currentUserContext) {
        this.inputOutput = inputOutput;
        this.currentUserContext = currentUserContext;
    }

    @Override
    public void execute(Optional<Session> optionalSession) throws IllegalArgumentException {
        if (requiresSession()
                && optionalSession.isEmpty()) {
            throw new IllegalArgumentException("Сессия обязательна для выполнения этой команды");
        }
        optionalSession.ifPresent(this::getInfo);
    }

    @Override
    public boolean requiresTransaction() {
        return false;
    }

    @Override
    public boolean requiresSession() {
        return true;
    }

    /**
     * Получить информацию о пользователе
     * @param session текущая сессия
     */
    private void getInfo(Session session) {
        String username = currentUserContext.getUsername();
        if (!validationUtils.isValidUsername(username)) {
            inputOutput.output("Для получения информации о пользователе" +
                    " необходимо авторизоваться");
            return;
        }
        String info = userStatistics.getUserInfo(username, session);
        inputOutput.output(info);
    }
}
