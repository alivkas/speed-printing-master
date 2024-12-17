package org.example.utils.service;

import org.example.database.entity.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Тестирование класса UserRatingUtils
 */
class UserRatingUtilsTest {

    private final static int TOP_LIMIT = 3;
    private final UserRatingUtils userRating = new UserRatingUtils();
    private final UserEntity user1 = new UserEntity();
    private final UserEntity user2 = new UserEntity();
    private final UserEntity user3 = new UserEntity();

    /**
     * Инициализация данных трех пользователей перед каждым тестом
     */
    @BeforeEach
    public void setUp() {
        user1.setUsername("test1");
        user2.setUsername("test2");
        user3.setUsername("test3");
        user1.setTrainingCount(2);
        user2.setTrainingCount(5);
        user3.setTrainingCount(1);
        user1.setSumTypoCount(3);
        user2.setSumTypoCount(2);
        user3.setSumTypoCount(10);
        user1.setAverageTime(3.0);
        user2.setAverageTime(6.0);
        user3.setAverageTime(9.0);
        user1.setTime(180000);
        user2.setTime(120000);
        user3.setTime(60000);
        user1.setRating(2.0);
        user2.setRating(3.2);
        user3.setRating(0.1);
    }

    /**
     * Тестировать получение топ 3 пользователей
     * по рейтингу
     */
    @Test
    public void testGetTopUsers() {
        List<UserEntity> users = List.of(user1, user2, user3);

        List<UserEntity> topUsers = userRating.getTopUsers(users, TOP_LIMIT);
        List<UserEntity> expected = List.of(user2, user1, user3);

        Assertions.assertEquals(expected, topUsers);
    }

    /**
     * Тестировать нахождение ранга существующего пользователя
     */
    @Test
    public void testFindExistingUserRank() {
        List<UserEntity> users = List.of(user1, user2, user3);
        List<UserEntity> topUsers = userRating.getTopUsers(users, TOP_LIMIT);

        int rank = userRating.findUserRank(topUsers, "test2");
        Assertions.assertEquals(1, rank);
    }

    /**
     * Тестировать нахождение ранга несуществующего пользователя
     */
    @Test
    public void testFindNonExistingUserRank() {
        List<UserEntity> users = List.of(user1, user2, user3);
        List<UserEntity> topUsers = userRating.getTopUsers(users, TOP_LIMIT);

        int rank = userRating.findUserRank(topUsers, "test5");
        Assertions.assertEquals(-1, rank);
    }

    /**
     * Тестировать форматирование текущего пользователя
     */
    @Test
    public void testFormatCurrentUserInfo() {
        String formattedCurrentUser = userRating.formatUserInfo(user1, "test1", 2);
        String expected = """
                Пользователь: test1 (вы)
                2 место —> рейтинг 2,00
                Количество тренировок: 2
                Количество ошибок: 3
                Средняя скорость: 1 слов/мин
                Последнее время тренировки: 3 минут
                """;
        Assertions.assertEquals(expected, formattedCurrentUser);
    }

    /**
     * Тестировать форматирование стороннего пользователя
     */
    @Test
    public void testFormatUserInfo() {
        String formattedCurrentUser = userRating.formatUserInfo(user1, "test2", 2);
        String expected = """
                Пользователь: test1\s
                2 место —> рейтинг 2,00
                Количество тренировок: 2
                Количество ошибок: 3
                Средняя скорость: 1 слов/мин
                Последнее время тренировки: 3 минут
                """;
        Assertions.assertEquals(expected, formattedCurrentUser);
    }

    /**
     * Тестировать форматирование пользователя без ранга
     */
    @Test
    public void testFormatUserInfoWithoutRank() {
        String formattedCurrentUser = userRating.formatUserInfo(user1, "test1", 0);
        String expected = """
                Пользователь: test1 (вы)
                Место отсутствует —> рейтинг 2,00
                Количество тренировок: 2
                Количество ошибок: 3
                Средняя скорость: 1 слов/мин
                Последнее время тренировки: 3 минут
                """;
        Assertions.assertEquals(expected, formattedCurrentUser);
    }
}