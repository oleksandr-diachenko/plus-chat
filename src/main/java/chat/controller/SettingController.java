package chat.controller;

import chat.Main;
import chat.component.ConfirmDialog;
import chat.component.DataDialog;
import chat.model.entity.*;
import chat.model.repository.*;
import chat.util.AppProperty;
import chat.util.ColorUtil;
import chat.util.Settings;
import chat.util.StyleUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Alexander Diachenko.
 */
public class SettingController {

    @FXML
    private ColorPicker directMessageColorPicker;
    @FXML
    private ColorPicker baseColorPicker;
    @FXML
    private Node settingsRoot;
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
    private Node ownerRoot;

    @FXML
    public void initialize() {
        this.settings = AppProperty.getProperty("./settings/settings.properties");
        this.ownerRoot = getOwnerRoot();
        initLanguage();
        initTheme();
        initFontSizeSlider();
        initTransparencySlider();
        initBaseColorPicker();
        initBackGroundColorPicker();
        initNickColorPicker();
        initSeparatorColorPicker();
        initMessageColorPicker();
        initDirectMessageColorPicker();
    }

    private void initBaseColorPicker() {
        this.baseColorPicker.setValue(Color.valueOf(this.settings.getProperty(Settings.ROOT_BASE_COLOR)));
        this.baseColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            StyleUtil.setRootStyle(Arrays.asList(this.ownerRoot, this.settingsRoot),
                    ColorUtil.getHexColor(new_val),
                    ColorUtil.getHexColor(this.backgroundColorPicker.getValue())
            );
        });
    }

    private void initLanguage() {
        this.languages = new HashMap<>();
        this.languages.put("en", "English");
        this.languages.put("ru", "Russian");
        this.languages.put("ua", "Ukrainian");
        this.languageChoiceBox.setItems(FXCollections.observableArrayList(this.languages.values()));
        this.languageChoiceBox.setValue(this.languages.get(this.settings.getProperty(Settings.ROOT_LANGUAGE)));
    }

    private void initTheme() {
        final List<String> list = new ArrayList<>();
        list.add("default");
        this.themeChoiceBox.setItems(FXCollections.observableArrayList(list));
        this.themeChoiceBox.setValue(this.settings.getProperty(Settings.ROOT_THEME));
    }

    private void initFontSizeSlider() {
        final String fontSizeValue = this.settings.getProperty(Settings.FONT_SIZE);
        this.fontSize.setText(fontSizeValue);
        this.fontSizeSlider.setValue(Double.parseDouble(fontSizeValue));
        this.fontSizeSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            this.fontSize.setText(String.valueOf(Math.round(new_val.doubleValue())));
            StyleUtil.setLabelStyle(this.settingsRoot, ColorUtil.getHexColor(this.nickColorPicker.getValue()));
            StyleUtil.setMessageStyle(
                    this.ownerRoot,
                    String.valueOf(new_val),
                    ColorUtil.getHexColor(this.nickColorPicker.getValue()),
                    ColorUtil.getHexColor(this.separatorColorPicker.getValue()),
                    ColorUtil.getHexColor(this.messageColorPicker.getValue()),
                    ColorUtil.getHexColor(this.directMessageColorPicker.getValue())
            );
        });
    }

    private void initTransparencySlider() {
        final String backgroundTransparencyValue = this.settings.getProperty(Settings.ROOT_BACKGROUND_TRANSPARENCY);
        this.transparencyValue.setText(backgroundTransparencyValue);
        this.transparencySlider.setValue(Long.valueOf(backgroundTransparencyValue));
        this.transparencySlider.valueProperty().addListener((ov, old_val, new_val) -> {
            getOwner().setOpacity((Double) new_val / 100);
            this.transparencyValue.setText(String.valueOf(Math.round(new_val.doubleValue())));
        });
    }

    private void initBackGroundColorPicker() {
        this.backgroundColorPicker.setValue(Color.valueOf(this.settings.getProperty(Settings.ROOT_BACKGROUND_COLOR)));
        this.backgroundColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            StyleUtil.setRootStyle(Arrays.asList(this.ownerRoot, this.settingsRoot),
                    ColorUtil.getHexColor(this.baseColorPicker.getValue()),
                    ColorUtil.getHexColor(new_val)
            );
        });
    }

    private void initNickColorPicker() {
        this.nickColorPicker.setValue(Color.valueOf(this.settings.getProperty(Settings.FONT_NICK_COLOR)));
        this.nickColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            StyleUtil.setLabelStyle(this.settingsRoot, ColorUtil.getHexColor(this.nickColorPicker.getValue()));
            StyleUtil.setMessageStyle(
                    this.ownerRoot,
                    this.fontSize.getText(),
                    ColorUtil.getHexColor(new_val),
                    ColorUtil.getHexColor(this.separatorColorPicker.getValue()),
                    ColorUtil.getHexColor(this.messageColorPicker.getValue()),
                    ColorUtil.getHexColor(this.directMessageColorPicker.getValue())
            );

        });
    }

    private void initSeparatorColorPicker() {
        this.separatorColorPicker.setValue(Color.valueOf(this.settings.getProperty(Settings.FONT_SEPARATOR_COLOR)));
        this.separatorColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            StyleUtil.setLabelStyle(this.settingsRoot, ColorUtil.getHexColor(this.nickColorPicker.getValue()));
            StyleUtil.setMessageStyle(
                    this.ownerRoot,
                    this.fontSize.getText(),
                    ColorUtil.getHexColor(this.nickColorPicker.getValue()),
                    ColorUtil.getHexColor(new_val),
                    ColorUtil.getHexColor(this.messageColorPicker.getValue()),
                    ColorUtil.getHexColor(this.directMessageColorPicker.getValue())
            );

        });
    }

    private void initMessageColorPicker() {
        this.messageColorPicker.setValue(Color.valueOf(this.settings.getProperty(Settings.FONT_MESSAGE_COLOR)));
        this.messageColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            StyleUtil.setLabelStyle(this.settingsRoot, ColorUtil.getHexColor(this.nickColorPicker.getValue()));
            StyleUtil.setMessageStyle(
                    this.ownerRoot,
                    this.fontSize.getText(),
                    ColorUtil.getHexColor(this.nickColorPicker.getValue()),
                    ColorUtil.getHexColor(this.separatorColorPicker.getValue()),
                    ColorUtil.getHexColor(new_val),
                    ColorUtil.getHexColor(this.directMessageColorPicker.getValue())
            );

        });
    }

    private void initDirectMessageColorPicker() {
        this.directMessageColorPicker.setValue(Color.valueOf(this.settings.getProperty(Settings.FONT_DIRECT_MESSAGE_COLOR)));
        this.directMessageColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            StyleUtil.setLabelStyle(this.settingsRoot, ColorUtil.getHexColor(this.nickColorPicker.getValue()));
            StyleUtil.setMessageStyle(
                    this.ownerRoot,
                    this.fontSize.getText(),
                    ColorUtil.getHexColor(this.nickColorPicker.getValue()),
                    ColorUtil.getHexColor(this.separatorColorPicker.getValue()),
                    ColorUtil.getHexColor(this.messageColorPicker.getValue()),
                    ColorUtil.getHexColor(new_val)
            );

        });
    }

    public void confirmAction() {
        final ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.openDialog(getStage(), this.settings, this.nickColorPicker.getValue(),
                this.baseColorPicker.getValue(), this.backgroundColorPicker.getValue());
        final Stage stage = confirmDialog.getStage();
        stage.setOnCloseRequest(event -> {
            if (confirmDialog.isConfirmed()) {
                flushSettings();
            }
        });
    }

    private void flushSettings() {
        this.settings.setProperty(Settings.ROOT_BACKGROUND_TRANSPARENCY, this.transparencyValue.getText());
        this.settings.setProperty(Settings.FONT_SIZE, this.fontSize.getText());
        this.settings.setProperty(Settings.ROOT_LANGUAGE, getLanguage(this.languageChoiceBox.getValue()));
        final String value = this.themeChoiceBox.getValue();
        this.settings.setProperty(Settings.ROOT_THEME, value);

        this.settings.setProperty(Settings.ROOT_BASE_COLOR, ColorUtil.getHexColor(this.baseColorPicker.getValue()));
        this.settings.setProperty(Settings.ROOT_BACKGROUND_COLOR, ColorUtil.getHexColor(this.backgroundColorPicker.getValue()));
        this.settings.setProperty(Settings.FONT_NICK_COLOR, ColorUtil.getHexColor(this.nickColorPicker.getValue()));
        this.settings.setProperty(Settings.FONT_SEPARATOR_COLOR, ColorUtil.getHexColor(this.separatorColorPicker.getValue()));
        this.settings.setProperty(Settings.FONT_MESSAGE_COLOR, ColorUtil.getHexColor(this.messageColorPicker.getValue()));
        this.settings.setProperty(Settings.FONT_DIRECT_MESSAGE_COLOR, ColorUtil.getHexColor(this.directMessageColorPicker.getValue()));
        AppProperty.setProperties("./settings/settings.properties", this.settings);
        final ChatController chatController = (ChatController) this.ownerRoot.getUserData();
        chatController.setSettings(this.settings);
        chatController.getSetting().setDisable(false);
    }

    public void cancelAction() {
        getStage().fireEvent(new WindowEvent(getStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    private String getLanguage(final String value) {
        for (String key : this.languages.keySet()) {
            if (this.languages.get(key).equals(value)) {
                return key;
            }
        }
        return "en";
    }

    public void commandsDataAction() {
        final CRUDRepository<Command> repository = new JSONCommandRepository("./data/commands.json");
        final Set<Command> commands = repository.getAll();
        final Optional<Command> first = commands.stream().findFirst();
        if (first.isPresent()) {
            final Command command = first.get();
            final Set<String> fields = getFields(command.getClass().getDeclaredFields());
            openDialog(new HashSet<>(commands), fields);
        }
    }

    public void usersDataAction() {
        final CRUDRepository<User> repository = new JSONUserRepository("./data/users.json");
        final Set<User> commands = repository.getAll();
        final Optional<User> first = commands.stream().findFirst();
        if (first.isPresent()) {
            final User user = first.get();
            final Set<String> fields = getFields(user.getClass().getDeclaredFields());
            openDialog(new HashSet<>(commands), fields);
        }
    }

    public void ranksDataAction() {
        final CRUDRepository<Rank> repository = new JSONRankRepository("./data/ranks.json");
        final Set<Rank> ranks = repository.getAll();
        final Optional<Rank> first = ranks.stream().findFirst();
        if (first.isPresent()) {
            final Rank rank = first.get();
            final Set<String> fields = getFields(rank.getClass().getDeclaredFields());
            openDialog(new HashSet<>(ranks), fields);
        }
    }

    public void smilesDataAction() {
        final CRUDRepository<Smile> repository = new JSONSmileRepository("./data/smiles.json");
        final Set<Smile> smiles = repository.getAll();
        final Optional<Smile> first = smiles.stream().findFirst();
        if (first.isPresent()) {
            final Smile smile = first.get();
            final Set<String> fields = getFields(smile.getClass().getDeclaredFields());
            openDialog(new HashSet<>(smiles), fields);
        }
    }

    public void directsDataAction() {
        final CRUDRepository<Direct> repository = new JSONDirectRepository("./data/directs.json");
        final Set<Direct> directs = repository.getAll();
        final Optional<Direct> first = directs.stream().findFirst();
        if (first.isPresent()) {
            final Direct direct = first.get();
            final Set<String> fields = getFields(direct.getClass().getDeclaredFields());
            openDialog(new HashSet<>(directs), fields);
        }
    }

    private Set<String> getFields(final Field[] declaredFields) {
        final Set<String> fields = new HashSet<>();
        for (Field field : declaredFields) {
            fields.add(field.getName());
        }
        return fields;
    }

    private void openDialog(final Set<Object> objects, final Set<String> fields) {
        final DataDialog dataDialog = new DataDialog();
        dataDialog.openDialog(
                getStage(),
                this.settings,
                this.nickColorPicker.getValue(),
                this.baseColorPicker.getValue(),
                this.backgroundColorPicker.getValue(),
                objects,
                fields);
    }

    private Stage getStage() {
        return (Stage) settingsRoot.getScene().getWindow();
    }

    private Stage getOwner() {
        return Main.stage;
    }

    public Node getRoot() {
        return this.settingsRoot;
    }

    private Node getOwnerRoot() {
        final Stage owner = getOwner();
        return owner.getScene().lookup("#root");
    }
}
