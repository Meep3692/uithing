package ca.awoo.uithing;

import java.awt.Component;
import java.awt.LayoutManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ListPanel<Model> extends JPanel {
    private Map<Model, Component> components;

    public ListPanel(Observable<List<Model>> list, BiPredicate<Model, Model> equals, Function<Model, Component> componentFactory) {
        super();
        components = new HashMap<>();
        list.addObserver(models -> {
            SwingUtilities.invokeLater(() -> {
                System.out.println("ListPanel update");
                Map<Model, Component> oldComponents = components;
                Map<Model, Component> newComponents = new HashMap<>();
                for(Map.Entry<Model, Component> entry : oldComponents.entrySet()){
                    System.out.println("Currently here: " + entry.getKey() + " " + entry.getValue() + " " + entry.getValue().getName());
                }
                for (Model model : models) {
                    Optional<Map.Entry<Model, Component>> entry = oldComponents.entrySet().stream().filter(e -> equals.test(e.getKey(), model)).findFirst();
                    if (entry.isPresent()) {
                        System.out.println("Preserving old component: " + model + " " + entry.get().getValue() + " " + entry.get().getValue().getName());
                        newComponents.put(model, entry.get().getValue());
                        oldComponents.remove(entry.get().getKey());
                    } else {
                        System.out.println("Creating new component: " + model);
                        Component component = componentFactory.apply(model);
                        newComponents.put(model, component);
                        add(component);
                    }
                }
                for (Map.Entry<Model, Component> entry : oldComponents.entrySet()) {
                    System.out.println("Removing old component: " + entry.getKey() + " " + entry.getValue() + " " + entry.getValue().getName());
                    remove(entry.getValue());
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
