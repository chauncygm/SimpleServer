//package com.utils;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.Collection;
//import java.util.Map;
//
///**
// * @author by user
// * @description TODO
// * @date 2022/7/7 11:44
// */
//public class JsonUtil {
//
//    /**
//     * 必须使用ThreadLocal - 我们在中途可能修改ObjectMapper的状态，这是我们避免线程安全问题的方案。
//     */
//    private static final ThreadLocal<ObjectMapper> MAPPER_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
//        final ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
//        // 默认采用比较宽容的方式反序列化
//        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        return objectMapper;
//    });
//
//    /**
//     * 一定要私有，不可以暴露给外层，否则会导致应用程序对{@code Jackson}产生强依赖
//     */
//    private static ObjectMapper getMapper() {
//        return MAPPER_THREAD_LOCAL.get();
//    }
//
//    // ---------------------------------- 基本支持 ---------------------------
//
//    /**
//     * 仅限Debug使用 将bean转为漂亮的json字符串
//     */
//    public static String writeAsPrettyJson(@Nonnull Object bean) {
//        try {
//            return getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(bean);
//        } catch (JsonProcessingException e) {
//            // 之所以捕获，是因为，出现异常的地方应该是非常少的
//            return ExceptionUtils.rethrow(e);
//        }
//    }
//
//    /**
//     * 将bean转换为json字符串
//     */
//    public static String writeAsJson(@Nonnull Object bean) {
//        try {
//            return getMapper().writeValueAsString(bean);
//        } catch (JsonProcessingException e) {
//            // 之所以捕获，是因为，出现异常的地方应该是非常少的
//            return ExceptionUtils.rethrow(e);
//        }
//    }
//
//    /**
//     * 解析json字符串为java对象。
//     */
//    public static <T> T readFromJson(@Nonnull String json, @Nonnull Class<T> clazz) {
//        try {
//            return getMapper().readValue(json, clazz);
//        } catch (IOException e) {
//            return ExceptionUtils.rethrow(e);
//        }
//    }
//
//    /**
//     * 解析json字符串为java对象，并在出现不能识别的字段时失败。
//     */
//    public static <T> T readFromJsonFailOnUnknownProperties(@Nonnull String json, @Nonnull Class<T> clazz) {
//        final ObjectMapper mapper = getMapper();
//        mapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        try {
//            return mapper.readValue(json, clazz);
//        } catch (IOException e) {
//            return ExceptionUtils.rethrow(e);
//        } finally {
//            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        }
//    }
//
//    /**
//     * 将bean转换为json对应的字节数组
//     */
//    public static byte[] writeAsJsonBytes(@Nonnull Object bean) {
//        try {
//            return getMapper().writeValueAsBytes(bean);
//        } catch (JsonProcessingException e) {
//            return ExceptionUtils.rethrow(e);
//        }
//    }
//
//    /**
//     * 解析json字符串对应的字节数组为java对象。
//     */
//    public static <T> T readFromJsonBytes(@Nonnull byte[] jsonBytes, @Nonnull Class<T> clazz) {
//        try {
//            return getMapper().readValue(jsonBytes, clazz);
//        } catch (IOException e) {
//            return ExceptionUtils.rethrow(e);
//        }
//    }
//
//    // ---------------------------------- 集合支持 ---------------------------
//
//    public static <C extends Collection> C readCollectionFromJson(@Nonnull String json,
//                                                                  @Nonnull Class<C> collectionClass, @Nonnull Class<?> elementClass) {
//        final ObjectMapper mapper = getMapper();
//        final CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
//        try {
//            return mapper.readValue(json, collectionType);
//        } catch (IOException e) {
//            return ExceptionUtils.rethrow(e);
//        }
//
//    }
//
//    // ---------------------------------- Map支持 ---------------------------
//
//    /**
//     * 解析json字符串为map对象。
//     */
//    public static <M extends Map> M readMapFromJson(@Nonnull String json,
//                                                    @Nonnull Class<M> mapClass,
//                                                    @Nonnull Class<?> keyClass,
//                                                    @Nonnull Class<?> valueClass) {
//        ObjectMapper mapper = getMapper();
//        MapType mapType = mapper.getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
//        try {
//            return mapper.readValue(json, mapType);
//        } catch (IOException e) {
//            return ExceptionUtils.rethrow(e);
//        }
//    }
//
//    // ---------------------------------- 流 ---------------------------
//
//    public static void writeToOutputStream(OutputStream out, Object value) {
//        try {
//            getMapper().writeValue(out, value);
//        } catch (IOException e) {
//            ExceptionUtils.rethrow(e);
//        }
//    }
//
//    public static <T> T readFromInputStream(InputStream in, Class<T> clazz) {
//        try {
//            return getMapper().readValue(in, clazz);
//        } catch (IOException e) {
//            return ExceptionUtils.rethrow(e);
//        }
//    }
//}
