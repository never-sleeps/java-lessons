package ru.java.dataprocessor;

import ru.java.model.Measurement;

import java.util.List;

public interface Loader {

    List<Measurement> load();
}
