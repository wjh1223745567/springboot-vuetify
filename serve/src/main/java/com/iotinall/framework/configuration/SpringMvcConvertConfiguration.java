package com.iotinall.framework.configuration;

import com.iotinall.framework.constants.constinterface.DateTimeFormatters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * config parameter converters
 * time: 4/20/2019
 * @author xin-bing
 */
@Configuration
public class SpringMvcConvertConfiguration {
    /**
     * description: request param convert to LocalDateTime
     * time: 4/20/2019 09:01
     * @author xin-bing
     * @version 1.0
     * @return org.springframework.core.convert.converter.Converter<java.lang.String,java.time.LocalDateTime>
     */
    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                if(source.isEmpty()) {
                    return null;
                }
                // just judging source length for choice witch formatter
                if(source.length() == 10) {
                    LocalDate localDate = LocalDate.parse(source, DateTimeFormatters.STANDARD_DATE_FORMATTER);
                    return LocalDateTime.from(localDate.atStartOfDay());
                } if(source.length() == 19) {
                    return LocalDateTime.parse(source, DateTimeFormatters.STANDARD_DATETIME_FORMATTER);
                } else {
                    throw new IllegalArgumentException("unsupported datetime formatter");
                }
            }
        };
    }

    /**
     * description: request param convert to LocalDate
     * time: 4/20/2019 09:11
     * @author xin-bing
     * @version 1.0
     * @return org.springframework.core.convert.converter.Converter<java.lang.String,java.time.LocalDate>
     */
    @Bean
    public Converter<String, LocalDate> localDateConverter() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                if(source.isEmpty()) {
                    return null;
                }
                // just judging source length for choice witch formatter
                return LocalDate.parse(source, DateTimeFormatters.STANDARD_DATE_FORMATTER);
            }
        };
    }

    /**
     * description: request param convert to LocalTime
     * time: 4/20/2019 09:12
     * @author xin-bing
     * @version 1.0
     * @return org.springframework.core.convert.converter.Converter<java.lang.String,java.time.LocalTime>
     */
    @Bean
    public Converter<String, LocalTime> localTimeConverter() {
        return new Converter<String, LocalTime>() {
            @Override
            public LocalTime convert(String source) {
                if(source.isEmpty()) {
                    return null;
                }
                if(source.length() == 19) {
                    return LocalTime.parse(source, DateTimeFormatters.STANDARD_DATETIME_FORMATTER);
                }else if(source.length() == 5){
                    return LocalTime.parse(source,  DateTimeFormatter.ofPattern("HH:mm"));
                }
                return LocalTime.parse(source, DateTimeFormatters.STANDARD_TIME_FORMATTER);
            }
        };
    }
}