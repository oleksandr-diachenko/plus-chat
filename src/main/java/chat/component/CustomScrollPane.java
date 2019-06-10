package chat.component;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.ScrollPane;

public class CustomScrollPane extends ScrollPane {

    public void bindPrefHeightProperty(ReadOnlyDoubleProperty readOnlyDoubleProperty) {
        prefHeightProperty().bind(readOnlyDoubleProperty);
    }

    public void bindValueProperty(ReadOnlyDoubleProperty readOnlyDoubleProperty) {
        vvalueProperty().bind(readOnlyDoubleProperty);
    }
}
