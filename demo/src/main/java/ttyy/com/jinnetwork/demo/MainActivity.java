package ttyy.com.jinnetwork.demo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.small.tools.network.Https;
import com.small.tools.network.global.Configs;
import com.small.tools.network.internal.HTTPJsonCallback;
import com.small.tools.network.internal.cache.CacheAction;
import com.small.tools.network.internal.interfaces.HTTPCallback;
import com.small.tools.network.internal.interfaces.HTTPRequest;
import com.small.tools.network.internal.ResourceParserFile;
import com.small.tools.network.internal.ResourceParserString;
import com.small.tools.network.support.Images;
import com.small.tools.network.support.images.ImagesActionImageView;
import com.small.tools.network.support.images.SchedulerImages;

import java.io.File;
import java.util.ArrayList;

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

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test_bg.jpg";
        ImageView local_iv = (ImageView) findViewById(R.id.local_iv);

        String url0 = "http://photocdn.sohu.com/20150917/Img421382005.jpg";
        String url1 = "http://ww1.sinaimg.cn/large/0065oQSqly1fswhaqvnobj30sg14hka0.jpg";

        Configs.getSingleton().images().setCacheAction(CacheAction.None);
        Configs.getSingleton().getCacheManager()
                .setDiskCacheDirectory(getExternalFilesDir("SmallCache"));

        Images.get().loadFromNetwork(url0)
                .placeholder(getResources().getDrawable(R.drawable.shape_pre))
                .fail(getResources().getDrawable(R.drawable.shape_err))
                .into(new ImagesActionImageView(local_iv));

//        Images.get().loadFromDrawable(this, R.drawable.shape_err)
//                .placeholder(getResources().getDrawable(R.drawable.shape_pre))
//                .fail(getResources().getDrawable(R.drawable.shape_err))
//                .into(new ImagesActionView(local_iv));

        Log.e("Https", "path " + getExternalFilesDir("SmallCaches").getPath());
        requestGankIODatas();
//
//
//        testDownload();

//        Https.createService(TestAPIProxy.class, TestAPI.class)
//                .test(12, "params", new HTTPCallback() {
//                    @Override
//                    public void onPreStart(HTTPRequest request) {
//                        Log.e("Test", "onPreStart param0 -> "+request.getParams().getInstance("param0"));
//                        Log.e("Test", "onPreStart param1 -> "+request.getParams().getInstance("param1"));
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

    void requestGankIODatas() {
        if (currIndex > maxIndex) {
            Configs.getSingleton().setScheduler(new SchedulerImages());

            lv_images.post(new Runnable() {
                @Override
                public void run() {
                    adapter.datas = datas;
                    adapter.notifyDataSetChanged();
                }
            });

        } else {
            String url = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/" + currIndex;
            Https.get(url)
                    .setResourceDataParser(new ResourceParserString())
                    .setHTTPCallback(new HTTPJsonCallback<GankIOBean>(){
                        @Override
                        public void onSuccess(HTTPRequest request, GankIOBean data) {
                            super.onSuccess(request, data);
                            if (!data.error) {
                                currIndex++;
                                datas.addAll(data.results);
                            }
                            requestGankIODatas();
                        }
                    })
                    .requestAsync();
        }
    }

    /**
     * 测试下载
     */
    void testDownload() {
        File file = new File(Environment.getExternalStorageDirectory(), "jin_download");
        String url = "http://p.gdown.baidu.com/6c0ca4285c9794d112e2a600dc8d8cd7db4d80b16e394bf42beca8141b0661d514479117b88de9964a0667180e8c590b13809f8c37646b6bbc31b861c293ee68fd81c507c8d04dcd382eaf16e69266ee8128413baacbd977ec9722f42e241d3625d0c296a5531300b865ddaed90c5daa07a49d27cd51dceb2bda23508cd6aed0987a5ee890e1aa14";
        Https.get(url)
                .setResourceDataParser(new ResourceParserFile().setReceivedFile(file))
                .setHTTPCallback(new HTTPCallback() {
                    @Override
                    public void onStart(HTTPRequest request) {
                        Log.d("Https", "onStart " + request.getRemoteResource().getResourceAddress());
                    }

                    @Override
                    public void onProgress(HTTPRequest request, long cur, long total) {
                        float percent = (float) cur / total;
                        Log.d("Https", "onProgress " + request.getRemoteResource().getResourceAddress()
                                + "\n" + "onProgress cur " + cur + " total " + total + " percent " + percent);
                    }

                    @Override
                    public void onSuccess(HTTPRequest request) {
                        Log.d("Https", "onSuccess " + request.getRemoteResource().getResourceAddress() + request.getResourceDataParser().getData());
                    }

                    @Override
                    public void onFailure(HTTPRequest request) {
                        Log.d("Https", "onFailure " + request.getRemoteResource().getResourceAddress());
                    }

                    @Override
                    public void onCancel(HTTPRequest request, boolean isUserCanceled) {
                        Log.d("Https", "onCancel " + request.getRemoteResource().getResourceAddress());

                    }

                    @Override
                    public void onFinish(HTTPRequest request) {
                        Log.d("Https", "onFinish " + request.getRemoteResource().getResourceAddress());

                    }
                })
                .requestAsync();

    }

}
