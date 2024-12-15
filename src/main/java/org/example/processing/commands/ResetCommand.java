package org.example.processing.commands;

import org.example.database.context.CurrentUserContext;
import org.example.database.dao.UserDao;
import org.example.database.entity.UserEntity;
import org.example.interfaces.Command;
import org.example.interfaces.InputOutput;
import org.hibernate.Session;

import java.util.Optional;

public class ResetCommand implements Command {

    private final UserDao userDao = new UserDao();
    private final InputOutput inputOutput;
    private final CurrentUserContext currentUserContext;

    public ResetCommand(InputOutput inputOutput, CurrentUserContext currentUserContext) {
        this.inputOutput = inputOutput;
        this.currentUserContext = currentUserContext;
    }

    @Override
    public void execute(Optional<Session> optionalSession) throws IllegalArgumentException {
        if (requiresSession() && optionalSession.isEmpty()) {
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

    private void resetStatistics(Session session) {
        String username = currentUserContext.getUsername();
        inputOutput.output("Вы уверены, что хотите сбросить вашу статистику? (Y/N)");
        String answer = inputOutput.input();
        if (answer.equalsIgnoreCase("Y")) {
            UserEntity user = userDao.getUserByUsername(username, session);
            if (user != null) {
                user.setTrainingCount(0);
                user.setAverageTime(0.0);
                user.setTime(0);
                user.setSumTypoCount(0);
                user.setSumTime(0);
                user.setRating(0.0);
                session.merge(user);
                inputOutput.output("Статистика сброшена");
            }
        } else {
            inputOutput.output("Сброс статистики отменен");
        }
    }
}