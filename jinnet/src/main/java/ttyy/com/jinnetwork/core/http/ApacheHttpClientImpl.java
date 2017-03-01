package ttyy.com.jinnetwork.core.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Map;

import ttyy.com.jinnetwork.core.config.HttpConfig;
import ttyy.com.jinnetwork.core.http.base.Client;
import ttyy.com.jinnetwork.core.work.HTTPResponse;
import ttyy.com.jinnetwork.core.work.HTTPRequest;
import ttyy.com.jinnetwork.core.work.inner.$Apache_CountingMultipartEntity;
import ttyy.com.jinnetwork.core.work.inner.$Converter;
import ttyy.com.jinnetwork.core.work.inner.$HttpResponse;
import ttyy.com.jinnetwork.core.work.method_post.PostContentType;

/**
 * author: admin
 * date: 2017/02/27
 * version: 0
 * mail: secret
 * desc: ApacheHttpClientImpl
 */

public class ApacheHttpClientImpl implements Client {

    HttpClient mHttpClient;
    RequestConfig mApacheConfig;

    private ApacheHttpClientImpl(HttpConfig config) {
        mHttpClient = new DefaultHttpClient();

        mHttpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

        mApacheConfig = RequestConfig.custom()
                .setAuthenticationEnabled(config.isIgnoreCertificate())
                .setConnectTimeout((int) config.getConnectTimeOut())
                .setSocketTimeout((int) config.getReadTimeOut())
                .build();
    }

    static class Holder {
        static ApacheHttpClientImpl INSTANCE = new ApacheHttpClientImpl(HttpConfig.get());
    }

    public static ApacheHttpClientImpl getInstance() {
        return Holder.INSTANCE;
    }

    public static ApacheHttpClientImpl create() {
        return null;
    }

    @Override
    public HTTPResponse post(HTTPRequest worker) {
        HttpPost post = new HttpPost(worker.getRequestURL());
        try {
            // post参数内容
            PostContentType mContentType = worker.getContentType();
            if (mContentType == PostContentType.ApplicationJson) {

                String jsonText = $Converter.toJson(worker).toString();

                StringEntity requestEntity = new StringEntity(jsonText, "utf-8");
                post.setEntity(requestEntity);

            } else if (mContentType == PostContentType.ApplicationForm) {

                LinkedList<NameValuePair> params = new LinkedList<>();
                for (Map.Entry<String, Object> entry : worker.getParams().entrySet()) {
                    params.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
                }
                post.setEntity(new UrlEncodedFormEntity(params));
            } else {

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
                post.setEntity(progressHttpEntity);
            }
        } catch (UnsupportedEncodingException e) {

            $HttpResponse resp = new $HttpResponse(worker);
            resp.setStatusCode(-1);
            resp.setException(e);

            return resp;
        }

        // Header
        for (Map.Entry<String, String> entry : worker.getHeaders().entrySet()) {
            post.addHeader(entry.getKey(), entry.getValue());
        }

        return call(post, worker);
    }

    @Override
    public HTTPResponse get(HTTPRequest worker) {
        // 拼接URL
        String url = worker.getRequestURL();
        if (worker.getParams().size() > 0) {
            String url_params = $Converter.toFormParams(worker);
            url += "?" + url_params;
        }

        HttpGet get = new HttpGet(url);
        get.setConfig(mApacheConfig);
        for (Map.Entry<String, String> entry : worker.getHeaders().entrySet()) {
            get.setHeader(entry.getKey(), entry.getValue());
        }

        return call(get, worker);
    }

    private HTTPResponse call(HttpUriRequest request, HTTPRequest worker) {
        // 开始发射，请求吧
        $HttpResponse response = new $HttpResponse(worker);
        try {
            // 同步执行
            HttpResponse apache_resp = mHttpClient.execute(request);

            // 设置HttpCode
            response.setStatusCode(apache_resp.getStatusLine().getStatusCode());

            // 设置Header
            for (Header header : apache_resp.getAllHeaders()) {
                response.addHeader(header.getName(), header.getValue());
            }

            // 获取响应结果
            if (response.getStatusCode() == 200
                    || response.getStatusCode() == 206) {
                // 206 断点续传
                HttpEntity entity = apache_resp.getEntity();

                response.setContentLength(entity.getContentLength());
                response.readContentFromStream(entity.getContent());
            }
        } catch (IOException e) {
            response.setException(e);
            response.setStatusCode(-1);
        }

        return response;
    }
}
