package com.iotinall.framework.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.iotinall.framework.constants.constinterface.DateTimeFormatters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author xin-bing
 * @date 10/22/2019 18:05
 */
@Configuration
public class JacksonConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        JavaTimeModule javaModule = new JavaTimeModule();
        javaModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatters.STANDARD_DATETIME_FORMATTER));
        javaModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatters.STANDARD_DATE_FORMATTER));
        javaModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatters.STANDARD_TIME_FORMATTER));
        javaModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatters.STANDARD_DATETIME_FORMATTER));
        javaModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatters.STANDARD_DATE_FORMATTER));
        javaModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatters.STANDARD_TIME_FORMATTER));
        javaModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);
        objectMapper.registerModule(javaModule).registerModule(new ParameterNamesModule());

        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }
}
