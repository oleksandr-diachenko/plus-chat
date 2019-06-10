package chat.component;

import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CustomStage extends Stage {

    public void fireCloseEvent() {
        fireEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}
