package chat.component;

import javafx.scene.control.Button;

public class CustomButton extends Button {

    public void disable() {
        setDisable(true);
    }

    public void enable() {
        setDisable(false);
    }
}
