package ca.awoo.uithing;

import java.awt.Component;
import java.awt.LayoutManager;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ListPanel<Model> extends JPanel {
    private Set<Pair<Model, Component>> components;

    public ListPanel(Observable<List<Model>> list, BiPredicate<Model, Model> equals, Function<Model, Component> componentFactory) {
        super();
        components = new HashSet<>();
        list.addObserver(models -> {
            SwingUtilities.invokeLater(() -> {
                Set<Pair<Model, Component>> oldComponents = components;
                Set<Pair<Model, Component>> newComponents = new HashSet<>();
                removeAll();
                for (Model model : models) {
                    Optional<Pair<Model, Component>> entry = oldComponents.stream().filter(e -> equals.test(e.first(), model)).findFirst();
                    if (entry.isPresent()) {
                        newComponents.add(entry.get());
                        add(entry.get().second());
                    } else {
                        Component component = componentFactory.apply(model);
                        newComponents.add(Pair.of(model, component));
                        add(component);
                    }
                }
                components = newComponents;
                revalidate();
                repaint();
            });
        });
    }

    public ListPanel(Observable<List<Model>> list, Function<Model, Component> componentFactory) {
        this(list, Object::equals, componentFactory);
    }

    public ListPanel(LayoutManager layout, Observable<List<Model>> list, Function<Model, Component> componentFactory) {
        this(list, componentFactory);
        setLayout(layout);
    }

    public ListPanel(Function<ListPanel<Model>, LayoutManager> layout, Observable<List<Model>> list, Function<Model, Component> componentFactory) {
        this(list, componentFactory);
        setLayout(layout.apply(this));
    }
}
