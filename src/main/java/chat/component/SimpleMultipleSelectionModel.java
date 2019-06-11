package chat.component;

import com.sun.javafx.collections.ImmutableObservableList;
import javafx.collections.ObservableList;
import javafx.scene.control.MultipleSelectionModel;

public class SimpleMultipleSelectionModel<T> extends MultipleSelectionModel<T> {
    @Override
    public ObservableList<Integer> getSelectedIndices() {
        return new ImmutableObservableList<>();
    }

    @Override
    public ObservableList<T> getSelectedItems() {
        return new ImmutableObservableList<T>();
    }

    @Override
    public void selectIndices(int index, int... indices) {
        //do nothing
    }

    @Override
    public void selectAll() {
        //do nothing
    }

    @Override
    public void clearAndSelect(int index) {
        //do nothing
    }

    @Override
    public void select(int index) {
        //do nothing
    }

    @Override
    public void select(T obj) {
        //do nothing
    }

    @Override
    public void clearSelection(int index) {
        //do nothing
    }

    @Override
    public void clearSelection() {
        //do nothing
    }

    @Override
    public boolean isSelected(int index) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public void selectPrevious() {
        //do nothing
    }

    @Override
    public void selectNext() {
        //do nothing
    }

    @Override
    public void selectFirst() {
        //do nothing
    }

    @Override
    public void selectLast() {
        //do nothing
    }
}
