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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import chat.model.entity.Font;
import chat.model.repository.FontRepository;
import chat.model.repository.JSONFontRepository;
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
    private VBox root;
    @FXML
    private Label transparencyValue;
    @FXML
    private Label fontSize;
    @FXML
    private ChoiceBox<String> languageChoiceBox;
    @FXML
    private ChoiceBox<String> themeChoiceBox;
    @FXML
    private ChoiceBox<Font> fontChoiceBox;
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

    private FontRepository fontRepository = new JSONFontRepository();

    public void initialize() {
        settings = AppProperty.getProperty("./settings/settings.properties");
        initLanguage();
        initFontFamily();
        initTheme();
        initFontSizeSlider();
        initTransparencySlider();
        initBackGroundColorPicker();
        initNickColorPicker();
        initSeparatorColorPicker();
        initMessageColorPicker();
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

    private void initFontFamily() {
        Set<Font> fonts = fontRepository.getFonts();
        fontChoiceBox.setItems(FXCollections.observableArrayList(fonts));
        Font current = new Font();
        current.setName(settings.getProperty("root.font.family"));
        fontChoiceBox.setValue(current);
    }

    private void initFontSizeSlider() {
        Stage owner = Main.stage;
        String fontSizeValue = settings.getProperty("font.size");
        fontSize.setText(fontSizeValue);
        fontSizeSlider.setValue(Double.parseDouble(fontSizeValue));
        fontSizeSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            Node chatRoot = owner.getScene().lookup("#root");
            Set<Node> names = chatRoot.lookupAll("#user-name");
            Set<Node> separators = chatRoot.lookupAll("#separator");
            Set<Node> messages = chatRoot.lookupAll("#user-message");
            names.iterator().forEachRemaining(node -> {
                node.setStyle("-fx-font-size: " + new_val + "px;");
            });
            separators.iterator().forEachRemaining(node -> {
                node.setStyle("-fx-font-size: " + new_val + "px;");
            });
            messages.iterator().forEachRemaining(node -> {
                node.setStyle("-fx-font-size: " + new_val + "px;");
            });
            fontSize.setText(String.valueOf(Math.round(new_val.doubleValue())));
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
            label.setStyle(getLabelStyle());
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
                settings.setProperty("root.font.family", fontChoiceBox.getValue().getName());

                settings.setProperty("root.background.color", getHexColor(backgroundColorPicker));
                settings.setProperty("nick.font.color", getHexColor(nickColorPicker));
                settings.setProperty("separator.font.color", getHexColor(separatorColorPicker));
                settings.setProperty("message.font.color", getHexColor(messageColorPicker));
                AppProperty.setProperties(settings);
                reload();
            }
        });
    }

    private String getLabelStyle() {
        return "-fx-text-fill: " + settings.getProperty("nick.font.color") + ";" +
                "-fx-font-family: \"" + settings.getProperty("root.font.family") + "\";";
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

    private Stage getStage() {
        return (Stage) root.getScene().getWindow();
    }

    private String getLanguage(String value) {
        for (String key : languages.keySet()) {
            if (languages.get(key).equals(value)) {
                return key;
            }
        }
        return null;
    }

    public void cancelAction() {
        getStage().close();
    }

    private String getHexColor(ColorPicker color) {
        return "#" + Integer.toHexString(color.getValue().hashCode()).substring(0, 6).toUpperCase();
    }

    private String getRootStyle() {
        return "-fx-base: " + settings.getProperty("root.base.color") + ";" +
                "-fx-background: " + settings.getProperty("root.background.color") + ";" +
                "-fx-font-family: \"" + settings.getProperty("root.font.family") + "\";";
    }
}
