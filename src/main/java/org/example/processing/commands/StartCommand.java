package org.example.processing.commands;

import org.example.animation.Animation;
import org.example.database.context.CurrentUserContext;
import org.example.database.dao.UserDao;
import org.example.interfaces.Command;
import org.example.interfaces.InputOutput;
import org.example.service.UserTraining;
import org.example.training.TrainingProcess;
import org.example.utils.validation.ValidationUtils;
import org.hibernate.Session;

import java.util.Optional;

/**
 * Определение команды /start
 */
public class StartCommand implements Command {

    private final ValidationUtils validationUtils = new ValidationUtils();
    private final UserDao userDao = new UserDao();
    private final UserTraining userTraining;
    private final InputOutput inputOutput;
    private final CurrentUserContext currentUserContext;

    /**
     * Конструктор StartCommand, который получает ссылку на реализацию InputOutput,
     * объект CurrentUserContext и инициализирует userTraining
     * @param inputOutput реализация интерфейса InputOutput
     * @param currentUserContext контекст текущего пользователя
     */
    public StartCommand(InputOutput inputOutput, CurrentUserContext currentUserContext) {
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
        optionalSession.ifPresent(this::startTraining);
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
     * Запускает тренировку на установленное время.
     * @param session текущая сессия
     */
    private void startTraining(Session session) {
        String username = currentUserContext.getUsername();
        if (!validationUtils.isValidUsername(username)) {
            inputOutput.output("Для начала тренировки авторизуйтесь в системе");
            return;
        }
        if (userTraining.getUserTrainingTime(username, session) == 0) {
            inputOutput.output("Установите время тренировки с помощью команды /settings.");
            return;
        }

        Animation animation = new Animation(inputOutput);
        animation.countingDown();

        TrainingProcess trainingProcess = new TrainingProcess(inputOutput);
        trainingProcess.process(session, username);
    }
}
