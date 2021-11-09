package ru.java.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

/*
    Поскольку id в таблице несколько нетипичный, рукотворный (id varchar(50)),
    используем имплементацию Persistable<String> (String - поскольку id в данном случае - строка)
 */
@Table("manager")
public class Manager implements Persistable<String> {

    @Id
    @Nonnull
    private final String id;
    private final String label;

    /* Одновременно и clients, и clientsOrdered тут чисто ради эксперимента. В рабочих проектах так обычно не делают :) */

    @MappedCollection(idColumn = "manager_id") // Отношение один-ко-многим
    private final Set<Client> clients; // Set, когда записи между собой никак не упорядочены

    @MappedCollection(idColumn = "manager_id", keyColumn= "order_column") // keyColumn - столбец, по которому будут упорядочиваться записи
    private final List<Client> clientsOrdered; // List, когда записи упорядочены.

    // поле (отсутствует в базе) - флаг того, новые ли это объект или существующий
    @Transient // благодаря этой аннотации фреймворк будет знать, что данное поле персистить в базу не нужно
    private final boolean isNew;

    public Manager(String id, String label, Set<Client> clients, List<Client> clientsOrdered, boolean isNew) {
        this.id = id;
        this.label = label;
        this.clients = clients;
        this.clientsOrdered = clientsOrdered;
        this.isNew = isNew;
    }

    @PersistenceConstructor
    private Manager(String id, String label, Set<Client> clients, List<Client> clientsOrdered) {
        this(id, label, clients, clientsOrdered, false);
    }

    /*
    При вызове save() фреймворк будет вызывать этот метод. При true фреймворк будет выполнять insert, при false - update
     */
    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public List<Client> getClientsOrdered() {
        return clientsOrdered;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", clients=" + clients +
                ", clientsOrdered=" + clientsOrdered +
                ", isNew=" + isNew +
                '}';
    }
}
