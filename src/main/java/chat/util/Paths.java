package chat.util;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@NoArgsConstructor
public class Paths {

    private String logo;
    private AppProperty settingsProperties;
    private String chatCss;
    private String confirmCss;
    private String dataCss;
    private String settingsCss;
    private String enabledPin;
    private String disabledPin;
    private String twitchProperties;
    private String soundDirectory;

    public Paths(String logo, AppProperty settingsProperties,
                 String chatCss, String confirmCss, String dataCss,
                 String settingsCss, String enabledPin, String disabledPin,
                 String twitchProperties, String soundDirectory) {
        this.logo = logo;
        this.settingsProperties = settingsProperties;
        this.chatCss = chatCss;
        this.confirmCss = confirmCss;
        this.dataCss = dataCss;
        this.settingsCss = settingsCss;
        this.enabledPin = enabledPin;
        this.disabledPin = disabledPin;
        this.twitchProperties = twitchProperties;
        this.soundDirectory = soundDirectory;
    }

    public String getLogo() {
        return this.logo;
    }

    public String getChatCSS() {
        Properties settings = this.settingsProperties.getProperty();
        return "/theme/" + settings.getProperty(Settings.ROOT_THEME) + this.chatCss;
    }

    public String getConfirmCSS() {
        Properties settings = this.settingsProperties.getProperty();
        return "/theme/" + settings.getProperty(Settings.ROOT_THEME) + this.confirmCss;
    }

    public String getDataCSS() {
        Properties settings = this.settingsProperties.getProperty();
        return "/theme/" + settings.getProperty(Settings.ROOT_THEME) + this.dataCss;
    }

    public String getSettingsCSS() {
        Properties settings = this.settingsProperties.getProperty();
        return "/theme/" + settings.getProperty(Settings.ROOT_THEME) + this.settingsCss;
    }

    public String getEnabledPin() {
        return this.enabledPin;
    }

    public String getDisabledPin() {
        return this.disabledPin;
    }

    public String getTwitchProperties() {
        return this.twitchProperties;
    }

    public String getSoundsDirectory() {
        return this.soundDirectory;
    }
}
