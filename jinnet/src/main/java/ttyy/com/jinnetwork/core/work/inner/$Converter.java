package ttyy.com.jinnetwork.core.work.inner;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ttyy.com.jinnetwork.core.config.__Log;
import ttyy.com.jinnetwork.core.work.HTTPRequest;

/**
 * author: admin
 * date: 2017/02/28
 * version: 0
 * mail: secret
 * desc: $$Converter
 */

public final class $Converter {

    private $Converter() {

    }

    /**
     * 转换为Json参数
     *
     * @param request
     * @return
     */
    public static String toJson(HTTPRequest request) {
        Object object = SimpleJsonConverter.convert(request.getParams());
        return object == null ? null : object.toString();
    }

    /**
     * 转化为Get URL拼接参数 / Post application/x-www-form-urlencoded形式的参数
     *
     * @param request
     * @return
     */
    public static String toFormParams(HTTPRequest request) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : request.getParams().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value != null) {
                sb.append(key)
                        .append("=")
                        .append(URLEncoder.encode(value.toString()))
                        .append("&");
            }
        }
        String url_params = sb.substring(0, sb.length() - 1);
        return url_params;
    }

    /**
     * multi-part形式的参数
     *
     * @param request
     * @return
     */
    public static byte[] toMultipartFormParams(HTTPRequest request) {

        return null;
    }

    private static Pattern UrlPattern = Pattern.compile("([\u4e00-\u9fa5]+)");

    // 资源地址转码 针对中文
    public static String EncodeURL(String url) {
        String realUrl = url;
        Matcher m = UrlPattern.matcher(url);
        while (m.find()) {
            String target = m.group();
            String encodeTarget = URLEncoder.encode(target);
            realUrl = realUrl.replaceAll(target, encodeTarget);
        }
        return realUrl;
    }

}
