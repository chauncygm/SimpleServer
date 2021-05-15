package struct;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  gongshengjun
 * @date    2021/3/23 10:19
 */
public class FloatReadArray extends ReadArray<Float>{

    public FloatReadArray(String str, String delimiter) {
        super(str, delimiter);
    }

    @Override
    protected Float[] parseValue(String str, String delimiter) {
        if (str == null || str.trim().isEmpty()) {
            return new Float[0];
        }
        List<Float> list = new ArrayList<>();
        for (String s : str.split(delimiter)) {
            list.add(Float.parseFloat(s));
        }

        Float[] values = new Float[list.size()];
        return list.toArray(values);
    }
}
