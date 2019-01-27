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
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

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
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Author: hjq
 * Date  : 2018/10/03 14:21
 * Name  : ClientApacheImpl
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class ClientApacheImpl implements HTTPClient {

    HttpClient mClient;

    public ClientApacheImpl() {
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

            mClient = new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            mClient = new DefaultHttpClient();
        }

        mClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                HttpVersion.HTTP_1_1);
        mClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
                TimeUnit.SECONDS.toMillis(15));//连接时间
        mClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                TimeUnit.SECONDS.toMillis(60));//数据传输时间

    }

    @Override
    public HTTPResponse get(HTTPRequest smallRequest) {

        String url = URLGetterTransfer.transform(smallRequest);
        HttpGet get = new HttpGet(url);
        for (Map.Entry<String, String> entry : smallRequest.getHeaders().entrySet()) {
            get.setHeader(entry.getKey(), entry.getValue());
        }

        return call(get, smallRequest);
    }

    @Override
    public HTTPResponse post(HTTPRequest smallRequest) {
        HttpPost post = new HttpPost(smallRequest.getRemoteResource().getResourceAddress());
        try {
            // post参数内容
            SmallHeader.ContentType mContentType = smallRequest.getHTTPContent();
            if (mContentType == SmallHeader.ContentType.ApplicationJson) {

                String jsonText = SmallJsonTransfer.convert(smallRequest.getParams()).toString();

                StringEntity requestEntity = new StringEntity(jsonText, "utf-8");
                post.setEntity(requestEntity);
            } else if (mContentType == SmallHeader.ContentType.FormURLEncoded) {

                LinkedList<NameValuePair> params = new LinkedList<>();
                for (Map.Entry<String, Object> entry : smallRequest.getParams().entrySet()) {
                    params.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
                }
                post.setEntity(new UrlEncodedFormEntity(params));
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
                post.setEntity(multipartEntity);
            }
        } catch (UnsupportedEncodingException e) {

            SmallHTTPResponse resp = (SmallHTTPResponse) smallRequest.getResponse();
            resp.setStatusCode(StatusCode.REQUEST_ERROR_UNSUPPORTENCODE);

            return resp;
        }

        // Header
        for (Map.Entry<String, String> entry : smallRequest.getHeaders().entrySet()) {
            post.addHeader(entry.getKey(), entry.getValue());
        }

        return call(post, smallRequest);
    }

    private HTTPResponse call(HttpUriRequest apacheRequest, HTTPRequest smallRequest) {
        // 开始发射，请求吧
        SmallHTTPResponse response = (SmallHTTPResponse) smallRequest.getResponse();

        try {
            // 同步执行
            HttpResponse apache_resp = mClient.execute(apacheRequest);

            // Status Check
            if (smallRequest.isCanceled()
                    || smallRequest.isFinished()) {

            } else {

                // 设置HttpCode
                response.setStatusCode(apache_resp.getStatusLine().getStatusCode());

                // 设置Header
                for (Header header : apache_resp.getAllHeaders()) {
                    response.setHeader(header.getName(), header.getValue());
                }

                // 获取响应结果
                if (response.getStatusCode() == 200
                        || response.getStatusCode() == 206) {
                    // 206 断点续传
                    HttpEntity entity = apache_resp.getEntity();

                    response.setContentLength(entity.getContentLength());
                    smallRequest.getResourceDataParser().parse(smallRequest, entity.getContent());
                }
            }

            // 流关闭逻辑在Parser
        } catch (IOException e) {
            response.setStatusCode(StatusCode.PARSE_ERROR_IOEXCEPTION);
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

            sslContext.init(null, new TrustManager[]{tm}, null);
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
