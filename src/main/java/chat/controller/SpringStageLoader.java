package chat.controller;

import chat.util.AppProperty;
import chat.util.ResourceBundleControl;
import chat.util.Settings;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

@Component
@NoArgsConstructor
public class SpringStageLoader implements ApplicationContextAware {

    private static ApplicationContext staticContext;
    //инъекция заголовка главного окна
    @Value("${title}")
    private String appTitle;
    private static String staticTitle;
    private AppProperty settingsProperties;

    private static final String FXML_DIR = "/view/";

    @Autowired
    public SpringStageLoader(@Qualifier("settingsProperties") AppProperty settingsProperties) {
        this.settingsProperties = settingsProperties;
    }

    /**
     * Загрузка корневого узла и его дочерних элементов из fxml шаблона
     * @param fxmlName наименование *.fxml файла в ресурсах
     * @return объект типа Region
     * @throws IOException бросает исключение ввода-вывода
     */
    public Region load(String fxmlName) throws IOException {
        Properties settings = settingsProperties.getProperty();
        String language = settings.getProperty(Settings.ROOT_LANGUAGE);
        ResourceBundle bundle = ResourceBundle.getBundle(
                "bundles.chat", new Locale(language), new ResourceBundleControl());
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);
        // setLocation необходим для корректной загрузки включенных шаблонов,
        // без этого получим исключение javafx.fxml.LoadException: Base location is undefined.
        loader.setLocation(SpringStageLoader.class.getResource(FXML_DIR + fxmlName + ".fxml"));
        // setLocation необходим для корректной того чтобы loader видел наши кастомные котнролы
        loader.setClassLoader(SpringStageLoader.class.getClassLoader());
        loader.setControllerFactory(staticContext::getBean);
        return loader.load(SpringStageLoader.class.getResourceAsStream(FXML_DIR + fxmlName + ".fxml"));
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
