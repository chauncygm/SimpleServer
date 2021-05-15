package struct;

/**
 * @author  gongshengjun
 * @date    2021/3/21 17:11
 */
public class IntegerReadArrayEs extends ReadArrayEs<Integer>{

    public IntegerReadArrayEs(String str, String bigDelimiter, String smallDelimiter) {
        super(str, bigDelimiter, smallDelimiter);
    }

    @Override
    protected ReadArray<Integer> parseValue(String str, String delimiter) {
      return new IntegerReadArray(str, delimiter);
    }
}
