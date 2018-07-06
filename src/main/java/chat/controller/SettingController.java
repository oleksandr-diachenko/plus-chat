package chat.controller;

import chat.Main;
import chat.component.StyleUtil;
import chat.util.ColorUtil;
import chat.util.ResourceBundleControl;
import insidefx.undecorator.UndecoratorScene;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import chat.util.AppProperty;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author Alexander Diachenko.
 */
public class SettingController {

    @FXML
    private ColorPicker baseColorPicker;
    @FXML
    private Node root;
    @FXML
    private Label transparencyValue;
    @FXML
    private Label fontSize;
    @FXML
    private ChoiceBox<String> languageChoiceBox;
    @FXML
    private ChoiceBox<String> themeChoiceBox;
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
    private Map<String, String> languages;
    private Node chatRoot;

    public void initialize() {
        this.settings = AppProperty.getProperty("./settings/settings.properties");
        this.chatRoot = getChatRoot();
        initLanguage();
        initTheme();
        initFontSizeSlider();
        initTransparencySlider();
        initBaseColorPicker();
        initBackGroundColorPicker();
        initNickColorPicker();
        initSeparatorColorPicker();
        initMessageColorPicker();
        getStage().setOnCloseRequest(event -> {
            cancelAction();
        });
    }

    private void initBaseColorPicker() {
        this.baseColorPicker.setValue(Color.valueOf(this.settings.getProperty("root.base.color")));
        this.baseColorPicker.valueProperty().addListener((ov, old_val, new_val) -> StyleUtil.setRootStyle(this.chatRoot, this.root, ColorUtil.getHexColor(new_val), ColorUtil.getHexColor(this.backgroundColorPicker.getValue())));
    }

    private void initLanguage() {
        this.languages = new HashMap<>();
        this.languages.put("en", "English");
        this.languages.put("ru", "Russian");
        this.languages.put("ua", "Ukrainian");
        this.languageChoiceBox.setItems(FXCollections.observableArrayList(this.languages.values()));
        this.languageChoiceBox.setValue(this.languages.get(this.settings.getProperty("root.language")));
    }

    private void initTheme() {
        try {
            File[] themes = new File(getClass().getResource("/theme").toURI()).listFiles();
            List<String> list = new ArrayList<>();
            for (File theme : themes) {
                list.add(theme.getName());
            }
            this.themeChoiceBox.setItems(FXCollections.observableArrayList(list));
            this.themeChoiceBox.setValue(this.settings.getProperty("root.theme"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void initFontSizeSlider() {
        String fontSizeValue = this.settings.getProperty("font.size");
        this.fontSize.setText(fontSizeValue);
        this.fontSizeSlider.setValue(Double.parseDouble(fontSizeValue));
        this.fontSizeSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            this.fontSize.setText(String.valueOf(Math.round(new_val.doubleValue())));
            StyleUtil.setLabelStyle(this.chatRoot, this.root, String.valueOf(new_val), ColorUtil.getHexColor(this.nickColorPicker.getValue()), ColorUtil.getHexColor(this.separatorColorPicker.getValue()), ColorUtil.getHexColor(this.messageColorPicker.getValue()));
        });
    }

    private void initTransparencySlider() {
        String backgroundTransparencyValue = this.settings.getProperty("background.transparency");
        this.transparencyValue.setText(backgroundTransparencyValue);
        this.transparencySlider.setValue(Long.valueOf(backgroundTransparencyValue) * 100);
        this.transparencySlider.valueProperty().addListener((ov, old_val, new_val) -> {
            String format = String.format("%.2f", new_val.doubleValue() / 100);
            this.transparencyValue.setText(format);
        });
    }

    private void initBackGroundColorPicker() {
        this.backgroundColorPicker.setValue(Color.valueOf(this.settings.getProperty("root.background.color")));
        this.backgroundColorPicker.valueProperty().addListener((ov, old_val, new_val) -> StyleUtil.setRootStyle(this.chatRoot, this.root, ColorUtil.getHexColor(this.baseColorPicker.getValue()), ColorUtil.getHexColor(new_val)));
    }

    private void initNickColorPicker() {
        this.nickColorPicker.setValue(Color.valueOf(this.settings.getProperty("nick.font.color")));
        this.nickColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            StyleUtil.setLabelStyle(this.chatRoot, this.root, this.fontSize.getText(), ColorUtil.getHexColor(new_val), ColorUtil.getHexColor(this.separatorColorPicker.getValue()), ColorUtil.getHexColor(this.messageColorPicker.getValue()));
        });
    }

