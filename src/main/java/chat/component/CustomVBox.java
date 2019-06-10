package chat.component;

import chat.component.dialog.CustomStage;
import javafx.scene.layout.VBox;

public class CustomVBox extends VBox {

    public CustomStage getStage() {
        return (CustomStage) getScene().getWindow();
    }

    public CustomStage getOwner() {
        return (CustomStage) getStage().getOwner();
    }
}
