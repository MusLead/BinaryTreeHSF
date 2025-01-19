package de.hsfd.binary_tree.services.wrapper;

@SuppressWarnings("rawtypes")
public class IntComparable implements Comparable {
    private final int value;

    public IntComparable(int value) {
        this.value = value;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof IntComparable other)) {
            throw new ClassCastException("Cannot compare IntComparable with " + o.getClass());
        }
        return Integer.compare(this.value, other.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
