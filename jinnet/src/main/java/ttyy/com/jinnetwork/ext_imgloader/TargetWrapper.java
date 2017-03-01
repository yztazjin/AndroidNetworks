package ttyy.com.jinnetwork.ext_imgloader;

import android.view.View;

import java.io.File;

import ttyy.com.jinnetwork.core.callback.HttpUIThreadCallbackAdapter;
import ttyy.com.jinnetwork.core.work.HTTPResponse;

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

        }

    }
}
