package ttyy.com.jinnetwork.core.http;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import ttyy.com.jinnetwork.core.config.HttpConfig;
import ttyy.com.jinnetwork.core.http.base.Client;
import ttyy.com.jinnetwork.core.http.stream.RequestStringEntity;
import ttyy.com.jinnetwork.core.work.HTTPRequest;
import ttyy.com.jinnetwork.core.work.HTTPResponse;
import ttyy.com.jinnetwork.core.work.inner.$Apache_CountingMultipartEntity;
import ttyy.com.jinnetwork.core.work.inner.$Converter;
import ttyy.com.jinnetwork.core.work.inner.$HttpResponse;
import ttyy.com.jinnetwork.core.work.method_post.PostContentType;

/**
 * author: admin
 * date: 2017/02/27
 * version: 0
 * mail: secret
 * desc: URLConnectionImpl
 */

public class URLConnectionImpl implements Client {

    long mConnectTimeOut;
    long mReadTimeOut;

    private URLConnectionImpl(HttpConfig config){
        if(config.isIgnoreCertificate()){
            /**
             * 忽略证书认证
             */
            try {
                // Create a trust manager that does not validate certificate chains
                TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs,
                                                   String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs,
                                                   String authType) {
                    }
                } };

                // Install the all-trusting trust manager
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mConnectTimeOut = config.getConnectTimeOut();
        mReadTimeOut = config.getReadTimeOut();
    }

    static class Holder{
        static URLConnectionImpl INSTANCE = new URLConnectionImpl(HttpConfig.get());
    }

    public static URLConnectionImpl getInstance(){

        return Holder.INSTANCE;
    }

    public static URLConnectionImpl create(HttpConfig config){

        return new URLConnectionImpl(config);
    }

    @Override
    public HTTPResponse post(HTTPRequest worker) {
        $HttpResponse rsp = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(worker.getRequestURL()).openConnection();
            initConn(conn, worker);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // 开始连接
            conn.connect();

            // post参数内容
            PostContentType mContentType = worker.getContentType();

            OutputStream os = conn.getOutputStream();

            // 表单填写借助HttpClient Entity
            if(mContentType == PostContentType.ApplicationJson){

                String jsonText = $Converter.toJson(worker).toString();
                RequestStringEntity requestEntity = new RequestStringEntity(jsonText, "utf-8");

                requestEntity.writeTo(os);

            }else if(mContentType == PostContentType.ApplicationForm){

                LinkedList<NameValuePair> params = new LinkedList<>();
                for (Map.Entry<String, Object> entry : worker.getParams().entrySet()) {
                    params.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
                }
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params);

                formEntity.writeTo(os);

            }else{

                $Apache_CountingMultipartEntity progressHttpEntity = new $Apache_CountingMultipartEntity(new $Apache_CountingMultipartEntity.ProgressListener() {
                    @Override
                    public void transferred(long transferedBytes) {
                        // 带进度
                    }
                });
                for (Map.Entry<String, Object> entry : worker.getParams().entrySet()) {
                    if (entry.getValue() instanceof File) {

                        progressHttpEntity.addPart(entry.getKey(), new FileBody((File) entry.getValue()));
                    } else {

                        progressHttpEntity.addPart(entry.getKey(), new StringBody(String.valueOf(entry.getValue())));
                    }
                }

                progressHttpEntity.writeTo(os);
            }

            rsp = new $HttpResponse(worker);

            // Http状态码
            rsp.setStatusCode(conn.getResponseCode());
            // 获取响应结果
            if (rsp.getStatusCode() == 200
                    || rsp.getStatusCode() == 206) {
                // 206 断点续传
                rsp.setContentLength(conn.getContentLength());
                rsp.readContentFromStream(conn.getInputStream());
            }

        } catch (IOException e) {

            rsp = new $HttpResponse(worker);
            rsp.setStatusCode(-1);
            rsp.setException(e);

            return rsp;
        }

        return rsp;
    }

    @Override
    public HTTPResponse get(HTTPRequest worker) {
        // 拼接URL
        String url = worker.getRequestURL() ;
        if(worker.getParams().size() > 0){
            String url_params = $Converter.toFormParams(worker);
            url += "?" + url_params;
        }

        $HttpResponse rsp = null;

        try {

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            initConn(conn, worker);
            conn.setDoInput(true);
            conn.setDoOutput(false);

            // 开始连接
            conn.connect();

            rsp = new $HttpResponse(worker);

            // Http状态码
            rsp.setStatusCode(conn.getResponseCode());
            // 获取响应结果
            if (rsp.getStatusCode() == 200
                    || rsp.getStatusCode() == 206) {
                // 206 断点续传
                rsp.setContentLength(conn.getContentLength());
                rsp.readContentFromStream(conn.getInputStream());
            }
        } catch (IOException e) {

            rsp = new $HttpResponse(worker);
            rsp.setStatusCode(-1);
            rsp.setException(e);

            return rsp;

        }

        return rsp;
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
