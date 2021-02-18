package io.github.wendergalan.desafiocompassouol.config.converter.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;

import static io.github.wendergalan.desafiocompassouol.utility.Utility.DATE_FORMAT;

/**
 * The type Local date time deserializer.
 */
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    /**
     * @param parser
     * @param deserializationContext
     * @return
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return LocalDate.parse(parser.getValueAsString(), DATE_FORMAT);
    }
}