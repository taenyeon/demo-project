package com.example.demoproject.common.serializer;

import org.springframework.core.convert.converter.Converter;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class StringToDateConverter  implements Converter<String, LocalDate> {
    @Override
    public LocalDate convert(String str) {
        return LocalDate.parse(str,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
