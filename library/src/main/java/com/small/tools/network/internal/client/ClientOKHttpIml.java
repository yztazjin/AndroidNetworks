package com.small.tools.network.internal.client;

import com.small.tools.network.internal.SmallHTTPResponse;
import com.small.tools.network.internal.interfaces.StatusCode;
import com.small.tools.network.internal.interfaces.SmallHeader;
import com.small.tools.network.internal.interfaces.HTTPClient;
import com.small.tools.network.internal.interfaces.HTTPRequest;
import com.small.tools.network.internal.interfaces.HTTPResponse;
import com.small.tools.network.internal.tools.SmallJsonTransfer;
import com.small.tools.network.internal.tools.URLGetterTransfer;
import com.small.tools.network.internal.SmallFileBody;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
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
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Author: hjq
 * Date  : 2018/10/03 14:20
 * Name  : ClientOKHttpIml
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class ClientOKHttpIml implements HTTPClient {

    OkHttpClient mClient;

    public ClientOKHttpIml() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.protocols(Collections.singletonList(Protocol.HTTP_1_1));
        // Cookie自动化管理
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
                .connectTimeout(15, TimeUnit.SECONDS)
                // 读 超时设置
                .readTimeout(60, TimeUnit.SECONDS)
                // 写 超时设置
                .writeTimeout(60, TimeUnit.SECONDS);

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

        // https忽略证书设置
        builder.sslSocketFactory(sslContext.getSocketFactory())
                // https忽略证书设置
                .hostnameVerifier(DO_NOT_VERIFY);

        mClient = builder.build();
    }

    @Override
    public HTTPResponse get(HTTPRequest smallRequest) {
        String url = URLGetterTransfer.transform(smallRequest);
        // 加入头部信息
        Request.Builder requestBuilder = new Request.Builder()
                .url(url);
        for (Map.Entry<String, String> entry : smallRequest.getHeaders().entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }

        Request okRequest = requestBuilder.build();

        return call(okRequest, smallRequest);
    }

    @Override
    public HTTPResponse post(HTTPRequest smallRequest) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(smallRequest.getRemoteResource().getResourceAddress());

        // post参数内容
        SmallHeader.ContentType mContentType = smallRequest.getHTTPContent();
        if (mContentType == SmallHeader.ContentType.ApplicationJson) {

            String jsonText = SmallJsonTransfer.convert(smallRequest.getParams()).toString();
            RequestBody requestBody = RequestBody.create(MediaType.parse(mContentType.getValue()), jsonText);
            requestBuilder.post(requestBody);
        } else if (mContentType == SmallHeader.ContentType.FormURLEncoded) {

            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, Object> entry : smallRequest.getParams().entrySet()) {
                builder.add(entry.getKey(), String.valueOf(entry.getValue()));
            }
            requestBuilder.post(builder.build());
        } else if (mContentType == SmallHeader.ContentType.MultipartFormdata) {

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            for (Map.Entry<String, Object> entry : smallRequest.getParams().entrySet()) {
                if (entry.getValue() instanceof SmallFileBody) {

                    SmallFileBody fileBody = (SmallFileBody) entry.getValue();
                    StringBuilder sb = new StringBuilder("form-data; name=\"");
                    sb.append(entry.getKey()).append("\";filename=\"");
                    sb.append(fileBody.getFileNameDesc()).append("\"");
                    builder.addPart(Headers.of("Content-Disposition", sb.toString()),
                            RequestBody.create(MediaType.parse("application/octet-stream"), fileBody.getFile()));
                } else {

                    StringBuilder sb = new StringBuilder("form-data; name=\"");
                    sb.append(entry.getKey()).append("\"");
                    builder.addPart(Headers.of("Content-Disposition", sb.toString()),
                            RequestBody.create(null, String.valueOf(entry.getValue())));
                }
            }
            requestBuilder.post(builder.build());
        }

        // Header
        for (Map.Entry<String, String> entry : smallRequest.getHeaders().entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        Request request = requestBuilder.build();

        return call(request, smallRequest);
    }


    private HTTPResponse call(Request okRequest, HTTPRequest smallRequest) {
        SmallHTTPResponse response = (SmallHTTPResponse) smallRequest.getResponse();

        try {
            // 同步执行
            Response okhttp_resp = mClient.newCall(okRequest).execute();

            // Status Check
            if (smallRequest.isCanceled()
                    || smallRequest.isFinished()) {

            } else {

                // 设置HttpCode
                response.setStatusCode(okhttp_resp.code());

                // 设置Header
                Headers headers = okhttp_resp.headers();
                for (int i = 0; i < headers.size(); i++) {
                    response.setHeader(headers.name(i), headers.value(i));
                }

                // 获取响应结果
                if (response.getStatusCode() == 200
                        || response.getStatusCode() == 206) {
                    // 206 断点续传
                    response.setContentLength(okhttp_resp.body().contentLength());
                    smallRequest.getResourceDataParser().parse(smallRequest, okhttp_resp.body().byteStream());
                }
            }
            // 关闭连接
            okhttp_resp.close();
        } catch (IOException e) {
            response.setStatusCode(StatusCode.PARSE_ERROR_IOEXCEPTION);
        }

        return response;
    }

}
