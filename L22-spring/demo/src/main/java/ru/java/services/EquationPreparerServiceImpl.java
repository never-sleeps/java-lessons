package ru.java.services;

import ru.java.model.DivisionEquation;
import ru.java.model.Equation;
import ru.java.model.MultiplicationEquation;
import ru.java.services.EquationPreparerService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EquationPreparerServiceImpl implements EquationPreparerService {
    @Override
    public List<Equation> prepareEquationsFor(int base) {
        List<Equation> equations = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            var multiplicationEquation = new MultiplicationEquation(base, i);
            var divisionEquation = new DivisionEquation(multiplicationEquation.getResult(), base);
            equations.add(multiplicationEquation);
            equations.add(divisionEquation);
        }
        Collections.shuffle(equations);
        return equations;
    }
}
