package ca.awoo.uithing;

import javax.swing.JLabel;

public class Label extends JLabel {
    public Label(String text) {
        super(text);
    }

    public Label(Observable<String> text) {
        super(text.get());
        text.addObserver(this::setText);
    }
}
