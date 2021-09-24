package ru.java.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ru.java.exception.FileProcessException;
import ru.java.model.Measurement;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileLoader implements Loader {

    private final String fileName;

    public FileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        try{
            var filePath = ClassLoader.getSystemResource(fileName);
            if(filePath == null || !Files.exists(Paths.get(filePath.getPath()))){
                throw new FileNotFoundException(String.format("Файл '%s' (%s) не найден", fileName, filePath));
            }

            ObjectMapper objectMapper = new ObjectMapper();
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addDeserializer(Measurement.class, new MeasurementFileDeserializer(Measurement.class));
            objectMapper.registerModule(simpleModule);

            return objectMapper.readValue(filePath, new TypeReference<>() {});

        } catch (IOException ex){
            throw new FileProcessException(ex);
        }
    }
}
