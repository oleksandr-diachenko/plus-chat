package chat.controller;

import chat.util.Settings;
import javafx.scene.Node;

import java.util.Properties;

/**
 * @author Alexander Diachenko.
 */
public class ApplicationStyle {

    private Node chatRoot;
    private String fontSize;
    private String nickColor;
    private String separatorColor;
    private String messageColor;
    private String directColor;

    public ApplicationStyle(final Node chatRoot, final Properties settings) {
        this.chatRoot = chatRoot;
        this.fontSize = settings.getProperty(Settings.FONT_SIZE);
        this.nickColor = settings.getProperty(Settings.FONT_NICK_COLOR);
        this.separatorColor = settings.getProperty(Settings.FONT_SEPARATOR_COLOR);
        this.messageColor = settings.getProperty(Settings.FONT_MESSAGE_COLOR);
        this.directColor = settings.getProperty(Settings.FONT_DIRECT_MESSAGE_COLOR);
    }

    public Node getChatRoot() {
        return this.chatRoot;
    }

    public void setChatRoot(final Node chatRoot) {
        this.chatRoot = chatRoot;
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
