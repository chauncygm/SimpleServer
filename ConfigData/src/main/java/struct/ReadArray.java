package struct;

/**
 * @author  gongshengjun
 * @date    2021/3/21 15:43
 */
public abstract class ReadArray<T> {

    private final T[] values;

    public ReadArray(T[] values) {
        this.values = values;
    }

    public ReadArray(String str, String delimiter) {
        values = parseValue(str, delimiter);
    }

    protected abstract T[] parseValue(String str, String delimiter);

    public T[] getValues() {
        return values;
    }

    public T get(int index) {
        if (index < 0 || index >= values.length) {
            throw new IndexOutOfBoundsException("index: " + index);
        }
        return values[index];
    }

    public int indexOf(T value) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    public boolean contains(T value) {
        return indexOf(value) > 0;
    }
}
