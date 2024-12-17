package org.example.processing.commands;

import org.example.database.context.CurrentUserContext;
import org.example.interfaces.Command;
import org.example.interfaces.InputOutput;
import org.example.service.UserStatistics;
import org.example.utils.validation.ValidationUtils;
import org.hibernate.Session;

import java.util.Optional;

public class ResetCommand implements Command {

    private final ValidationUtils validationUtils = new ValidationUtils();
    private final UserStatistics userStatistics = new UserStatistics();
    private final InputOutput inputOutput;
    private final CurrentUserContext currentUserContext;

    /**
     * Конструктор ResetCommand, который получает ссылку на реализацию InputOutput
     * и объект CurrentUserContext
     * @param inputOutput реализация интерфейса InputOutput
     * @param currentUserContext контекст текущего пользователя
     */
    public ResetCommand(InputOutput inputOutput, CurrentUserContext currentUserContext) {
        this.inputOutput = inputOutput;
        this.currentUserContext = currentUserContext;
    }

    @Override
    public void execute(Optional<Session> optionalSession) throws IllegalArgumentException {
        if (requiresSession()
                && optionalSession.isEmpty()) {
            throw new IllegalArgumentException("Сессия обязательна для выполнения этой команды");
        }
        optionalSession.ifPresent(this::resetStatistics);
    }

    @Override
    public boolean requiresTransaction() {
        return true;
    }

    @Override
    public boolean requiresSession() {
        return true;
    }

    /**
     * Сбросить статистику пользователя с запросом на подтверждение сброса
     * @param session текущая сессия
     */
    private void resetStatistics(Session session) {
        String username = currentUserContext.getUsername();
        if (!validationUtils.isValidUsername(username)) {
            inputOutput.output("Для получения рейтинга необходимо авторизоваться");
            return;
        }
        inputOutput.output("Вы уверены, что хотите сбросить вашу статистику? (Y/N)");
        String answer = inputOutput.input();
        if (answer.equalsIgnoreCase("Y")) {
            userStatistics.resetUserStatistic(username, session);
            inputOutput.output("Статистика сброшена");
        } else if (answer.equalsIgnoreCase("N")) {
            inputOutput.output("Сброс статистики отменен");
        }
    }
}