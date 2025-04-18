package exambyte.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final StringToLocalDateTimeConverter stringToLocalDateTimeConverter;

    public WebConfig(StringToLocalDateTimeConverter converter) {
        this.stringToLocalDateTimeConverter = converter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToLocalDateTimeConverter);
    }
}
