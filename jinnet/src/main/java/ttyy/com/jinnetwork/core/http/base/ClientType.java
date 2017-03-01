package ttyy.com.jinnetwork.core.http.base;

/**
 * author: admin
 * date: 2017/02/27
 * version: 0
 * mail: secret
 * desc: ClientType
 */

public enum ClientType {

    /**
     * Apache HttpClient
     */
    APACHE_CLIENT,
    /**
     * OKHttpClient
     */
    OKHTTP_CLIENT,
    /**
     * HttpURLConnection
     * HttpsURLConnection
     */
    URLCONNECTION_CLIENT;

}
