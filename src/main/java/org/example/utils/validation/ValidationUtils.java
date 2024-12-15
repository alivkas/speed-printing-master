package org.example.utils.validation;

/**
 * Валидация данных
 */
public class ValidationUtils {

    /**
     * Проверить валидность ввода имени пользователя на пустоту
     * @param username имя пользователя
     * @return true если имя не пустое, false если имя пустое
     */
    public boolean isValidUsername(String username) {
        return username != null && !username.isEmpty();
    }
}
