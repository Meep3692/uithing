package ca.awoo.uithing;

import java.util.function.Consumer;

import javax.swing.JTextField;

public class TextField extends JTextField {

    public TextField(){
        super();
    }

    public TextField(Consumer<String> onChange){
        super();
        this.addActionListener(e -> onChange.accept(this.getText()));
    }

    public TextField(String text){
        super(text);
    }

    public TextField(String text, Consumer<String> onChange){
        super(text);
        this.addActionListener(e -> onChange.accept(this.getText()));
    }

    public TextField(Observable<String> text){
        super(text.get());
        text.addObserver(this::setText);
        this.addActionListener(e -> text.set(this.getText()));
    }
}
