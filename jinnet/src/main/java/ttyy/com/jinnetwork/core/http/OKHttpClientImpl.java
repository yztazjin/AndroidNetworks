package ttyy.com.jinnetwork.core.http;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ttyy.com.jinnetwork.core.config.HTTPConfig;
import ttyy.com.jinnetwork.core.http.base.Client;
import ttyy.com.jinnetwork.core.work.HTTPRequest;
import ttyy.com.jinnetwork.core.work.HTTPResponse;
import ttyy.com.jinnetwork.core.work.inner.$Converter;
import ttyy.com.jinnetwork.core.work.inner.$HttpResponse;
import ttyy.com.jinnetwork.core.work.method_post.RequestFileBody;
import ttyy.com.jinnetwork.core.work.method_post.PostContentType;

/**
 * author: admin
 * date: 2017/02/27
 * version: 0
 * mail: secret
 * desc: OKHttpClientImpl
 */

public class OKHttpClientImpl implements Client {

    OkHttpClient mOKHttpClient;

    private OKHttpClientImpl(HTTPConfig config) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(new CookieJar() {
            private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url, cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url);
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        })
                .connectTimeout(config.getConnectTimeOut(), TimeUnit.MILLISECONDS)
                // 读 超时设置
                .readTimeout(config.getReadTimeOut(), TimeUnit.MILLISECONDS)
                // 写 超时设置
                .writeTimeout(3, TimeUnit.MINUTES);

        if (config.isIgnoreCertificate()) {
            setCertificateValidationIgnored(builder);
        }

        mOKHttpClient = builder.build();
    }

    static class Holder {
        static OKHttpClientImpl INSTANCE = new OKHttpClientImpl(HTTPConfig.getInstance());
    }

    public static OKHttpClientImpl getInstance() {

        return Holder.INSTANCE;
    }

    public static OKHttpClientImpl create(HTTPConfig config) {

        return new OKHttpClientImpl(config);
    }

    @Override
    public HTTPResponse post(HTTPRequest worker) {
        Request.Builder requestBuilder = new Request.Builder()
                                            .url(worker.getRequestURL());

        // post参数内容
        PostContentType mContentType = worker.getContentType();
        if(mContentType == PostContentType.ApplicationJson){

            String jsonText = $Converter.toJson(worker);
            RequestBody requestBody = RequestBody.create(MediaType.parse(mContentType.value()), jsonText);
            requestBuilder.post(requestBody);
        }else if(mContentType == PostContentType.FormURLEncoded){

            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, Object> entry : worker.getParams().entrySet()) {
                builder.add(entry.getKey(), String.valueOf(entry.getValue()));
            }
            requestBuilder.post(builder.build());
        }else{

            MultipartBody.Builder builder = new MultipartBody.Builder();
            for (Map.Entry<String, Object> entry : worker.getParams().entrySet()) {
                if(entry.getValue() instanceof RequestFileBody){

                    RequestFileBody fileBody = (RequestFileBody) entry.getValue();
                    StringBuilder sb = new StringBuilder("form-data; name=\"");
                    sb.append(entry.getKey()).append("\";filename=\"");
                    sb.append(fileBody.getFileNameDesc()).append("\"");
                    builder.addPart(Headers.of("Content-Disposition", sb.toString()),
                            RequestBody.create(MediaType.parse("application/octet-stream"),fileBody.getFile()));
                }else {

                    StringBuilder sb = new StringBuilder("form-data; name=\"");
                    sb.append(entry.getKey()).append("\"");
                    builder.addPart(Headers.of("Content-Disposition", sb.toString()),
                            RequestBody.create(null ,String.valueOf(entry.getValue())));
                }
            }
            requestBuilder.post(builder.build());
        }

        // Header
        for (Map.Entry<String, String> entry : worker.getHeaders().entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        Request request = requestBuilder.build();

        return call(request, worker);
    }

    @Override
    public HTTPResponse get(HTTPRequest worker) {

        // 拼接URL
        String url = worker.getRequestURL() ;
        if(worker.getParams().size() > 0){
            String url_params = $Converter.toFormParams(worker);
            url += "?" + url_params;
        }

        // 加入头部信息
        Request.Builder requestBuilder = new Request.Builder()
                .url(url);
        for (Map.Entry<String, String> entry : worker.getHeaders().entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        Request request = requestBuilder.build();

        return call(request, worker);
    }

    @Override
    public HTTPResponse special(HTTPRequest request) {
        // 默认提供的OKHttpClient 不支持 用户自定义操作
        $HttpResponse err = new $HttpResponse(request);
        err.setStatusCode(-1);
        err.setErrorMessage("默认提供的OKHttpClient不支持用户自定义HTTP请求方式");
        return err;
    }

    private HTTPResponse call(Request request, HTTPRequest worker){
        // 开始发射，请求吧
        $HttpResponse response = new $HttpResponse(worker);
        try {
            // 同步执行
            Response okhttp_resp = mOKHttpClient.newCall(request).execute();

            // 设置HttpCode
            response.setStatusCode(okhttp_resp.code());

            // 设置Header
            Headers headers = okhttp_resp.headers();
            for(int i = 0 ; i < headers.size(); i++){
                response.addHeader(headers.name(i), headers.value(i));
            }

            // 获取响应结果
            if(response.getStatusCode() == 200
                    || response.getStatusCode() == 206){
                // 206 断点续传
                response.setContentLength(okhttp_resp.body().contentLength());
                response.readContentFromStream(okhttp_resp.body().byteStream());
            }
            // 关闭连接
            okhttp_resp.close();
        } catch (IOException e) {
            response.setException(e);
            response.setStatusCode(-1);
        }

        return response;
    }

    /**
     * 生成吧 一个支持https/http请求的okhttp客户端
     *
     * @return
     */
    private void setCertificateValidationIgnored(OkHttpClient.Builder builder) {
        // 忽略证书验证
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                // 返回null okhttp会出现NullPointer异常
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        };
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");

            sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        // Host不验证
        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Cookie自动化管理

        // https忽略证书设置
        builder.sslSocketFactory(sslContext.getSocketFactory())
                // https忽略证书设置
                .hostnameVerifier(DO_NOT_VERIFY);
    }

}
