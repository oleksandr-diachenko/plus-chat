package chat.component;

import javafx.scene.layout.VBox;

public class CustomVBox extends VBox {

    public CustomStage getStage() {
        return (CustomStage) getScene().getWindow();
    }

    public CustomStage getOwner() {
        return (CustomStage) getStage().getOwner();
    }
}
