package com.example.demoproject.common.serializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class DateTimeJacksonModule extends SimpleModule {

    public DateTimeJacksonModule() {
        super();

        addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                    throws IOException {
                return LocalDate.parse(jsonParser.getValueAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        });

        addDeserializer(LocalTime.class, new JsonDeserializer<LocalTime>() {
            @Override
            public LocalTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                    throws IOException {
                return LocalTime.parse(jsonParser.getValueAsString(), DateTimeFormatter.ofPattern("HH:mm:ss"));
            }
        });

        addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                    throws IOException {
                return LocalDateTime
                        .parse(jsonParser.getValueAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
        });

        addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
            @Override
            public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializers)
                    throws IOException {
                jsonGenerator.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(localDate));
            }
        });

        addSerializer(LocalTime.class, new JsonSerializer<LocalTime>() {
            @Override
            public void serialize(LocalTime localTime, JsonGenerator jsonGenerator,
                                  SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(DateTimeFormatter.ofPattern("HH:mm:ss").format(localTime));
            }
        });

        addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator,
                                  SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime));
            }
        });
    }
}
