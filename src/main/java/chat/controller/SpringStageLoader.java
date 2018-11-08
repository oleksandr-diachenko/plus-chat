package chat.controller;

import chat.util.AppProperty;
import chat.util.ResourceBundleControl;
import chat.util.Settings;
import insidefx.undecorator.UndecoratorScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

@Component
public class SpringStageLoader implements ApplicationContextAware {

    private final static Logger logger = LogManager.getLogger(SpringStageLoader.class);

    private static ApplicationContext staticContext;
    //инъекция заголовка главного окна
    @Value("${title}")
    private String appTitle;
    private static String staticTitle;
    public static Stage chatStage;

    private static final String FXML_DIR = "/view/";

    /**
     * Загрузка корневого узла и его дочерних элементов из fxml шаблона
     * @param fxmlName наименование *.fxml файла в ресурсах
     * @return объект типа Region
     * @throws IOException бросает исключение ввода-вывода
     */
    public static Region load(String fxmlName) throws IOException {
        final Properties settings = AppProperty.getProperty("./settings/settings.properties");
        final String language = settings.getProperty(Settings.ROOT_LANGUAGE);
        final ResourceBundle bundle = ResourceBundle.getBundle("bundles.chat", new Locale(language), new ResourceBundleControl());
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);
        // setLocation необходим для корректной загрузки включенных шаблонов, таких как productTable.fxml,
        // без этого получим исключение javafx.fxml.LoadException: Base location is undefined.
        loader.setLocation(SpringStageLoader.class.getResource(FXML_DIR + fxmlName + ".fxml"));
        // setLocation необходим для корректной того чтобы loader видел наши кастомные котнролы
        loader.setClassLoader(SpringStageLoader.class.getClassLoader());
        loader.setControllerFactory(staticContext::getBean);
        return loader.load(SpringStageLoader.class.getResourceAsStream(FXML_DIR + fxmlName + ".fxml"));
    }

    /**
     * Реализуем загрузку главной сцены. На закрытие сцены стоит обработчик, которых выходит из приложения
     * @return главную сцену
     */
    public static Stage loadMain() {
        final Properties settings = AppProperty.getProperty("./settings/settings.properties");
        final Stage primaryStage = new Stage();
        chatStage = primaryStage;
        primaryStage.setAlwaysOnTop(Boolean.parseBoolean(settings.getProperty(Settings.ROOT_ALWAYS_ON_TOP)));
        primaryStage.getIcons().add(new Image("/img/logo.png"));
        try {
            final UndecoratorScene undecorator = new UndecoratorScene(primaryStage, load("chat"));
            stageEvents(primaryStage, undecorator);
            undecorator.getStylesheets().add("/theme/" + settings.getProperty(Settings.ROOT_THEME) + "/chat.css");
            primaryStage.setScene(undecorator);
            primaryStage.setTitle("(+) chat");
            primaryStage.show();
        } catch (IOException exception) {
            exception.printStackTrace();
            logger.error(exception.getMessage(), exception);
            throw new RuntimeException("Chat view failed to load");
        }
        return primaryStage;
    }

    private static void stageEvents(final Stage primaryStage, final UndecoratorScene undecorator) {
        primaryStage.setOnCloseRequest(we -> {
            we.consume();
            undecorator.setFadeOutTransition();
        });
    }

    /**
     * Передаем данные в статические поля в реализации метода интерфейса ApplicationContextAware,
     т.к. методы их использующие тоже статические
     */
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringStageLoader.staticContext = context;
        SpringStageLoader.staticTitle = appTitle;
    }
}
