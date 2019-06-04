package chat.controller;

import chat.util.AppProperty;
import chat.util.Settings;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author Alexander Diachenko.
 */
@Component
@Getter
@Setter
@NoArgsConstructor
public class ApplicationStyle {
    private String backgroundColor;
    private String baseColor;
    private String fontSize;
    private String nickColor;
    private String separatorColor;
    private String messageColor;
    private String directColor;
    private AppProperty settingProperties;

    public ApplicationStyle(AppProperty settingProperties) {
        this.settingProperties = settingProperties;
        reverse();
    }

    public void reverse() {
        Properties settings = settingProperties.loadProperty();
        backgroundColor = settings.getProperty(Settings.ROOT_BACKGROUND_COLOR);
        baseColor = settings.getProperty(Settings.ROOT_BASE_COLOR);
        fontSize = settings.getProperty(Settings.FONT_SIZE);
        nickColor = settings.getProperty(Settings.FONT_NICK_COLOR);
        separatorColor = settings.getProperty(Settings.FONT_SEPARATOR_COLOR);
        messageColor = settings.getProperty(Settings.FONT_MESSAGE_COLOR);
        directColor = settings.getProperty(Settings.FONT_DIRECT_MESSAGE_COLOR);
    }
}
