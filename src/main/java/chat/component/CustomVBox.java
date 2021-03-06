package chat.component;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class CustomVBox extends VBox {

    public CustomStage getStage() {
        return (CustomStage) getScene().getWindow();
    }

    public CustomStage getOwner() {
        return (CustomStage) getStage().getOwner();
    }

    public ReadOnlyDoubleProperty getHeightProperty() {
        return heightProperty();
    }

    public void addNode(Node node) {
        getChildren().add(node);
    }

    public void clear() {
        getChildren().clear();
    }
}
