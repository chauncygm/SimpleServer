package struct;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  gongshengjun
 * @date    2021/3/21 16:07
 */
public abstract class ReadArrayEs<T> {

    private final ReadArray<T>[] values;

    public ReadArrayEs(ReadArray<T>[] values) {
        this.values = values;
    }

    public ReadArrayEs(String str, String bigDelimiter, String smallDelimiter) {
        List<ReadArray<T>> list = new ArrayList<>();
        for (String s : str.split(bigDelimiter)) {
            ReadArray<T> array = parseValue(s, smallDelimiter);
            list.add(array);
        }
        values = new ReadArray[list.size()];
        list.toArray(values);
    }

    protected abstract ReadArray<T> parseValue(String str, String delimiter);

    public ReadArray<T>[] getValues() {
        return values;
    }

    public ReadArray<T> get(int index) {
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
