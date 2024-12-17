package org.example.processing.commands;

import org.example.database.context.CurrentUserContext;
import org.example.database.dao.UserDao;
import org.example.interfaces.Command;
import org.example.interfaces.InputOutput;
import org.example.service.UserAuth;
import org.example.utils.validation.ValidationUtils;
import org.hibernate.Session;

import java.util.Optional;

/**
 * Определение команды /login
 */
public class LoginCommand implements Command {

    private final ValidationUtils validationUtils = new ValidationUtils();
    private final UserDao userDao = new UserDao();
    private final CurrentUserContext currentUserContext;
    private final UserAuth userAuth;
    private final InputOutput inputOutput;

    /**
     * Конструктор LoginCommand, который получает ссылку на реализацию InputOutput,
     * объект CurrentUserContext и инициализирует userAuth
     * @param inputOutput реализация интерфейса InputOutput
     * @param currentUserContext контекст текущего пользователя
     */
    public LoginCommand(InputOutput inputOutput, CurrentUserContext currentUserContext) {
        this.inputOutput = inputOutput;
        this.currentUserContext = currentUserContext;
        this.userAuth = new UserAuth(userDao);
    }

    @Override
    public void execute(Optional<Session> optionalSession) throws IllegalArgumentException {
        if (requiresSession()
                && optionalSession.isEmpty()) {
            throw new IllegalArgumentException("Сессия обязательна для выполнения этой команды");
        }
        optionalSession.ifPresent(this::login);
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
     * Выполняет вход пользователя в систему
     * @param session текущая сессия
     */
    private void login(Session session) {
        inputOutput.output("Введите имя пользователя: ");
        String username = inputOutput.input();
        inputOutput.output("Введите пароль: ");
        String password = inputOutput.input();

        if (!validationUtils.isValidUsername(username)) {
            inputOutput.output("Имя пользователя не должно быть пустым");
            return;
        }

        boolean isSuccess = userAuth.loginUser(username, password, session);
        if (isSuccess) {
            inputOutput.output("Вход выполнен!");
            currentUserContext.setUsername(username);
        } else {
            inputOutput.output("Неверный логин или пароль.");
        }
    }
}
