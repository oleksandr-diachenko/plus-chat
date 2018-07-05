package chat.controller;

import chat.Main;
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
    }

    private void initBaseColorPicker() {
        baseColorPicker.setValue(Color.valueOf(settings.getProperty("root.base.color")));
        baseColorPicker.valueProperty().addListener((ov, old_val, new_val) -> setRootStyle(getHexColor(new ColorPicker(new_val)), getHexColor(backgroundColorPicker)));
    }

    private void initLanguage() {
        languages = new HashMap<>();
        languages.put("en", "English");
        languages.put("ru", "Russian");
        languages.put("ua", "Ukrainian");
        languageChoiceBox.setItems(FXCollections.observableArrayList(languages.values()));
        languageChoiceBox.setValue(languages.get(settings.getProperty("root.language")));
    }

    private void initTheme() {
        try {
            File[] themes = new File(getClass().getResource("/theme").toURI()).listFiles();
            List<String> list = new ArrayList<>();
            for (File theme : themes) {
                list.add(theme.getName());
            }
            themeChoiceBox.setItems(FXCollections.observableArrayList(list));
            themeChoiceBox.setValue(settings.getProperty("root.theme"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void initFontSizeSlider() {
        String fontSizeValue = settings.getProperty("font.size");
        fontSize.setText(fontSizeValue);
        fontSizeSlider.setValue(Double.parseDouble(fontSizeValue));
        fontSizeSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            fontSize.setText(String.valueOf(Math.round(new_val.doubleValue())));
            setLabelStyle(String.valueOf(new_val), getHexColor(nickColorPicker), getHexColor(separatorColorPicker), getHexColor(messageColorPicker));
        });
    }

    private void initTransparencySlider() {
        String backgroundTransparencyValue = settings.getProperty("background.transparency");
        transparencyValue.setText(backgroundTransparencyValue);
        transparencySlider.setValue(Long.valueOf(backgroundTransparencyValue) * 100);
        transparencySlider.valueProperty().addListener((ov, old_val, new_val) -> {
            String format = String.format("%.2f", new_val.doubleValue() / 100);
            transparencyValue.setText(format);
        });
    }

    private void initBackGroundColorPicker() {
        backgroundColorPicker.setValue(Color.valueOf(settings.getProperty("root.background.color")));
        backgroundColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            setRootStyle(getHexColor(baseColorPicker), getHexColor(new ColorPicker(new_val)));
        });
    }

    private void initNickColorPicker() {
        nickColorPicker.setValue(Color.valueOf(settings.getProperty("nick.font.color")));
        nickColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            setLabelStyle(String.valueOf(new_val), getHexColor(new ColorPicker(new_val)), getHexColor(separatorColorPicker), getHexColor(messageColorPicker));
        });
    }

    private void initSeparatorColorPicker() {
        separatorColorPicker.setValue(Color.valueOf(settings.getProperty("separator.font.color")));
        separatorColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            setLabelStyle(String.valueOf(new_val), getHexColor(nickColorPicker), getHexColor(new ColorPicker(new_val)), getHexColor(messageColorPicker));
        });
    }

    private void initMessageColorPicker() {
        messageColorPicker.setValue(Color.valueOf(settings.getProperty("message.font.color")));
        messageColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            setLabelStyle(String.valueOf(new_val), getHexColor(nickColorPicker), getHexColor(separatorColorPicker), getHexColor(new ColorPicker(new_val)));
        });
    }

    public void confirmAction() {
        Stage stage = new Stage();
        stage.setResizable(false);
        String language = settings.getProperty("root.language");
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
        undecorator.getStylesheets().add("/theme/" + settings.getProperty("root.theme") + "/dialog.css");
        root.setStyle(getRootStyle());
        Set<Node> labels = root.lookupAll(".label");
        for (Node label : labels) {
            label.setStyle(getLabelStyle(fontSize.getText(), getHexColor(nickColorPicker)));
        }
        stage.setScene(undecorator);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(getStage().getScene().getWindow());
        stage.show();

        stage.setOnCloseRequest(event -> {
            DialogController dialogController = fxmlLoader.getController();
            if (dialogController.isConfirmed()) {
                settings.setProperty("background.transparency", transparencyValue.getText());
                settings.setProperty("font.size", fontSize.getText());
                settings.setProperty("root.language", getLanguage(languageChoiceBox.getValue()));
                settings.setProperty("root.theme", themeChoiceBox.getValue());

                settings.setProperty("root.base.color", getHexColor(baseColorPicker));
                settings.setProperty("root.background.color", getHexColor(backgroundColorPicker));
                settings.setProperty("nick.font.color", getHexColor(nickColorPicker));
                settings.setProperty("separator.font.color", getHexColor(separatorColorPicker));
                settings.setProperty("message.font.color", getHexColor(messageColorPicker));
                AppProperty.setProperties(settings);
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
        setLabelStyle(
                settings.getProperty("font.size"),
                settings.getProperty("nick.font.color"),
                settings.getProperty("separator.font.color"),
                settings.getProperty("message.font.color")
        );
        setRootStyle(settings.getProperty("root.base.color"), settings.getProperty("root.background.color"));
        getStage().close();
    }

    private void setRootStyle(String baseColor, String backgroundColor) {
        chatRoot.setStyle("-fx-base: " + baseColor + "; -fx-background: " + backgroundColor + ";");
        root.setStyle("-fx-base: " + baseColor + "; -fx-background: " + backgroundColor + ";");
    }

    private void setLabelStyle(String fontSize, String nickColor, String separatorColor, String messageColor) {
        Set<Node> names = chatRoot.lookupAll("#user-name");
        Set<Node> separators = chatRoot.lookupAll("#separator");
        Set<Node> messages = chatRoot.lookupAll("#user-message");
        names.iterator().forEachRemaining(node -> node.setStyle(getLabelStyle(fontSize, nickColor)));
        separators.iterator().forEachRemaining(node -> node.setStyle(getLabelStyle(fontSize, separatorColor)));
        messages.iterator().forEachRemaining(node -> node.setStyle(getLabelStyle(fontSize, messageColor)));

        Set<Node> labels = this.chatRoot.lookupAll(".label");
        labels.iterator().forEachRemaining(node -> node.setStyle(getLabelStyle(nickColor)));
    }

    private String getRootStyle() {
        return "-fx-base: " + settings.getProperty("root.base.color") + ";" +
                "-fx-background: " + settings.getProperty("root.background.color") + ";";
    }

    private String getLabelStyle(String nickColor) {
        return "-fx-fill: " + nickColor + ";";
    }

    private String getLabelStyle(String fontSize, String nickColor) {
        return "-fx-font-size: " + fontSize + "px;" +
                "-fx-fill: " + nickColor + ";";
    }

    private String getHexColor(ColorPicker color) {
        return "#" + Integer.toHexString(color.getValue().hashCode()).substring(0, 6).toUpperCase();
    }

    private String getLanguage(String value) {
        for (String key : languages.keySet()) {
            if (languages.get(key).equals(value)) {
                return key;
            }
        }
        return null;
    }

    private Stage getStage() {
        return (Stage) root.getScene().getWindow();
    }

    private Node getChatRoot() {
        Stage owner = Main.stage;
        return owner.getScene().lookup("#root");
    }
}
