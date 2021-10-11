package ru.java.table_ineritance.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "B")
@DiscriminatorValue("BLeaf")
public class B extends A {
    private String b;

    public B() { }

    public B(long id, String a, String b) {
        super(id, a);
        this.b = b;
    }
}
