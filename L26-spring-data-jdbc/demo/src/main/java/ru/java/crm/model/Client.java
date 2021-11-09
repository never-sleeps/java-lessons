package ru.java.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;

/*
 * Поскольку для id применяется sequence, имплементация Persistable не требуется
 */
@Table("client")
public class Client {

    @Id
    private final Long id;
    @Nonnull
    private final String name;

    @Nonnull
    private final String managerId;

    @Nonnull
    private final int orderColumn;

    @Nonnull // конструктор для применения в коде
    public Client(String name, String managerId, int orderColumn) {
        this(null, name, managerId, orderColumn);
    }

    // конструктор для применения во фреймворке, может быть приватным, если в коде не используется, фреймворку это не помешает
    @PersistenceConstructor
    public Client(Long id, String name, String managerId, int orderColumn) {
        this.id = id;
        this.name = name;
        this.managerId = managerId;
        this.orderColumn = orderColumn;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getManagerId() {
        return managerId;
    }

    public int getOrderColumn() {
        return orderColumn;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", managerId='" + managerId + '\'' +
                ", orderColumn=" + orderColumn +
                '}';
    }
}
