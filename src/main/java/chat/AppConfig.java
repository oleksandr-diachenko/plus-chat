package chat;

import chat.controller.ApplicationStyle;
import chat.model.repository.*;
import chat.util.AppProperty;
import chat.util.Paths;
import chat.util.StyleUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;

@Lazy
@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Value("${path.json.commands}")
    private String jsonCommandsPath;

    @Value("${path.json.directs}")
    private String jsonDirectsPath;

    @Value("${path.json.ranks}")
    private String jsonRanksPath;

    @Value("${path.json.smiles}")
    private String jsonSmilesPath;

    @Value("${path.json.users}")
    private String jsonUsersPath;

    @Value("${path.json.orders}")
    private String jsonOrdersPath;

    @Value("${path.settings.twitch}")
    private String settingsTwitchPath;

    @Value("${path.settings.settings}")
    private String settingsSettingsPath;

    @Value("${path.settings.commands}")
    private String settingsCommandsPath;

    @Value("${path.css.chat}")
    private String cssChatPath;

    @Value("${path.css.settings}")
    private String cssSettingsPath;

    @Value("${path.css.data}")
    private String cssDataPath;

    @Value("${path.css.confirm}")
    private String cssConfirmPath;

    @Value("${path.css.randomizer}")
    private String cssRandomizerPath;

    @Value("${path.img.chat.logo}")
    private String imgChatLogoPath;

    @Value("${path.img.chat.pinenabled}")
    private String imgChatPinEnabledPath;

    @Value("${path.img.chat.pindisabled}")
    private String imgChatPinDisabledPath;

    @Value("${path.directory.sound}")
    private String soundDirectoryPath;

    @Bean
    public CommandRepository commandRepository() {
        return new JSONCommandRepository(jsonCommandsPath);
    }

    @Bean
    public DirectRepository directRepository() {
        return new JSONDirectRepository(jsonDirectsPath);
    }

    @Bean
    public RankRepository rankRepository() {
        return new JSONRankRepository(jsonRanksPath);
    }

    @Bean
    public SmileRepository smileRepository() {
        return new JSONSmileRepository(jsonSmilesPath);
    }

    @Bean
    public UserRepository userRepository() {
        return new JSONUserRepository(jsonUsersPath);
    }

    @Bean
    public OrderRepository orderRepository() {
        return new JSONOrderRepository(jsonOrdersPath);
    }

    @Bean
    public AppProperty twitchProperties() {
        return new AppProperty(settingsTwitchPath);
    }

    @Bean
    public AppProperty settingsProperties() {
        return new AppProperty(settingsSettingsPath);
    }

    @Bean
    public AppProperty commandsProperties() {
        return new AppProperty(jsonCommandsPath);
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
        return new Paths(imgChatLogoPath, settingsProperties(), cssChatPath, cssConfirmPath, cssDataPath,
                cssSettingsPath, imgChatPinEnabledPath, imgChatPinDisabledPath,
                settingsTwitchPath, soundDirectoryPath, cssRandomizerPath);
    }
}
