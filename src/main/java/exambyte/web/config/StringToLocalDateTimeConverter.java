package exambyte.web.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    public LocalDateTime convert(String source) {
        if (source.isBlank()) {
            return null;
        }
        return LocalDateTime.parse(source, formatter);
    }
}
