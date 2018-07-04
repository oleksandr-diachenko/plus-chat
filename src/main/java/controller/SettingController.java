package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import util.AppProperty;

import java.util.Properties;

/**
 * @author Alexander Diachenko.
 */
public class SettingController {
    @FXML
    private Label transparencyValue;
    @FXML
    private Label fontSize;
    @FXML
    private ChoiceBox languageChoiceBox;
    @FXML
    private ChoiceBox themeChoiceBox;
    @FXML
    private ChoiceBox fontChoiceBox;
    @FXML
    private Slider fontSizeSlider;
    @FXML
    private Slider transparencySlider;
    @FXML
    private ColorPicker backgroundColorPicker;
    @FXML
    private ColorPicker nickColorPicker;
    @FXML
    private ColorPicker separatorColorPicker;
    @FXML
    private ColorPicker messageColorPicker;
    private Properties settings;

    public void initialize() {
        settings = AppProperty.getProperty("./settings/settings.properties");
        initFontSizeSlider();
        initTransparencySlider();
        initBackGroundColorPicker();
        initNickColorPicker();
        initSeparatorColorPicker();
        initMessageColorPicker();
    }

    private void initFontSizeSlider() {
        String fontSizeValue = settings.getProperty("font.size");
        fontSize.setText(fontSizeValue);
        fontSizeSlider.setValue(Double.parseDouble(fontSizeValue));
        fontSizeSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            fontSize.setText(String.valueOf(Math.round(new_val.doubleValue())));
        });
    }

    private void initTransparencySlider() {
        String backgroundTransparencyValue = settings.getProperty("background.transparency");
        transparencyValue.setText(backgroundTransparencyValue);
        transparencySlider.setValue(Double.parseDouble(backgroundTransparencyValue));
        transparencySlider.valueProperty().addListener((ov, old_val, new_val) -> {
            String format = String.format("%.2f", new_val.doubleValue() / 100);
            transparencyValue.setText(format);
        });
    }

    private void initBackGroundColorPicker() {
        backgroundColorPicker.setValue(Color.valueOf(settings.getProperty("root.background.color")));
    }

    private void initNickColorPicker() {
        nickColorPicker.setValue(Color.valueOf(settings.getProperty("nick.font.color")));
    }

    private void initSeparatorColorPicker() {
        separatorColorPicker.setValue(Color.valueOf(settings.getProperty("separator.font.color")));
    }

    private void initMessageColorPicker() {
        messageColorPicker.setValue(Color.valueOf(settings.getProperty("message.font.color")));
    }

    public void confirmAction() {
    }

    public void cancelAction() {
    }
}