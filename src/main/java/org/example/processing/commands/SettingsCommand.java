package org.example.processing.commands;

import org.apache.log4j.Logger;
import org.example.commons.Time;
import org.example.database.context.CurrentUserContext;
import org.example.database.dao.UserDao;
import org.example.interfaces.Command;
import org.example.interfaces.InputOutput;
import org.example.service.UserTraining;
import org.example.utils.validation.ValidationUtils;
import org.hibernate.Session;

import java.util.Optional;

/**
 * Определение команды /settings
 */
public class SettingsCommand implements Command {

    private final UserDao userDao = new UserDao();
    private final ValidationUtils validationUtils = new ValidationUtils();
    private final Logger logger = Logger.getLogger(SettingsCommand.class);
    private final InputOutput inputOutput;
    private final UserTraining userTraining;
    private final CurrentUserContext currentUserContext;

    /**
     * Конструктор SettingsCommand, который получает ссылку на реализацию InputOutput
     * и инициализирует userTraining
     * @param inputOutput реализация интерфейса InputOutput
     * @param currentUserContext контекст текущего пользователя
     */
    public SettingsCommand(InputOutput inputOutput, CurrentUserContext currentUserContext) {
        this.inputOutput = inputOutput;
        this.currentUserContext = currentUserContext;
        this.userTraining = new UserTraining(userDao);
    }

    @Override
    public void execute(Optional<Session> optionalSession) throws IllegalArgumentException {
        if (requiresSession()
                && optionalSession.isEmpty()) {
            throw new IllegalArgumentException("Сессия обязательна для выполнения этой команды");
        }
        optionalSession.ifPresent(this::askTrainingTime);
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
     * Запрашивает у пользователя время тренировки в минутах и устанавливает его.
     * time - время в минутах
     * @param session текущая сессия
     */
    private void askTrainingTime(Session session) {
        String username = currentUserContext.getUsername();
        if (!validationUtils.isValidUsername(username)) {
            inputOutput.output("Для установки настроек авторизуйтесь в системе");
            return;
        }

        inputOutput.output("Укажите время на тренировку (минуты)");
        try {
            int time = Integer.parseInt(inputOutput.input());
            int millisecondsTime = time * Time.MINUTES_IN_MILLISECONDS;
            if (time <= 0) {
                inputOutput.output("Время тренировки должно быть положительным числом.");
                return;
            }
            userTraining.saveUsersTrainingTime(millisecondsTime, username, session);
            inputOutput.output("Время тренировки " + time + " минут");
        } catch (NumberFormatException e) {
            logger.error("Некорректный ввод. Введите целое положительное число.", e);
            inputOutput.output("Некорректный ввод. Введите целое положительное число.");
        }
    }
}
