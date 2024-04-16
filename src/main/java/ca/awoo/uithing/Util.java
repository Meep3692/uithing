package ca.awoo.uithing;

import java.awt.Component;
import java.util.Arrays;
import java.util.List;

public class Util {
    @SafeVarargs
    public static List<Pair<Component, Object>> components(Pair<Component, Object>... components) {
        return Arrays.asList(components);
    }

    public static Pair<Component, Object> component(Component component, Object constraints) {
        return new Pair<>(component, constraints);
    }

    public static Pair<Component, Object> component(Component component) {
        return new Pair<>(component, null);
    }
}
