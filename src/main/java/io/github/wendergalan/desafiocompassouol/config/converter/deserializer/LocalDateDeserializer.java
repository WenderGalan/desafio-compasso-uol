package io.github.wendergalan.desafiocompassouol.config.converter.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;

import static io.github.wendergalan.desafiocompassouol.utility.Utility.DATE_FORMAT;

/**
 * The type Local date time deserializer.
 */
public class LocalDateDeserializer extends JsonDeserializer<LocalDateTime> {

    /**
     * @param parser
     * @param deserializationContext
     * @return
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return LocalDateTime.parse(parser.getValueAsString(), DATE_FORMAT);
    }
}