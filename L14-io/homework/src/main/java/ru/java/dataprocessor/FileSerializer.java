package ru.java.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.java.exception.FileProcessException;

import java.io.File;
import java.util.Map;

public class FileSerializer implements Serializer {

    String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Сериализация данных: формирует результирующий json из  [data] и сохраняет его в файл
     * @param data - данные для сериализации
     */
    @Override
    public void serialize(Map<String, Double> data) {
        try {
            var file = new File(fileName);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(file, data);

        } catch (Exception ex) {
            throw new FileProcessException(ex);
        }
    }
}
