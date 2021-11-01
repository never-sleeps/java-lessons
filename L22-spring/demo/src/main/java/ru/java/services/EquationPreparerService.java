package ru.java.services;

import ru.java.model.Equation;

import java.util.List;

public interface EquationPreparerService {
    List<Equation> prepareEquationsFor(int base);
}
