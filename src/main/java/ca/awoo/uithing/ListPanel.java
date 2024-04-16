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
                System.out.println("ListPanel update");
                Set<Pair<Model, Component>> oldComponents = components;
                Set<Pair<Model, Component>> newComponents = new HashSet<>();
                for(Pair<Model, Component> entry : oldComponents){
                    System.out.println("Currently here: " + entry);
                }
                for (Model model : models) {
                    Optional<Pair<Model, Component>> entry = oldComponents.stream().filter(e -> equals.test(e.first(), model)).findFirst();
                    if (entry.isPresent()) {
                        System.out.println("Preserving old component: " + model + ", " + entry);
                        newComponents.add(entry.get());
                        oldComponents.remove(entry.get());
                    } else {
                        System.out.println("Creating new component: " + model);
                        Component component = componentFactory.apply(model);
                        newComponents.add(Pair.of(model, component));
                        add(component);
                    }
                }
                for (Pair<Model, Component> entry : oldComponents) {
                    System.out.println("Removing old component: " + entry);
                    remove(entry.second());
                }
                components = newComponents;
                revalidate();
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
