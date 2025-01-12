package ca.awoo.uithing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static ca.awoo.uithing.Util.components;
import static ca.awoo.uithing.Util.component;
import ca.awoo.uithing.Observable.*;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class App {

    private static class TodoItem implements Observable<TodoItem>{
        private String text;
        private boolean done;

        public TodoItem(String text) {
            this.text = text;
            this.done = false;
        }

        public String getText() {
            return text;
        }

        public boolean isDone() {
            return done;
        }

        public void setDone(boolean done) {
            this.done = done;
            notifyObservers();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((text == null) ? 0 : text.hashCode());
            result = prime * result + (done ? 1231 : 1237);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            TodoItem other = (TodoItem) obj;
            if (text == null) {
                if (other.text != null)
                    return false;
            } else if (!text.equals(other.text))
                return false;
            if (done != other.done)
                return false;
            return true;
        }

        @Override
        public String toString() {
            return text + (done ? " √" : " X");
        }

        private final Set<Consumer<TodoItem>> observers = new HashSet<>();

        private void notifyObservers() {
            observers.forEach(observer -> observer.accept(this));
        }

        @Override
        public TodoItem get() {
            return this;
        }

        @Override
        public void set(TodoItem value) {
            this.text = value.text;
            this.done = value.done;

        }

        @Override
        public void addObserver(Consumer<TodoItem> observer) {
            observers.add(observer);
        }

        @Override
        public void removeObserver(Consumer<TodoItem> observer) {
            observers.remove(observer);
        }

        
    }
    public static void main(String[] args) {
        menu().setVisible(true);
    }

    private static Frame menu(){
        Frame todo = todoApp();
        Frame counter = counterApp();
        Frame menu = new Frame(f -> new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS), Observable.of("Examples"), () -> 
            components(
                component(new Button("Todo app", () -> {
                    todo.setVisible(true);
                })),
                component(new Button("Counter app", () -> {
                    counter.setVisible(true);
                }))
            )
        );
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return menu;
    }

    private static Frame counterApp(){
        Observable<Integer> count = Observable.of(0);
        Frame frame = new Frame(new BorderLayout(), Observable.of("Counter app"), () ->
            components(
                component(new Button("Increment", () -> count.change(i -> i + 1)), BorderLayout.NORTH),
                component(new Label(count.map(i -> "Clicked " + i + " times")), BorderLayout.CENTER)
            )
        );
        return frame;
    }

    private static Frame todoApp(){
        Observable<String> title = Observable.of("Todo app");
        Frame frame = new Frame(new BorderLayout(), title, () -> {
            ObservableList<TodoItem, TodoItem> todos = Observable.ofList(new ArrayList<TodoItem>());
            todos.addObserver(l -> {
                long count = l.stream().filter(TodoItem::isDone).count();
                title.set("Todo app (" + count + "/" + l.size() + ")");
            });
            return components(
                component(new Panel(new FlowLayout(), () -> {
                    TextField textField = new TextField();
                    Dimension preferDimension = textField.getPreferredSize();
                    preferDimension.width = 200;
                    textField.setPreferredSize(preferDimension);
                    Button button = new Button("Add", () -> {
                        todos.add(new TodoItem(textField.getText()));
                        textField.setText("");
                    });
                    return components(
                        component(textField),
                        component(button)
                    );
                }), BorderLayout.NORTH),
                component(new ListPanel<>(panel -> new BoxLayout(panel, BoxLayout.Y_AXIS), todos, todo -> {
                    Observable<Boolean> done = Observable.of(todo.isDone());
                    done.addObserver(todo::setDone);
                    Button delete = new Button("Delete", () -> todos.remove(todo));
                    return new Panel(p -> new BoxLayout(p, BoxLayout.X_AXIS), () ->
                        components(
                            component(new CheckBox(todo.getText(), done), BorderLayout.CENTER),
                            component(delete, BorderLayout.EAST)
                        ));
                }), BorderLayout.CENTER)
            );
        });
        frame.setSize(frame.getWidth(), 200);
        return frame;
    }
}
