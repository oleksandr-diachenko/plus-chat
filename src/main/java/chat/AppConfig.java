package chat;

import chat.controller.ApplicationStyle;
import chat.model.repository.*;
import chat.util.AppProperty;
import chat.util.Paths;
import chat.util.StyleUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public CommandRepository commandRepository() {
        return new JSONCommandRepository("./data/commands.json");
    }

    @Bean
    public DirectRepository directRepository() {
        return new JSONDirectRepository("./data/directs.json");
    }

    @Bean
    public RankRepository rankRepository() {
        return new JSONRankRepository("./data/ranks.json");
    }

    @Bean
    public SmileRepository smileRepository() {
        return new JSONSmileRepository("./data/smiles.json");
    }

    @Bean
    public UserRepository userRepository() {
        return new JSONUserRepository("./data/users.json");
    }

    @Bean
    public OrderRepository orderRepository() {
        return new JSONOrderRepository("./data/orders.json");
    }

    @Bean
    public AppProperty twitchProperties() {
        return new AppProperty("./settings/twitch.properties");
    }

    @Bean
    public AppProperty settingsProperties() {
        return new AppProperty("./settings/settings.properties");
    }

    @Bean
    public AppProperty commandsProperties() {
        return new AppProperty("./settings/commands.properties");
    }

    @Bean
    public ApplicationStyle applicationStyle() {
        return new ApplicationStyle(settingsProperties());
    }

    @Bean
    public StyleUtil styleUtil() {
        return new StyleUtil(applicationStyle());
    }

    @Bean
    public Paths paths() {
        return new Paths("/img/chat/logo.png", settingsProperties(), "/chat.css", "/confirm.css", "/data.css",
                "/settings.css", "/img/chat/pin-enabled.png", "/img/chat/pin-disabled.png",
                "/settings/twitch.properties", "./sound/", "/randomizer.css");
    }
}
