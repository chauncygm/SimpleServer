package struct;

/**
 * @author  gongshengjun
 * @date    2021/3/21 17:11
 */
public class FloatReadArrayEs extends ReadArrayEs<Float>{

    public FloatReadArrayEs(String str, String bigDelimiter, String smallDelimiter) {
        super(str, bigDelimiter, smallDelimiter);
    }

    @Override
    protected ReadArray<Float> parseValue(String str, String delimiter) {
      return new FloatReadArray(str, delimiter);
    }
}
