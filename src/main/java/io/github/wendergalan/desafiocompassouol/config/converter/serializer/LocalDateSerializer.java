package io.github.wendergalan.desafiocompassouol.config.converter.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;

import static io.github.wendergalan.desafiocompassouol.util.Util.DATE_FORMAT;


/**
 * The type Local date time serializer.
 */
public class LocalDateSerializer extends JsonSerializer<LocalDateTime> {

    /**
     * @param value
     * @param generator
     * @param serializers
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Override
    public void serialize(LocalDateTime value, JsonGenerator generator, SerializerProvider serializers) throws IOException, JsonProcessingException {
        generator.writeString(value.format(DATE_FORMAT));
    }
}
