package com.example.messagecenter.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;

/**
 * jackson工具类
 *
 * @author lixiangqian
 * @since 2021/5/16 12:36
 */
@UtilityClass
public class JacksonUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public static String bean2Json(Object data) {
        return mapper.writeValueAsString(data);
    }

    @SneakyThrows
    public static <T> T json2Bean(String jsonData, Class<T> beanType) {
        return mapper.readValue(jsonData, beanType);
    }

    @SneakyThrows
    public static <T> List<T> json2List(String jsonData, Class<T> beanType) {
        var javaType = mapper.getTypeFactory().constructParametricType(List.class, beanType);
        return mapper.readValue(jsonData, javaType);
    }

    @SneakyThrows
    public static <K, V> Map<K, V> json2Map(String jsonData, Class<K> keyType, Class<V> valueType) {
        var javaType = mapper.getTypeFactory().constructMapType(Map.class, keyType, valueType);
        return mapper.readValue(jsonData, javaType);
    }


}