    private void initSeparatorColorPicker() {
        this.separatorColorPicker.setValue(Color.valueOf(this.settings.getProperty("separator.font.color")));
        this.separatorColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            StyleUtil.setLabelStyle(this.chatRoot, this.root, this.fontSize.getText(), ColorUtil.getHexColor(this.nickColorPicker.getValue()), ColorUtil.getHexColor(new_val), ColorUtil.getHexColor(this.messageColorPicker.getValue()));
        });
    }

    private void initMessageColorPicker() {
        this.messageColorPicker.setValue(Color.valueOf(this.settings.getProperty("message.font.color")));
        this.messageColorPicker.valueProperty().addListener((ov, old_val, new_val) -> StyleUtil.setLabelStyle(this.chatRoot, this.root, this.fontSize.getText(), ColorUtil.getHexColor(this.nickColorPicker.getValue()), ColorUtil.getHexColor(this.separatorColorPicker.getValue()), ColorUtil.getHexColor(new_val)));
    }

    public void confirmAction() {
        Stage stage = new Stage();
        stage.setResizable(false);
        String language = this.settings.getProperty("root.language");
        ResourceBundle bundle = ResourceBundle.getBundle("bundles.chat", new Locale(language), new ResourceBundleControl());
        Region root = null;
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.setResources(bundle);
            root = fxmlLoader.load(getClass().getResource("/view/dialog.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        UndecoratorScene undecorator = new UndecoratorScene(stage, root);
        undecorator.getStylesheets().add("/theme/" + this.settings.getProperty("root.theme") + "/dialog.css");
        root.setStyle(StyleUtil.getRootStyle(this.settings));
        Set<Node> labels = root.lookupAll(".label");
        for (Node label : labels) {
            label.setStyle(StyleUtil.getTextStyle(this.fontSize.getText(), ColorUtil.getHexColor(this.nickColorPicker.getValue())));
        }
        stage.setScene(undecorator);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(getStage().getScene().getWindow());
        stage.show();

        stage.setOnCloseRequest(event -> {
            DialogController dialogController = fxmlLoader.getController();
            if (dialogController.isConfirmed()) {
                this.settings.setProperty("background.transparency", this.transparencyValue.getText());
                this.settings.setProperty("font.size", this.fontSize.getText());
                this.settings.setProperty("root.language", getLanguage(this.languageChoiceBox.getValue()));
                this.settings.setProperty("root.theme", this.themeChoiceBox.getValue());

                this.settings.setProperty("root.base.color", ColorUtil.getHexColor(this.baseColorPicker.getValue()));
                this.settings.setProperty("root.background.color", ColorUtil.getHexColor(this.backgroundColorPicker.getValue()));
                this.settings.setProperty("nick.font.color", ColorUtil.getHexColor(this.nickColorPicker.getValue()));
                this.settings.setProperty("separator.font.color", ColorUtil.getHexColor(this.separatorColorPicker.getValue()));
                this.settings.setProperty("message.font.color", ColorUtil.getHexColor(this.messageColorPicker.getValue()));
                AppProperty.setProperties(this.settings);
                reload();
            }
        });
    }

    private void reload() {
        Stage primaryStage = (Stage) getStage().getOwner();
        primaryStage.close();
        Platform.runLater(() -> {
            try {
                new Main().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void cancelAction() {
        StyleUtil.setLabelStyle(this.chatRoot, this.root,
                this.settings.getProperty("font.size"),
                this.settings.getProperty("nick.font.color"),
                this.settings.getProperty("separator.font.color"),
                this.settings.getProperty("message.font.color")
        );
        StyleUtil.setRootStyle(this.chatRoot, this.root, this.settings.getProperty("root.base.color"), this.settings.getProperty("root.background.color"));
        getStage().close();
    }

    private String getLanguage(String value) {
        for (String key : this.languages.keySet()) {
            if (this.languages.get(key).equals(value)) {
                return key;
            }
        }
        return null;
    }

    private Stage getStage() {
        return ChatController.settingStage;
    }

    private Node getChatRoot() {
        Stage owner = Main.stage;
        return owner.getScene().lookup("#root");
    }
}
