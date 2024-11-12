package org.example.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.example.database.entity.base.BaseEntity;

/**
 * Сущность пользователя
 */
@Entity(name = "user")
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
     * Среднее время (считается как разность суммы времени на
     * количество тренировок)
     */
    @Column(name = "average_time")
    private Double averageTime;
}
