package de.hsfd.binary_tree.services.wrapper;

@SuppressWarnings("rawtypes")
public class CharComparable implements Comparable{
    private final char value;

    public CharComparable(char value) {
        this.value = value;
    }

    @Override
    public int compareTo(Object o) {
        if(!(o instanceof CharComparable)){
            throw new ClassCastException();
        }
        return Character.compare(value, ((CharComparable)o).value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
