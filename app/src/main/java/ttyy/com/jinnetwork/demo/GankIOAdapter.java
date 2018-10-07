package ttyy.com.jinnetwork.demo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.small.tools.network.internal.cache.CacheAction;
import com.small.tools.network.support.Images;
import com.small.tools.network.support.images.ImagesActionImageView;

import java.util.ArrayList;

/**
 * Author: hjq
 * Date  : 2017/03/16 21:37
 * Name  : GankIOAdapter
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class GankIOAdapter extends BaseAdapter {

    ArrayList<GankIOBean.Data> datas;

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image, null);
        }

        ImageView iv = (ImageView) view.findViewById(R.id.iv_image);
        GankIOBean.Data data = datas.get(i);
        Log.d("Https", "i " + i + " " + iv + " " + data.url);
        Images.get().loadFromNetwork(data.url)
                .placeholder(view.getContext().getResources().getDrawable(R.drawable.shape_pre))
                .fail(view.getContext().getResources().getDrawable(R.drawable.shape_err))
                .into(new ImagesActionImageView(iv));

        return view;
    }
}
