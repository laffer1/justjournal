package com.justjournal.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PrefBoolConverter implements AttributeConverter<PrefBool, String> {
    @Override
    public String convertToDatabaseColumn(PrefBool attribute) {
        if (attribute == null)
            return null;
        return attribute.name();
    }

    @Override
    public PrefBool convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null;
        return PrefBool.valueOf(dbData);
    }
}