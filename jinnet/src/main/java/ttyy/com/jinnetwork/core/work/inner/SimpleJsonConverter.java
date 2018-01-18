package ttyy.com.jinnetwork.core.work.inner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * author: admin
 * date: 2018/01/18
 * version: 0
 * mail: secret
 * desc: JSONConverter
 */

class SimpleJsonConverter {

    static Object convert(Object value) {

        if (isList(value)) {
            JSONArray array = new JSONArray();
            Iterable iterable = (Iterable) value;
            for (Object tmp : iterable) {
                array.put(convert(tmp));
            }

            return array;
        } else if (isDict(value)) {

            JSONObject json = new JSONObject();
            Map<String, Object> map = (Map<String, Object>) value;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                try {
                    json.put(entry.getKey(), convert(entry.getValue()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return json;
        } else if (isBaseType(value)) {

            return value;
        } else {

            return convert(convertToMap(value));
        }

    }

    private static boolean isList(Object value) {
        return value instanceof Iterable;
    }

    private static boolean isDict(Object value) {
        return value instanceof Map;
    }

    private static boolean isBaseType(Object value) {
        Class valueClass = value.getClass();
        return valueClass.equals(int.class)
                || valueClass.equals(Integer.class)
                || valueClass.equals(float.class)
                || valueClass.equals(Float.class)
                || valueClass.equals(boolean.class)
                || valueClass.equals(Boolean.class)
                || valueClass.equals(long.class)
                || valueClass.equals(Long.class)
                || valueClass.equals(double.class)
                || valueClass.equals(Double.class)
                || valueClass.equals(short.class)
                || valueClass.equals(Short.class)
                || valueClass.equals(char.class)
                || valueClass.equals(Character.class)
                || valueClass.equals(URL.class)
                || valueClass.equals(URI.class)
                || valueClass.equals(String.class);
    }

    private static Map<String, Object> convertToMap(Object src) {
        Class clazz = src.getClass();

        Map<String, Object> map = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            try {
                map.put(field.getName(), field.get(src));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        return map;
    }
}
