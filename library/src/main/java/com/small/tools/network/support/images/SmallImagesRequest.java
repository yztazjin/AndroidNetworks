package com.small.tools.network.support.images;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.small.tools.network.global.Configs;
import com.small.tools.network.global.SmallLogs;
import com.small.tools.network.internal.RemoteResources;
import com.small.tools.network.internal.SmallHTTPRequest;
import com.small.tools.network.internal.async.Scheduler;
import com.small.tools.network.internal.cache.CacheAction;
import com.small.tools.network.internal.cache.SmallCache;
import com.small.tools.network.internal.interfaces.HTTPMethod;
import com.small.tools.network.internal.interfaces.HTTPRequest;
import com.small.tools.network.internal.interfaces.SmallHeader;
import com.small.tools.network.internal.interfaces.HTTPCallback;
import com.small.tools.network.internal.interfaces.HTTPClient;
import com.small.tools.network.internal.interfaces.RemoteResource;
import com.small.tools.network.internal.interfaces.ResourceDataParser;
import com.small.tools.network.internal.SmallFileBody;
import com.small.tools.network.internal.interfaces.SmallParamsParser;
import com.small.tools.network.internal.tools.UICallbackWrapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Author: hjq
 * Date  : 2018/10/04 11:10
 * Name  : SmallImagesRequest
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class SmallImagesRequest extends SmallHTTPRequest implements SmallImages {

    public SmallImagesRequest() {
        super();
        setResourceDataParser(Configs.getSingleton().images().getDataParser());
        setHTTPMethod(HTTPMethod.GET);
        setCacheManager(Configs.getSingleton().images().getCacheManager());
        setCacheAction(Configs.getSingleton().images().getCacheAction());
        setHTTPClient(Configs.getSingleton().images().getHTTPClient());
        setScheduler(Configs.getSingleton().images().getScheduler());

        HTTPCallback callback = new HTTPCallback() {
            @Override
            public void onStart(HTTPRequest request) {

            }

            @Override
            public void onProgress(HTTPRequest request, long cur, long total) {
                getAction().onProgress(SmallImagesRequest.this, cur, total);
            }

            @Override
            public void onSuccess(HTTPRequest request) {
                getAction().onSuccess(SmallImagesRequest.this);
            }

            @Override
            public void onFailure(HTTPRequest request) {
                getAction().onFailure(SmallImagesRequest.this);
            }

            @Override
            public void onCancel(HTTPRequest request, boolean isUserCanceled) {
                getAction().onCancel(SmallImagesRequest.this, isUserCanceled);
            }

            @Override
            public void onFinish(HTTPRequest request) {
                getAction().onFinish(SmallImagesRequest.this);
            }
        };
        setHTTPCallback(UICallbackWrapper.wrap(callback));
    }

    @Override
    public SmallImagesRequest setHeader(String key, String value) {
        return (SmallImagesRequest) super.setHeader(key, value);
    }

    @Override
    public SmallImagesRequest setHeaders(Map<String, String> headers, boolean isForce) {
        return (SmallImagesRequest) super.setHeaders(headers, isForce);
    }

    @Override
    public SmallImagesRequest clearHeader(String key) {
        return (SmallImagesRequest) super.clearHeader(key);
    }

    @Override
    public SmallImagesRequest clearHeaders() {
        return (SmallImagesRequest) super.clearHeaders();
    }

    @Override
    public SmallImagesRequest setHTTPContent(SmallHeader.ContentType content) {
        return (SmallImagesRequest) super.setHTTPContent(content);
    }

    @Override
    public SmallImagesRequest setHeader(SmallHeader header) {
        return (SmallImagesRequest) super.setHeader(header);
    }

    @Override
    public SmallImagesRequest setParam(String key, String value) {
        return (SmallImagesRequest) super.setParam(key, value);
    }

    @Override
    public SmallImagesRequest setParam(String key, int value) {
        return (SmallImagesRequest) super.setParam(key, value);
    }

    @Override
    public SmallImagesRequest setParam(String key, long value) {
        return (SmallImagesRequest) super.setParam(key, value);
    }

    @Override
    public SmallImagesRequest setParam(String key, boolean value) {
        return (SmallImagesRequest) super.setParam(key, value);
    }

    @Override
    public SmallImagesRequest setParam(String key, float value) {
        return (SmallImagesRequest) super.setParam(key, value);
    }

    @Override
    public SmallImagesRequest setParam(String key, double value) {
        return (SmallImagesRequest) super.setParam(key, value);
    }

    @Override
    public SmallImagesRequest setParam(String key, File file) {
        return (SmallImagesRequest) super.setParam(key, file);
    }

    @Override
    public SmallImagesRequest setParam(String key, SmallFileBody file) {
        return (SmallImagesRequest) super.setParam(key, file);
    }

    @Override
    public SmallImagesRequest setParams(SmallParamsParser parser, boolean isForce) {
        return (SmallImagesRequest) super.setParams(parser, isForce);
    }

    @Override
    public SmallImagesRequest setParams(Map<String, Object> params, boolean isForce) {
        return (SmallImagesRequest) super.setParams(params, isForce);
    }

    @Override
    public SmallImagesRequest clearParam(String key) {
        return (SmallImagesRequest) super.clearParam(key);
    }

    @Override
    public SmallImagesRequest clearParams() {
        return (SmallImagesRequest) super.clearParams();
    }

    @Override
    public SmallImagesRequest setCacheAction(CacheAction value) {
        return (SmallImagesRequest) super.setCacheAction(value);
    }

    @Override
    public SmallImagesRequest setCacheManager(SmallCache cache) {
        return (SmallImagesRequest) super.setCacheManager(cache);
    }

    @Override
    public SmallImagesRequest setRequestAddress(String address) {
        return (SmallImagesRequest) super.setRequestAddress(address);
    }

    @Override
    public SmallImagesRequest setRemoteResource(RemoteResource resources) {
        return (SmallImagesRequest) super.setRemoteResource(resources);
    }

    @Override
    public SmallImagesRequest setResourceDataParser(ResourceDataParser data) {
        if (data == null
                || data.getParseType() == null) {
            throw new NullPointerException("ResourceDataParser shouldn't be null!");
        }

        if (!Drawable.class.isAssignableFrom(data.getParseType())) {
            SmallLogs.w("data parse-type is not drawable!");
        }

        return (SmallImagesRequest) super.setResourceDataParser(data);
    }

    @Override
    public SmallImagesRequest setHTTPClient(HTTPClient client) {
        return (SmallImagesRequest) super.setHTTPClient(client);
    }

    @Override
    public SmallImagesRequest setHTTPMethod(HTTPMethod method) {
        return (SmallImagesRequest) super.setHTTPMethod(method);
    }

    @Override
    public SmallImagesRequest setHTTPCallback(HTTPCallback callback) {
        return (SmallImagesRequest) super.setHTTPCallback(callback);
    }

    @Override
    public SmallImagesRequest setScheduler(Scheduler scheduler) {
        return (SmallImagesRequest) super.setScheduler(scheduler);
    }

    @Override
    public void requestAsync() {
        if (mTargetView == null) {
            throw new NullPointerException("AttachedView is null!");
        }

        super.requestAsync();
    }

    @Override
    public SmallImagesRequest cancel(boolean isUserCanceled) {
        return (SmallImagesRequest) super.cancel(isUserCanceled);
    }

    @Override
    public SmallImagesRequest finish() {
        return (SmallImagesRequest) super.finish();
    }

    @Override
    public Object getToken() {
        if (mTargetView != null) {
            return mTargetView.getTarget();
        }
        return super.getToken();
    }

    /* *********************** Images extends api begin ********************* */

    Drawable mPlaceDrawable;
    Drawable mFailDrawable;
    SmallImagesAction mTargetView;

    @Override
    public SmallImages loadFromDrawable(Context context, int id) {
        setRemoteResource(RemoteResources.create().setResourceAddress("drawable-" + id));

        ResourceParserDrawable drawableParser = new ResourceParserDrawable();
        drawableParser.parse(context, id);
        setResourceDataParser(drawableParser);

        return this;
    }

    @Override
    public SmallImages loadFromRaw(Context context, int id) {

        InputStream is = context.getResources().openRawResource(id);
        setRemoteResource(RemoteResources.create().setResourceStream(is));

        return this;
    }

    @Override
    public SmallImages loadFromAssets(Context context, String name) {

        try {
            InputStream is = context.getAssets().open(name);
            setRemoteResource(RemoteResources.create().setResourceStream(is));
        } catch (IOException e) {
            e.printStackTrace();
            setRemoteResource(RemoteResources.create());
        }

        return this;
    }

    @Override
    public SmallImages loadFromNetwork(String address) {
        setRemoteResource(RemoteResources.net().setResourceAddress(address));
        return this;
    }

    @Override
    public SmallImages loadFromDisk(File file) {
        setRemoteResource(RemoteResources.local().setResourceAddress(file));
        return this;
    }

    public SmallImages placeholder(Drawable drawable) {
        this.mPlaceDrawable = drawable;
        return this;
    }

    @Override
    public Drawable getPlaceholderDrawable() {
        return mPlaceDrawable;
    }

    public SmallImages fail(Drawable drawable) {
        this.mFailDrawable = drawable;
        return this;
    }

    @Override
    public Drawable getFailDrawable() {
        return mFailDrawable;
    }

    @Override
    public Drawable getSuccessDrawable() {
        Drawable drawable = (Drawable) getResourceDataParser().getData();
        return drawable;
    }

    @Override
    public <T extends SmallImagesAction> T getAction() {
        return (T) mTargetView;
    }

    @Override
    public <T extends SmallImagesAction> void into(T view) {
        if (view == null) {
            return;
        }
        this.mTargetView = view;
        this.mTargetView.placeholder(this);
        requestAsync();
    }

    @Override
    public void into(View view) {
        if (view == null) {
            return;
        }

        into(new ImagesActionView(view));
    }

    @Override
    public void into(ImageView view) {
        if (view == null) {
            return;
        }

        into(new ImagesActionImageView(view));
    }

    /* *********************** Images extends api begin ********************* */
}
