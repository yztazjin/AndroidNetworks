package ttyy.com.jinnetwork.demo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import ttyy.com.jinnetwork.CountingMultipartEntity;

public class MainActivity extends AppCompatActivity {

    long totalSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
//                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//                builder.setCharset(Charset.forName(HTTP.UTF_8));//设置请求的编码格式
//                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.mp4";
//
//
//                HttpEntity entity = builder.build();// 生成 HTTP POST 实体

                CountingMultipartEntity progressHttpEntity = new CountingMultipartEntity(new CountingMultipartEntity.ProgressListener() {
                    @Override
                    public void transferred(long transferedBytes) {

                        float percent = (float) transferedBytes / totalSize;

                        Log.d("Hjq", "progress " + percent);
                    }
                });
                File file = new File(path);
                progressHttpEntity.addPart("Filedata", new FileBody(file));

                totalSize = progressHttpEntity.getContentLength();//获取上传文件的大小


                HttpClient httpClient = new DefaultHttpClient();// 开启一个客户端 HTTP 请求
                httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);// 设置连接超时时间
                HttpPost httpPost = new HttpPost("http://apitest.peipeitech.com/index.php/file/upload");//创建 HTTP POST 请求
                httpPost.setEntity(progressHttpEntity);
                try {
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    Log.d("Hjq", "status_code -> " + httpResponse.getStatusLine().getStatusCode());
                    if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                        InputStream is = httpResponse.getEntity().getContent();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        String line = "";
                        StringBuilder sb = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }

                        Log.d("Hjq", "response -> " + sb.toString());


                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (ConnectTimeoutException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (httpClient != null && httpClient.getConnectionManager() != null) {
                        httpClient.getConnectionManager().shutdown();
                    }
                }

            }
        });

    }
}
