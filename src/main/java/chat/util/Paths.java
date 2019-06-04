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
    private String randomizerCss;

    public Paths(String logo, AppProperty settingsProperties,
                 String chatCss, String confirmCss, String dataCss,
                 String settingsCss, String enabledPin, String disabledPin,
                 String twitchProperties, String soundDirectory, String randomizerCss) {
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
        this.randomizerCss = randomizerCss;
    }

    public String getLogo() {
        return logo;
    }

    public String getChatCSS() {
        Properties settings = settingsProperties.loadProperty();
        return "/theme/" + settings.getProperty(Settings.ROOT_THEME) + chatCss;
    }

    public String getConfirmCSS() {
        Properties settings = settingsProperties.loadProperty();
        return "/theme/" + settings.getProperty(Settings.ROOT_THEME) + confirmCss;
    }

    public String getDataCSS() {
        Properties settings = settingsProperties.loadProperty();
        return "/theme/" + settings.getProperty(Settings.ROOT_THEME) + dataCss;
    }

    public String getSettingsCSS() {
        Properties settings = settingsProperties.loadProperty();
        return "/theme/" + settings.getProperty(Settings.ROOT_THEME) + settingsCss;
    }

    public String getRandomizerCSS() {
        Properties settings = settingsProperties.loadProperty();
        return "/theme/" + settings.getProperty(Settings.ROOT_THEME) + randomizerCss;
    }

    public String getEnabledPin() {
        return enabledPin;
    }

    public String getDisabledPin() {
        return disabledPin;
    }

    public String getTwitchProperties() {
        return twitchProperties;
    }

    public String getSoundsDirectory() {
        return soundDirectory;
    }
}
