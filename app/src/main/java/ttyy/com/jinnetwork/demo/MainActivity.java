package ttyy.com.jinnetwork.demo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

import ttyy.com.jinnetwork.Https;
import ttyy.com.jinnetwork.core.callback.HttpUIThreadCallbackAdapter;
import ttyy.com.jinnetwork.core.work.HTTPResponse;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testDownload();
    }

    /**
     * 测试下载
     */
    void testDownload(){
        File file = new File(Environment.getExternalStorageDirectory(), "jin_download");
        String url = "http://p.gdown.baidu.com/6c0ca4285c9794d112e2a600dc8d8cd7db4d80b16e394bf42beca8141b0661d514479117b88de9964a0667180e8c590b13809f8c37646b6bbc31b861c293ee68fd81c507c8d04dcd382eaf16e69266ee8128413baacbd977ec9722f42e241d3625d0c296a5531300b865ddaed90c5daa07a49d27cd51dceb2bda23508cd6aed0987a5ee890e1aa14";
        Https.get(url)
                .setDownloadMode(file)
                .setHttpCallback(new HttpUIThreadCallbackAdapter(){
                    @Override
                    public void onProgress(HTTPResponse request, long cur, long total) {
                        super.onProgress(request, cur, total);
                        float percent = (float)cur / total;
                        Log.d("Https", "onProgress cur "+cur+" total "+total+" percent "+percent);
                    }

                    @Override
                    public void onSuccess(HTTPResponse response) {
                        super.onSuccess(response);
                        Log.d("Https", "onSuccess");
                    }
                })
                .build()
                .requestAsync();

    }
}
