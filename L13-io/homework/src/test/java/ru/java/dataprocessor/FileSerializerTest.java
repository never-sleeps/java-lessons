package ru.java.dataprocessor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Paths;
import java.util.Map;

class FileSerializerTest {

    private final String fileName = "outputData.json";
    private FileSerializer fileSerializer = new FileSerializer(fileName);

    @Test
    @DisplayName("данные должны успешно сериализоваться")
    void serializeShouldSuccessfullySerializeData() throws IOException {
        // given
        String key = "key";
        double value = 10;
        Map<String, Double> map = Map.of(key, value);

        // when
        fileSerializer.serialize(map);

        // then
        File file = Paths.get(fileName).toFile();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(file);
        Assertions.assertEquals(value, node.get(key).doubleValue());
    }
}