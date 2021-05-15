package struct;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  gongshengjun
 * @date    2021/3/21 17:04
 */
public class LongReadArray extends ReadArray<Long>{

    public LongReadArray(String str, String delimiter) {
        super(str, delimiter);
    }

    @Override
    protected Long[] parseValue(String str, String delimiter) {
        if (str == null || str.trim().isEmpty()) {
            return new Long[0];
        }
        List<Long> list = new ArrayList<>();
        for (String s : str.split(delimiter)) {
            list.add(Long.parseLong(s));
        }

        Long[] values = new Long[list.size()];
        return list.toArray(values);
    }
}
