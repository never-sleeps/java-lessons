package ru.java.crm.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import ru.java.crm.model.Manager;

import java.util.List;


public interface ManagerRepository extends CrudRepository<Manager, String> {

    /*
     Дефолтная реализация в CrudRepository: Iterable<T> findAll();
     Если работа с Iterable неудобна, можно переопределить метод и возвращать сразу List, даже если @Query явно не задаётся
     */
    @Override
    // решение проблемы N+1
    @Query(value = """ 
            select m.id    as manager_id,
                   m.label as manager_label,
                   c.id    as client_id,
                   c.name  as client_name,
                   c.order_column as order_column
            from manager m
                     left outer join client c
                                     on m.id = c.manager_id
            order by m.id
                                                          """,
            resultSetExtractorClass = ManagerResultSetExtractorClass.class)
    List<Manager> findAll();
}
