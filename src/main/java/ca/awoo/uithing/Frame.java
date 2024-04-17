package ca.awoo.uithing;

import java.awt.Component;
import java.awt.LayoutManager;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.JFrame;

public class Frame extends JFrame {
    public Frame(Function<Frame, LayoutManager> layout, Observable<String> title, Supplier<List<Pair<Component, Object>>> components) {
        super();
        setTitle(title.get());
        setLayout(layout.apply(this));
        for (Pair<Component, Object> component : components.get()) {
            add(component.first(), component.second());
        }
        title.addObserver(this::setTitle);
        pack();
    }

    public Frame(LayoutManager layout, Observable<String> title, Supplier<List<Pair<Component, Object>>> components) {
        this(p -> layout, title, components);
    }

}
