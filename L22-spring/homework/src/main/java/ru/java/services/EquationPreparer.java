package ru.java.services;

import ru.java.model.Equation;

import java.util.List;

public interface EquationPreparer {
    List<Equation> prepareEquationsFor(int base);
}
