package ca.awoo.uithing;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Observable<T> {
    public static class ObservableObject<T> implements Observable<T> {
        private T value;

        private final Set<Consumer<T>> observers = new HashSet<>();

        public ObservableObject(T value) {
            this.value = value;
        }

        public void set(T value) {
            this.value = value;
            notifyObservers();
        }

        public T get() {
            return value;
        }

        public void addObserver(Consumer<T> observer) {
            observers.add(observer);
        }

        public void removeObserver(Consumer<T> observer) {
            observers.remove(observer);
        }

        private void notifyObservers() {
            observers.forEach(observer -> observer.accept(value));
        }
    }

    public static class ObservableList<T, U extends Observable<T>> implements Observable<List<U>>, List<U> {
        private List<U> value;
        private final Set<Consumer<List<U>>> observers = new HashSet<>();
        private Consumer<T> observer = newValue -> notifyObservers();

        public ObservableList(List<U> value) {
            this.value = value;
        }

        private void notifyObservers() {
            observers.forEach(observer -> observer.accept(value));
        }

        @Override
        public List<U> get() {
            return this;
        }

        @Override
        public void set(List<U> value) {
            value.clear();
            value.addAll(value);
        }

        @Override
        public void addObserver(Consumer<List<U>> observer) {
            observers.add(observer);
        }

        @Override
        public void removeObserver(Consumer<List<U>> observer) {
            observers.remove(observer);
        }

        @Override
        public int size() {
            return value.size();
        }

        @Override
        public boolean isEmpty() {
            return value.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return value.contains(o);
        }

        @Override
        public Iterator<U> iterator() {
            return value.iterator();
        }

        @Override
        public Object[] toArray() {
            return value.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return value.toArray(a);
        }

        @Override
        public boolean add(U e) {
            value.add(e);
            e.addObserver(observer);
            notifyObservers();
            return true;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean remove(Object o) {
            if (value.remove(o)) {
                notifyObservers();
                if(o instanceof Observable){
                    ((Observable<T>)o).removeObserver(observer);
                }
                return true;
            }
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return value.containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends U> c) {
            boolean changed = false;
            for (U o : c) {
                changed |= value.add(o);
            }
            if (changed) {
                notifyObservers();
            }
            return changed;
        }

        @Override
        public boolean addAll(int index, Collection<? extends U> c) {
            boolean changed = false;
            for (U o : c) {
                value.add(index++, o);
                changed = true;
            }
            if (changed) {
                notifyObservers();
            }
            return changed;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            boolean changed = false;
            for (Object o : c) {
                changed |= value.remove(o);
                if(o instanceof Observable){
                    ((Observable<T>)o).removeObserver(observer);
                }
            }
            if (changed) {
                notifyObservers();
            }
            return changed;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            boolean changed = false;
            for (Observable<T> o : value) {
                if (!c.contains(o)) {
                    value.remove(o);
                    changed = true;
                    o.removeObserver(observer);
                }
            }
            if (changed) {
                notifyObservers();
            }
            return changed;
        }

        @Override
        public void clear() {
            value.forEach(o -> o.removeObserver(observer));
            value.clear();
            notifyObservers();
        }

        @Override
        public U get(int index) {
            return value.get(index);
        }

        @Override
        public U set(int index, U element) {
            U old = value.set(index, element);
            old.removeObserver(observer);
            element.addObserver(observer);
            notifyObservers();
            return old;
        }

        @Override
        public void add(int index, U element) {
            value.add(index, element);
            element.addObserver(observer);
            notifyObservers();
        }

        @Override
        public U remove(int index) {
            U removed = value.remove(index);
            removed.removeObserver(observer);
            notifyObservers();
            return removed;
        }

        @Override
        public int indexOf(Object o) {
            return value.indexOf(o);
        }

        @Override
        public int lastIndexOf(Object o) {
            return value.lastIndexOf(o);
        }

        @Override
        public ListIterator<U> listIterator() {
            return value.listIterator();
        }

        @Override
        public ListIterator<U> listIterator(int index) {
            return value.listIterator(index);
        }

        @Override
        public List<U> subList(int fromIndex, int toIndex) {
            return value.subList(fromIndex, toIndex);
        }

        
    }

    public static <T> Observable<T> of(T value) {
        return new ObservableObject<>(value);
    }

    public static <T, U extends Observable<T>> ObservableList<T, U> ofList(List<U> value) {
        return new ObservableList<>(value);
    }

    public T get();
    public void set(T value);
    public void addObserver(Consumer<T> observer);
    public void removeObserver(Consumer<T> observer);
    public default <U> Observable<U> map(Function<T, U> function) {
        Observable<U> mapped = new ObservableObject<>(function.apply(this.get()));
        addObserver(newValue -> mapped.set(function.apply(newValue)));
        return mapped;
    }
    public default void change(Function<T, T> function){
        set(function.apply(get()));
    }
    
}
