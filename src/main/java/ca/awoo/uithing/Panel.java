package ca.awoo.uithing;

import javax.swing.JPanel;

import java.awt.Component;
import java.awt.LayoutManager;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Panel extends JPanel {
    public Panel(Function<Panel, LayoutManager> layout, Supplier<List<Pair<Component, Object>>> components) {
        super();
        setLayout(layout.apply(this));
        for (Pair<Component, Object> component : components.get()) {
            add(component.first(), component.second());
        }
    }

    public Panel(LayoutManager layout, Supplier<List<Pair<Component, Object>>> components) {
        this(p -> layout, components);
    }
}
