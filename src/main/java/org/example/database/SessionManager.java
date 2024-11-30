package org.example.database;

import org.example.commons.LogsFile;
import org.example.database.dao.UserDao;
import org.example.utils.log.LogsWriterUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Управление сессиями
 */
public class SessionManager {
    private final Logger logger = Logger.getLogger(UserDao.class.getName());
    private final LogsWriterUtils logsWriter = new LogsWriterUtils(LogsFile.FILE_NAME);
    private SessionFactory sessionFactory;

    /**
     * Вернуть текущую сессию
     * @return сессия
     */
    public Session getSession() {
        if (sessionFactory == null) {
            logger.log(Level.SEVERE, "SessionFactory не инициализирован");
            logsWriter.writeStackTraceToFile(new IllegalStateException());
        }
        return sessionFactory.openSession();
    }

    /**
     * Запустить сессию
     * @param configuration конфигурация базы данных
     */
    public void startSession(Configuration configuration) {
        sessionFactory = configuration.buildSessionFactory();
    }
}
