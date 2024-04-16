package ca.awoo.uithing;

import javax.swing.JPanel;

import java.awt.Component;
import java.awt.LayoutManager;
import java.util.function.Supplier;

public class Panel extends JPanel {
    public Panel(LayoutManager layout, Supplier<Component[]> components) {
        super();
        setLayout(layout);
        for (Component component : components.get()) {
            add(component);
        }
    }
}
