package ttyy.com.jinnetwork.ext_imgloader;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import ttyy.com.jinnetwork.core.callback.HttpUIThreadCallbackAdapter;
import ttyy.com.jinnetwork.core.work.HTTPResponse;
import ttyy.com.jinnetwork.ext_imgloader.luban_compress.LBC;

/**
 * Author: hjq
 * Date  : 2017/03/01 20:23
 * Name  : TargetWrapper
 * Intro : Edit By hjq
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2017/03/01    hjq   1.0              1.0
 */
public class TargetWrapper extends HttpUIThreadCallbackAdapter{

    View mTargetView;

    public TargetWrapper(View view){
        this.mTargetView = view;
    }

    public View getTargetView(){
        return mTargetView;
    }

    @Override
    public void onSuccess(HTTPResponse response) {
        super.onSuccess(response);

        if(mTargetView == null){
            return;
        }

        String url = response.getHttpRequest().getRequestURL();
        if(mTargetView.getTag().equals(url.hashCode())){
            // 图片文件
            File image_file = response.getHttpRequest().getDownloadFile();
            Bitmap bm = null;
            ImageCache.get().setToRuntimCache(url.hashCode(), bm);

            if(LoaderConfig.get().getCacheDir() != null){
                if(!ImageCache.get().isThumbFile(image_file)){
                    File thumbDir = new File(LoaderConfig.get().getCacheDir(), "thumb");
                    File thumbFile = new File(thumbDir, String.valueOf(url.hashCode()));
                    if(!thumbFile.exists()){
                        thumbDir.mkdirs();
                        try {
                            thumbFile.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    bm = LBC.getInstance().compressToFile(thumbFile, thumbDir);
                }else {
                    bm = LBC.getInstance().compressToBitmap(image_file);
                }

                setResponse(bm);
            }

        }
    }

    public TargetWrapper setPreLoad(Bitmap bm){
        if(mTargetView instanceof ImageView){
            ImageView iv = (ImageView) mTargetView;
            iv.setImageBitmap(bm);
        }else {
            mTargetView.setBackground(new BitmapDrawable(bm));
        }

        return this;
    }

    public TargetWrapper setResponse(Bitmap bm){
        if(mTargetView instanceof ImageView){
            ImageView iv = (ImageView) mTargetView;
            iv.setImageBitmap(bm);
        }else {
            mTargetView.setBackground(new BitmapDrawable(bm));
        }

        return this;
    }

}
