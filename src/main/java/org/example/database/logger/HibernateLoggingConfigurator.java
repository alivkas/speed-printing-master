package org.example.database.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Конфигурация логгирования для Hibernate
 */
public class HibernateLoggingConfigurator {

    /**
     * Отключить логи
     */
    public void disableHibernateLogging() {
        Logger hibernateLogger = Logger.getLogger("org.hibernate");

        hibernateLogger.setLevel(Level.OFF);

        Logger hibernateSQLLogger = Logger.getLogger("org.hibernate.SQL");
        hibernateSQLLogger.setLevel(Level.OFF);

        Logger hibernateTypeLogger = Logger.getLogger("org.hibernate.type");
        hibernateTypeLogger.setLevel(Level.OFF);
    }
}
