package chat.controller;

import chat.util.AppProperty;
import chat.util.Settings;
import javafx.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author Alexander Diachenko.
 */
@Component
public class ApplicationStyle {
    private String backgroundColor;
    private String baseColor;
    private String fontSize;
    private String nickColor;
    private String separatorColor;
    private String messageColor;
    private String directColor;
    private AppProperty settingProperties;

    public ApplicationStyle() {
        //do nothing
    }

    public ApplicationStyle(final AppProperty settingProperties) {
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
    public String getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(final String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBaseColor() {
        return this.baseColor;
    }

    public void setBaseColor(final String baseColor) {
        this.baseColor = baseColor;
    }

    public String getFontSize() {
        return this.fontSize;
    }

    public void setFontSize(final String fontSize) {
        this.fontSize = fontSize;
    }

    public String getNickColor() {
        return this.nickColor;
    }

    public void setNickColor(final String nickColor) {
        this.nickColor = nickColor;
    }

    public String getSeparatorColor() {
        return this.separatorColor;
    }

    public void setSeparatorColor(final String separatorColor) {
        this.separatorColor = separatorColor;
    }

    public String getMessageColor() {
        return this.messageColor;
    }

    public void setMessageColor(final String messageColor) {
        this.messageColor = messageColor;
    }

    public String getDirectColor() {
        return this.directColor;
    }

    public void setDirectColor(final String directColor) {
        this.directColor = directColor;
    }
}
