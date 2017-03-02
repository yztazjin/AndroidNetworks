package ttyy.com.jinnetwork.core.http.stream;

import java.net.URLEncoder;
import java.util.Map;

/**
 * author: admin
 * date: 2017/03/02
 * version: 0
 * mail: secret
 * desc: RequestFormEntity
 */

public class RequestFormEntity extends RequestStringEntity{

    public RequestFormEntity(Map<String, Object> params){
        super(format(params));
    }

    public RequestFormEntity(Map<String, Object> params, String charset){
        super(format(params), charset);
    }

    private static String format(Map<String, Object> params){

        if(params == null
                || params.size() == 0){
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, Object> entry : params.entrySet()){
            String key = entry.getKey();
            sb.append(key)
                    .append("=")
                    .append(URLEncoder.encode(String.valueOf(entry.getValue())))
                    .append("&");
        }

        String formText = sb.substring(0, sb.length() - 1);
        return formText;
    }

}
