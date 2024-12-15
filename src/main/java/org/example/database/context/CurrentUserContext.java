package org.example.database.context;

/**
 * Контекст текущего пользователя
 */
public class CurrentUserContext {

    private String username;

    /**
     * Получить имя текущего пользователя
     * @return имя текущего пользователя
     */
    public String getUsername() {
        return username;
    }

    /**
     * Установить имя текущего пользователя
     * @param username имя текущего пользователя
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
