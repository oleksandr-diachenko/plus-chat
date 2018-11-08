package chat.util;

import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class PathsImpl {

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
    private String commandJson;
    private String directsJson;
    private String rankJson;
    private String smileJson;
    private String userJson;

    public PathsImpl() {
        //do nothing
    }

    public PathsImpl(final String logo, final AppProperty settingsProperties,
                     final String chatCss, final String confirmCss, final String dataCss,
                     final String settingsCss, final String enabledPin, final String disabledPin,
                     final String twitchProperties, final String soundDirectory, final String commandJson,
                     final String directsJson, final String rankJson, final String smileJson,
                     final String userJson) {
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
        this.commandJson = commandJson;
        this.directsJson = directsJson;
        this.rankJson = rankJson;
        this.smileJson = smileJson;
        this.userJson = userJson;
    }

    public String getLogo() {
        return this.logo;
    }

    public String getChatCSS() {
        final Properties settings = this.settingsProperties.getProperty();
        return "/theme/" + settings.getProperty(Settings.ROOT_THEME) + this.chatCss;
    }

    public String getConfirmCSS() {
        final Properties settings = this.settingsProperties.getProperty();
        return "/theme/" + settings.getProperty(Settings.ROOT_THEME) + this.confirmCss;
    }

    public String getDataCSS() {
        final Properties settings = this.settingsProperties.getProperty();
        return "/theme/" + settings.getProperty(Settings.ROOT_THEME) + this.dataCss;
    }

    public String getSettingsCSS() {
        final Properties settings = this.settingsProperties.getProperty();
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

    public String getCommandJson() {
        return this.commandJson;
    }

    public String getDirectsJson() {
        return this.directsJson;
    }

    public String getRankJson() {
        return this.rankJson;
    }

    public String getSmileJson() {
        return this.smileJson;
    }

    public String getUserJso() {
        return this.userJson;
    }
}
