package org.example.processing.commands;

import org.example.database.dao.UserDao;
import org.example.interfaces.Command;
import org.example.interfaces.InputOutput;
import org.example.service.UserAuth;
import org.example.utils.validation.ValidationUtils;
import org.hibernate.Session;

import java.util.Optional;

/**
 * Определение команды /registration
 */
public class RegistrationCommand implements Command {

    private final ValidationUtils validationUtils = new ValidationUtils();
    private final UserDao userDao = new UserDao();
    private final UserAuth userAuth;
    private final InputOutput inputOutput;

    /**
     * Конструктор RegistrationCommand, который получает ссылку на реализацию InputOutput
     * и инициализирует userAuth
     * @param inputOutput реализация интерфейса InputOutput
     */
    public RegistrationCommand(InputOutput inputOutput) {
        this.inputOutput = inputOutput;
        this.userAuth = new UserAuth(userDao);
    }

    @Override
    public void execute(Optional<Session> optionalSession) throws IllegalArgumentException {
        if (requiresSession()
                && optionalSession.isEmpty()) {
            throw new IllegalArgumentException("Сессия обязательна для выполнения этой команды");
        }
        optionalSession.ifPresent(this::registration);
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
     * Регистрирует нового пользователя в системе
     * @param session текущая сессия
     */
    private void registration(Session session) {
        inputOutput.output("Введите имя пользователя: ");
        String username = inputOutput.input();
        inputOutput.output("Введите пароль: ");
        String password = inputOutput.input();

        if (!validationUtils.isValidUsername(username)) {
            inputOutput.output("Имя пользователя не должно быть пустым");
            return;
        }

        boolean isSuccess = userAuth.registerUser(username, password, session);
        inputOutput.output(isSuccess
                ? "Регистрация прошла успешно! Войдите в аккаунт."
                : "Пользователь с таким именем уже существует.");
    }
}
