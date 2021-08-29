package ru.java.behavioral.visitor;

public interface Element {
    void accept(Visitor v);
}
