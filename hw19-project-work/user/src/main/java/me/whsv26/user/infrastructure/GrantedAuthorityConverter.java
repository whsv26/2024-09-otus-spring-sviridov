package me.whsv26.user.infrastructure;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Converter(autoApply = true)
public class GrantedAuthorityConverter implements AttributeConverter<Set<GrantedAuthority>, String> {

    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(Set<GrantedAuthority> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return attribute.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(SPLIT_CHAR));
    }

    @Override
    public Set<GrantedAuthority> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return Collections.emptySet();
        }
        return Arrays.stream(dbData.split(SPLIT_CHAR))
            .map(String::trim)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());
    }
}
