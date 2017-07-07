package ttyy.com.jinnetwork.demo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

import ttyy.com.jinnetwork.Https;
import ttyy.com.jinnetwork.Images;
import ttyy.com.jinnetwork.core.callback.HTTPCallback;
import ttyy.com.jinnetwork.core.callback.HTTPUIThreadCallbackAdapter;
import ttyy.com.jinnetwork.core.work.HTTPRequest;
import ttyy.com.jinnetwork.core.work.HTTPResponse;


public class MainActivity extends AppCompatActivity {

    ListView lv_images;
    GankIOAdapter adapter;

    ArrayList<GankIOBean.Data> datas;

    int maxIndex = 3;
    int currIndex = 1;

    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_images = (ListView) findViewById(R.id.lv_images);
        adapter = new GankIOAdapter();
        lv_images.setAdapter(adapter);

        datas = new ArrayList<>();

        String path = "file://"+Environment.getExternalStorageDirectory().getAbsolutePath()+"/test_bg.jpg";
        ImageView local_iv = (ImageView) findViewById(R.id.local_iv);
        Images.get().source(path)
                .placeholder(R.drawable.shape_pre)
                .error(R.drawable.shape_err)
                .into(local_iv);

        requestGankIODatas();
//
//
//        testDownload();

//        Https.createService(TestAPIProxy.class, TestAPI.class)
//                .test(12, "params", new HTTPCallback() {
//                    @Override
//                    public void onPreStart(HTTPRequest request) {
//                        Log.e("Test", "onPreStart param0 -> "+request.getParams().get("param0"));
//                        Log.e("Test", "onPreStart param1 -> "+request.getParams().get("param1"));
//                    }
//
//                    @Override
//                    public void onProgress(HTTPResponse response, long cur, long total) {
//                        Log.e("Test", "onProgress cur -> "+cur+" total -> "+total);
//                    }
//
//                    @Override
//                    public void onSuccess(HTTPResponse response) {
//                        Log.e("Test", "onSuccess");
//                    }
//
//                    @Override
//                    public void onCancel(HTTPRequest response) {
//                        Log.e("Test", "onCancel");
//                    }
//
//                    @Override
//                    public void onFailure(HTTPResponse response) {
//                        Log.e("Test", "onFailure -> "+response.getErrorMessage());
//                    }
//
//                    @Override
//                    public void onFinish(HTTPResponse response) {
//                        Log.e("Test", "onFinish isSuccess -> "+response.isStatusCodeSuccessful());
//                    }
//                });
    }

    void requestGankIODatas(){
        if(currIndex > maxIndex){
            adapter.datas = datas;
            adapter.notifyDataSetChanged();
        }else {
            String url = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/{page}";
            Https.get(url)
                    .addPathParam("page", currIndex+"")
                    .setHttpCallback(new HTTPUIThreadCallbackAdapter(){
                        @Override
                        public void onPreStart(HTTPRequest request) {
                            super.onPreStart(request);
                            Log.d("Https", "url "+request.getRequestURL());
                        }

                        @Override
                        public void onSuccess(HTTPResponse response) {
                            super.onSuccess(response);
                            GankIOBean bean = gson.fromJson(response.getConentToString(), GankIOBean.class);
                            if(!bean.error){
                                currIndex++;
                                datas.addAll(bean.results);
                            }
                            requestGankIODatas();
                        }
                    })
                    .build().requestAsync();
        }
    }

    /**
     * 测试下载
     */
    void testDownload(){
        File file = new File(Environment.getExternalStorageDirectory(), "jin_download");
        String url = "http://p.gdown.baidu.com/6c0ca4285c9794d112e2a600dc8d8cd7db4d80b16e394bf42beca8141b0661d514479117b88de9964a0667180e8c590b13809f8c37646b6bbc31b861c293ee68fd81c507c8d04dcd382eaf16e69266ee8128413baacbd977ec9722f42e241d3625d0c296a5531300b865ddaed90c5daa07a49d27cd51dceb2bda23508cd6aed0987a5ee890e1aa14";
        Https.get(url)
                .setDownloadMode(file)
                .setHttpCallback(new HTTPUIThreadCallbackAdapter(){
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
