package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import util.AppProperty;

import java.util.Properties;

/**
 * @author Alexander Diachenko.
 */
public class SettingController {
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
    }

    public void confirmAction() {
    }

    public void cancelAction() {
    }
}
