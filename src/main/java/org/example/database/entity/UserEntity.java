package org.example.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.example.database.entity.base.BaseEntity;

/**
 * Сущность пользователя
 */
@Entity
@Table(name = "users", schema = "public")
public class UserEntity extends BaseEntity {

    /**
     * Имя пользователя
     */
    @Column(name = "username")
    private String username;

    /**
     * Пароль
     */
    @Column(name = "password")
    private String password;

    /**
     * Время введенное пользователем для тренировки
     */
    @Column(name = "time")
    private Double time;

    /**
     * Количество тренировок
     */
    @Column(name = "training_count")
    private Integer trainingCount;

    /**
     * Среднее время слов в минуту (считается как сумма среднего времени деленное на
     * количество тренировок)
     */
    @Column(name = "average_time")
    private Double averageTime;

    /**
     * Возвращает имя пользователя.
     *
     * @return Имя пользователя.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Устанавливает имя пользователя.
     *
     * @param username Новое имя пользователя.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Возвращает пароль пользователя.
     *
     * @return Пароль пользователя.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Устанавливает пароль пользователя.
     *
     * @param password Новый пароль пользователя.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Возвращает время, введенное пользователем для тренировки
     *
     * @return Время тренировки
     */
    public Double getTime() {
        return time;
    }

    /**
     * Устанавливает время тренировки пользователя
     *
     * @param time Новое время тренировки
     */
    public void setTime(Double time) {
        this.time = time;
    }

    /**
     * Возвращает количество тренировок, пройденных пользователем
     *
     * @return Количество тренировок
     */
    public Integer getTrainingCount() {
        return trainingCount;
    }

    /**
     * Устанавливает количество тренировок пользователя
     *
     * @param trainingCount Новое количество тренировок
     */
    public void setTrainingCount(Integer trainingCount) {
        this.trainingCount = trainingCount;
    }

    /**
     * Возвращает среднее время тренировки пользователя
     *
     * @return Среднее время тренировки
     */
    public Double getAverageTime() {
        return averageTime;
    }

    /**
     * Устанавливает среднее время тренировки пользователя
     *
     * @param averageTime Новое среднее время тренировки
     */
    public void setAverageTime(Double averageTime) {
        this.averageTime = averageTime;
    }

}