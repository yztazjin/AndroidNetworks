package com.small.tools.network.internal.client;

import com.small.tools.network.internal.SmallHTTPResponse;
import com.small.tools.network.internal.interfaces.StatusCode;
import com.small.tools.network.internal.interfaces.SmallHeader;
import com.small.tools.network.internal.interfaces.HTTPClient;
import com.small.tools.network.internal.interfaces.HTTPRequest;
import com.small.tools.network.internal.interfaces.HTTPResponse;
import com.small.tools.network.internal.io.SmallStringEntity;
import com.small.tools.network.internal.tools.SmallJsonTransfer;
import com.small.tools.network.internal.tools.URLGetterTransfer;
import com.small.tools.network.internal.SmallFileBody;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Author: hjq
 * Date  : 2018/10/03 14:21
 * Name  : ClientURLConnIml
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class ClientURLConnIml implements HTTPClient {

    long mConnectTimeOut;
    long mReadTimeOut;

    public ClientURLConnIml() {
        // 忽略证书认证
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs,
                                               String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs,
                                               String authType) {
                }
            }};

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mConnectTimeOut = TimeUnit.SECONDS.toMillis(15);
        mReadTimeOut = TimeUnit.SECONDS.toMillis(60);
    }

    @Override
    public HTTPResponse get(HTTPRequest smallRequest) {

        String url = URLGetterTransfer.transform(smallRequest);
        SmallHTTPResponse response = (SmallHTTPResponse) smallRequest.getResponse();

        try {

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            initConn(conn, smallRequest);
            conn.setDoInput(true);
            conn.setDoOutput(false);
            // 开始连接
            conn.connect();

            if (smallRequest.isCanceled()
                    || smallRequest.isFinished()) {

            } else {

                // Http状态码
                response.setStatusCode(conn.getResponseCode());
                // 获取响应结果
                if (response.getStatusCode() == 200
                        || response.getStatusCode() == 206) {
                    // 206 断点续传
                    response.setContentLength(conn.getContentLength());
                    smallRequest.getResourceDataParser().parse(smallRequest, conn.getInputStream());
                }
            }
            // 断开连接
            conn.disconnect();
        } catch (IOException e) {

            response.setStatusCode(StatusCode.PARSE_ERROR_IOEXCEPTION);

        }

        return response;
    }

    @Override
    public HTTPResponse post(HTTPRequest smallRequest) {
        SmallHTTPResponse response = (SmallHTTPResponse) smallRequest.getResponse();
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(
                    smallRequest.getRemoteResource().getResourceAddress()).openConnection();
            initConn(conn, smallRequest);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // 开始连接
            conn.connect();

            // post参数内容
            SmallHeader.ContentType mContentType = smallRequest.getHTTPContent();

            OutputStream os = conn.getOutputStream();

            // 表单填写借助HttpClient Entity
            if (mContentType == SmallHeader.ContentType.ApplicationJson) {

                String jsonText = SmallJsonTransfer.convert(smallRequest.getParams()).toString();
                SmallStringEntity requestEntity = new SmallStringEntity(jsonText, "utf-8");

                requestEntity.writeTo(os);

            } else if (mContentType == SmallHeader.ContentType.FormURLEncoded) {

                LinkedList<NameValuePair> params = new LinkedList<>();
                for (Map.Entry<String, Object> entry : smallRequest.getParams().entrySet()) {
                    params.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
                }
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params);

                formEntity.writeTo(os);

            } else if (mContentType == SmallHeader.ContentType.MultipartFormdata) {

                MultipartEntity multipartEntity = new MultipartEntity();
                for (Map.Entry<String, Object> entry : smallRequest.getParams().entrySet()) {
                    if (entry.getValue() instanceof SmallFileBody) {

                        SmallFileBody fileBody = (SmallFileBody) entry.getValue();
                        multipartEntity.addPart(entry.getKey(), new FileBody(fileBody.getFile(), fileBody.getFileNameDesc()));
                    } else {

                        multipartEntity.addPart(entry.getKey(), new StringBody(String.valueOf(entry.getValue())));
                    }
                }

                multipartEntity.writeTo(os);
            }

            // Status Check
            if (smallRequest.isCanceled()
                    || smallRequest.isFinished()) {

            } else {

                // Http状态码
                response.setStatusCode(conn.getResponseCode());
                // 获取响应结果
                if (response.getStatusCode() == 200
                        || response.getStatusCode() == 206) {
                    // 206 断点续传
                    response.setContentLength(conn.getContentLength());
                    smallRequest.getResourceDataParser().parse(smallRequest, conn.getInputStream());
                }
            }
            conn.disconnect();
        } catch (IOException e) {
            response.setStatusCode(StatusCode.PARSE_ERROR_IOEXCEPTION);
        }

        return response;
    }

    private HttpURLConnection initConn(HttpURLConnection conn, HTTPRequest worker) throws IOException {
        // 连接超时设置
        conn.setConnectTimeout((int) mConnectTimeOut);
        // 读取超时设置
        conn.setReadTimeout((int) mReadTimeOut);

        // Header
        for (Map.Entry<String, String> entry : worker.getHeaders().entrySet()) {
            conn.setRequestProperty(entry.getKey(), entry.getValue());
        }

        return conn;
    }
}
