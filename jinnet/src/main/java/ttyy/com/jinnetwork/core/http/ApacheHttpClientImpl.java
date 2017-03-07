package ttyy.com.jinnetwork.core.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import ttyy.com.jinnetwork.core.config.HTTPConfig;
import ttyy.com.jinnetwork.core.http.base.Client;
import ttyy.com.jinnetwork.core.work.HTTPResponse;
import ttyy.com.jinnetwork.core.work.HTTPRequest;
import ttyy.com.jinnetwork.core.work.inner.$Apache_CountingMultipartEntity;
import ttyy.com.jinnetwork.core.work.inner.$Converter;
import ttyy.com.jinnetwork.core.work.inner.$HttpResponse;
import ttyy.com.jinnetwork.core.work.method_post.PostContentType;
import ttyy.com.jinnetwork.core.work.method_post.RequestFileBody;

/**
 * author: admin
 * date: 2017/02/27
 * version: 0
 * mail: secret
 * desc: ApacheHttpClientImpl
 */

public class ApacheHttpClientImpl implements Client {

    HttpClient mHttpClient;

    private ApacheHttpClientImpl(HTTPConfig config) {
        if(config.isIgnoreCertificate()){
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);

                SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                HttpParams params = new BasicHttpParams();
                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
                HttpConnectionParams.setConnectionTimeout(params, 10000);
                HttpConnectionParams.setSoTimeout(params, 10000);

                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                registry.register(new Scheme("https", sf, 443));

                ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

                mHttpClient = new DefaultHttpClient(ccm, params);
            } catch (Exception e) {
                mHttpClient = new DefaultHttpClient();
            }
        }else {
            mHttpClient = HttpClients.createDefault();
        }

        mHttpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        mHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,(int)config.getConnectTimeOut());//连接时间
        mHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,(int)config.getReadTimeOut());//数据传输时间
    }

    static class Holder {
        static ApacheHttpClientImpl INSTANCE = new ApacheHttpClientImpl(HTTPConfig.get());
    }

    public static ApacheHttpClientImpl getInstance() {
        return Holder.INSTANCE;
    }

    public static ApacheHttpClientImpl create(HTTPConfig config) {
        return new ApacheHttpClientImpl(config);
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
                    if (entry.getValue() instanceof RequestFileBody) {

                        RequestFileBody fileBody = (RequestFileBody) entry.getValue();
                        progressHttpEntity.addPart(entry.getKey(), new FileBody(fileBody.getFile(), fileBody.getFileNameDesc()));
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

    private static class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore)
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
}
