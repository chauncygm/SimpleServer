package struct;

/**
 * @author  gongshengjun
 * @date    2021/3/21 17:11
 */
public class LongReadArrayEs extends ReadArrayEs<Integer>{

    public LongReadArrayEs(String str, String bigDelimiter, String smallDelimiter) {
        super(str, bigDelimiter, smallDelimiter);
    }

    @Override
    protected ReadArray<Integer> parseValue(String str, String delimiter) {
      return new IntegerReadArray(str, delimiter);
    }
}
