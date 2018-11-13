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
        Properties settings = settingProperties.getProperty();
        this.backgroundColor = settings.getProperty(Settings.ROOT_BACKGROUND_COLOR);
        this.baseColor = settings.getProperty(Settings.ROOT_BASE_COLOR);
        this.fontSize = settings.getProperty(Settings.FONT_SIZE);
        this.nickColor = settings.getProperty(Settings.FONT_NICK_COLOR);
        this.separatorColor = settings.getProperty(Settings.FONT_SEPARATOR_COLOR);
        this.messageColor = settings.getProperty(Settings.FONT_MESSAGE_COLOR);
        this.directColor = settings.getProperty(Settings.FONT_DIRECT_MESSAGE_COLOR);
    }
}
