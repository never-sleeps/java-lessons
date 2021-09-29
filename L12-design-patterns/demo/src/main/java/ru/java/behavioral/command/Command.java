package ru.java.behavioral.command;

@FunctionalInterface
public interface Command {
    String execute(SomeObject object);
}
