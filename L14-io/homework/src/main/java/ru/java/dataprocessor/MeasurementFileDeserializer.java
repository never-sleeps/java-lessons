package ru.java.dataprocessor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ru.java.exception.FileProcessException;
import ru.java.model.Measurement;

import java.io.IOException;

public class MeasurementFileDeserializer extends StdDeserializer<Measurement> {

    protected MeasurementFileDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Measurement deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JsonProcessingException {
        try {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            String name = node.get("name").asText();
            double value = node.get("value").doubleValue();
            return new Measurement(name,value);
        } catch (IOException ex) {
            throw new FileProcessException(ex);
        }
    }
}
