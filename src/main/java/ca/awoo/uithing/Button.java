package ca.awoo.uithing;

import javax.swing.JButton;

public class Button extends JButton {
    public Button(String label, Runnable action) {
        super(label);
        addActionListener(e -> action.run());
    }

    public Button(Observable<String> label, Runnable action) {
        super(label.get());
        label.addObserver(this::setText);
        addActionListener(e -> action.run());
    }
}
