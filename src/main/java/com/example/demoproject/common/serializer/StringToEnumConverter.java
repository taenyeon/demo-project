package com.example.demoproject.common.serializer;

import com.example.demoproject.common.enums.Gender;
import org.springframework.core.convert.converter.Converter;

import java.util.Locale;

public class StringToEnumConverter implements Converter<String, Gender> {
    @Override
    public Gender convert(String str) {
        return Gender.valueOf(str.toUpperCase(Locale.ROOT));
    }
}
