package chat;


import chat.util.AppProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestAppConfig {

    @Value("${path.settings.simpleproperty}")
    private String simpleProperty;

    @Bean
    public AppProperty simpleProperty() {
        return new AppProperty(simpleProperty);
    }
}
