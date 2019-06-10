package chat.component;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class CustomListView<T> extends ListView<T> {

    public void setData(ObservableList<T> data) {
        setItems(data);
    }

    public void setSelected(int index) {
        getSelectionModel().select(index);
    }

    public int getSelected() {
        return getSelectionModel().getSelectedIndex();
    }
}
