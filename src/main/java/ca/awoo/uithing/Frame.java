package ca.awoo.uithing;

import java.awt.Component;
import java.awt.LayoutManager;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.JFrame;

public class Frame extends JFrame {
    public Frame(LayoutManager layout, Supplier<Component[]> components) {
        this(layout, "", components);
    }

    public Frame(LayoutManager layout, String title, Supplier<Component[]> components) {
        super();
        setTitle(title);
        setLayout(layout);
        for (Component component : components.get()) {
            add(component);
        }
        pack();
    }

    /*public Frame(LayoutManager layout, Observable<String> title, Supplier<Component[]> components) {
        this(layout, title.get(), components);
        title.addObserver(this::setTitle);
    }*/

    public Frame(LayoutManager layout, Observable<String> title, Supplier<List<Pair<Component, Object>>> components) {
        super();
        setTitle(title.get());
        setLayout(layout);
        for (Pair<Component, Object> component : components.get()) {
            add(component.first(), component.second());
        }
        title.addObserver(this::setTitle);
        pack();
    }

}
