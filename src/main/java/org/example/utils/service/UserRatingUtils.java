package org.example.utils.service;

import org.example.commons.Time;
import org.example.database.entity.UserEntity;

import java.util.Comparator;
import java.util.List;

/**
 * Работа с рейтингом пользователей
 */
public class UserRatingUtils {

    private final static double AVERAGE_TIME_WEIGHT = 2.5;

    /**
     * Подсчет рейтинга
     * @param sumTrainingCount сумма количества тренировок
     * @param sumTypoCount сумма количества ошибок за тренировки
     * @param sumAverageTime сумма среднего времени тренировки
     * @return число рейтинга
     */
    public double ratingCalculation(int sumTrainingCount, int sumTypoCount, double sumAverageTime) {
        double allTrainingsAverageTime = sumAverageTime / sumTrainingCount;
        return allTrainingsAverageTime * AVERAGE_TIME_WEIGHT / (sumTypoCount + 1);
    }

    /**
     * Получить топ пользователей
     * @param users список пользователей
     * @param limit лимит на вывод топа пользователей
     * @return список отсортированных по рейтингу пользователей с определенным лимитом
     */
    public List<UserEntity> getTopUsers(List<UserEntity> users, int limit) {
        return users.stream()
                .sorted(Comparator.comparingDouble(UserEntity::getRating)
                        .reversed())
                .limit(limit)
                .toList();
    }

    /**
     * Найти ранг пользователя в системе
     * @param users список отсортированных по рейтингу пользователей
     * @param username имя текущего пользователя
     * @return ранг пользователя, если имя текущего пользователя совпадает
     * с именем найденного пользователя, -1, если пользователь не найден,
     * 0, если рейтинг пользователя равен 0
     */
    public int findUserRank(List<UserEntity> users, String username) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)
                    && users.get(i).getRating() == 0.0)
                return 0;
            if (users.get(i).getUsername().equals(username)) {
                return i + 1;
            }
        }
        return -1;
    }

    /**
     * Форматирование информации о пользователе в строку
     * @param user пользователь
     * @param username имя текущего пользователя
     * @param rank ранг пользователя
     * @return отформатированную в строку информацию о пользователе
     */
    public String formatUserInfo(UserEntity user, String username, int rank) {
        double allTrainingsAverageTime = user.getAverageTime() / (user.getTrainingCount() + 1);
        String rankInfo = rank > 0
                ? rank + " место"
                : "Место отсутствует";
        return String.format("""
                    Пользователь: %s %s
                    %s —> рейтинг %.2f
                    Количество тренировок: %d
                    Количество ошибок: %d
                    Средняя скорость: %.0f слов/мин
                    Последнее время тренировки: %d минут
                    """,
                user.getUsername(),
                username.equals(user.getUsername())
                        ? "(вы)"
                        : "",
                rankInfo,
                user.getRating(),
                user.getTrainingCount(),
                user.getSumTypoCount(),
                allTrainingsAverageTime,
                user.getTime() / Time.MINUTES_IN_MILLISECONDS);
    }

    /**
     * Построить рейтинг пользователей
     * @param topUsers список топа пользователей по рейтингу
     * @param currentRank текущий ранг
     * @param currentUser текущий пользователь
     * @param username имя пользователя
     * @return рейтинг всех пользователей
     */
    public String buildUsersRating(List<UserEntity> topUsers,
                                   int currentRank,
                                   UserEntity currentUser,
                                   String username) {
        StringBuilder info = new StringBuilder();
        String currentUserInfo = formatUserInfo(currentUser, username, currentRank);
        info.append(currentUserInfo);
        info.append("<------------------------------>\n");

        for (int i = 0; i < topUsers.size(); i++) {
            UserEntity user = topUsers.get(i);
            int rank = findUserRank(topUsers, user.getUsername());
            if(rank  !=  0) {
                String userInfo = formatUserInfo(user, username, i + 1);
                info.append(userInfo);
                info.append("<------------------------------>\n");
            }
        }

        return info.toString();
    }
}