package ca.awoo.uithing;

import javax.swing.JCheckBox;

public class CheckBox extends JCheckBox {
    public CheckBox(String text) {
        super(text);
    }

    public CheckBox(String text, boolean selected) {
        super(text, selected);
    }

    public CheckBox(String text, Observable<Boolean> selected){
        super(text, selected.get());
        selected.addObserver(this::setSelected);
        this.addActionListener(e -> selected.set(this.isSelected()));
    }
}
