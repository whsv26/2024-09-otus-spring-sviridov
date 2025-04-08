package me.whsv26.novel.api.infrastructure.converter;

import me.whsv26.novel.api.domain.valueobject.ValueObject;
import org.springframework.core.convert.converter.Converter;

public class FromValueObjectConverter implements Converter<ValueObject<String>, String> {

    @Override
    public String convert(ValueObject<String> source) {
        return source.value();
    }
}
