package struct;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  gongshengjun
 * @date    2021/3/21 17:04
 */
public class IntegerReadArray extends ReadArray<Integer>{

    public IntegerReadArray(String str, String delimiter) {
        super(str, delimiter);
    }

    @Override
    protected Integer[] parseValue(String str, String delimiter) {
        if (str == null || str.trim().isEmpty()) {
            return new Integer[0];
        }
        List<Integer> list = new ArrayList<>();
        for (String s : str.split(delimiter)) {
            list.add(Integer.parseInt(s));
        }

        Integer[] values = new Integer[list.size()];
        return list.toArray(values);
    }
}
