package game.utils;

import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;

import java.lang.reflect.Method;

public class ProtoBufUtils {

    private ProtoBufUtils() {}

    /**
     * 获取proto文件定义的文件的完整类名
     *
     * @param javaPackageName    文件中的java_package
     * @param javaOuterClassName 文件中的java_outer_classname
     * @param messageName        消息类名字
     */
    @SuppressWarnings("unchecked")
    public static <T extends MessageLite> Class<T> findMessageClass(String javaPackageName, String javaOuterClassName, String messageName) throws ClassNotFoundException {
        String classFullName = javaPackageName + "." + javaOuterClassName + "$" + messageName;
        return (Class<T>) Class.forName(classFullName);
    }

    /**
     * 反射获取消息体的Parser对象
     *
     * @param messageClass 消息类
     */
    @SuppressWarnings("unchecked")
    public static <T> Parser<T> findMessageParser(Class<T> messageClass) throws ReflectiveOperationException {

        // proto2 静态parser域,public的
//        Field field = messageClass.getDeclaredField("PARSER");
//        field.setAccessible(true);
//        return (Parser<T>) field.get(null);

        // protoBuf3获取parser的静态方法 parser();
        Method method = messageClass.getDeclaredMethod("parser");
        method.setAccessible(true);
        return (Parser<T>) method.invoke(null);
    }
}
