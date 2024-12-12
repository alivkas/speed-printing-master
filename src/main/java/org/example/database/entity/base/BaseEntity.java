package org.example.database.entity.base;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * Базовая сущность для других сущностей
 */
@MappedSuperclass
public class BaseEntity {

    /**
     * Идентификатор (primary key)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Получает идентификатор сущности.
     *
     * @return идентификатор сущности.
     */
    public Long getId() {
        return id;
    }

}