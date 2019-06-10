package chat.component;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomVBox extends VBox {

    public Stage getStage() {
        return (Stage) getScene().getWindow();
    }
}
