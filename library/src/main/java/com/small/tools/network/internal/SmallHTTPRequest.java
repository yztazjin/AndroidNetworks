package com.small.tools.network.internal;

import android.text.TextUtils;

import com.small.tools.network.global.Configs;
import com.small.tools.network.internal.async.Scheduler;
import com.small.tools.network.internal.cache.CacheAction;
import com.small.tools.network.internal.cache.SmallCache;
import com.small.tools.network.internal.interfaces.HTTPMethod;
import com.small.tools.network.internal.interfaces.SmallHeader;
import com.small.tools.network.internal.interfaces.StatusCode;
import com.small.tools.network.internal.interfaces.HTTPCallback;
import com.small.tools.network.internal.interfaces.HTTPClient;
import com.small.tools.network.internal.interfaces.HTTPRequest;
import com.small.tools.network.internal.interfaces.HTTPResponse;
import com.small.tools.network.internal.interfaces.RemoteResource;
import com.small.tools.network.internal.interfaces.ResourceDataParser;
import com.small.tools.network.internal.interfaces.SmallParamsParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

/**
 * Author: hjq
 * Date  : 2018/10/02 21:24
 * Name  : SmallHTTPRequest
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class SmallHTTPRequest implements HTTPRequest {

    TreeMap<String, String> mHeaders;
    TreeMap<String, Object> mParams;

    CacheAction mCacheAction;
    SmallCache mCacheManager;

    ResourceDataParser mDataParser;
    RemoteResource mRemoteResource;

    HTTPClient mHTTPClient;
    HTTPMethod mHTTPMethod;
    SmallHeader.ContentType mHTTPContent;

    SmallHTTPResponse mHTTPResponse;
    HTTPCallback mHTTPCallback;

    boolean isRequestExecuted = false;
    boolean isRequestCanceled = false;
    boolean isRequestFinished = false;

    Scheduler mScheduler;

    public SmallHTTPRequest() {
        mHeaders = new TreeMap<String, String>();
        mParams = new TreeMap<String, Object>();
        mHTTPResponse = new SmallHTTPResponse();

        mScheduler = Configs.getSingleton().getScheduler();
        mHTTPClient = Configs.getSingleton().getHTTPClient();
        mCacheManager = Configs.getSingleton().getCacheManager();
        mCacheAction = CacheAction.None;
        mDataParser = Configs.getSingleton().getDataParser();
    }

    @Override
    public HTTPRequest setHeader(String key, String value) {
        if (!TextUtils.isEmpty(key)) {
            mHeaders.put(key, value);
        }
        return this;
    }

    @Override
    public HTTPRequest setHeaders(Map<String, String> headers, boolean isForce) {
        if (isForce) {
            if (headers == null) {
                mHeaders.clear();
            } else {
                mHeaders = new TreeMap<String, String>(headers);
            }
        } else {
            if (headers != null)
                mHeaders.putAll(headers);
        }
        return this;
    }

    @Override
    public HTTPRequest clearHeader(String key) {
        if (!TextUtils.isEmpty(key)) {
            mHeaders.remove(key);
        }
        return this;
    }

    @Override
    public HTTPRequest clearHeaders() {
        mHeaders.clear();
        return this;
    }

    @Override
    public String getHeader(String key) {
        if (!TextUtils.isEmpty(key)) {
            return mHeaders.get(key);
        }
        return null;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new TreeMap<String, String>(mHeaders);
    }

    @Override
    public HTTPRequest setHTTPContent(SmallHeader.ContentType content) {
        if (content != null) {
            mHTTPContent = content;
            setHeader(content.getName(), content.getValue());
        }
        return this;
    }

    @Override
    public HTTPRequest setHeader(SmallHeader header) {
        if (header != null) {
            setHeader(header.getName(), header.getValue());
        }
        return this;
    }

    @Override
    public SmallHeader.ContentType getHTTPContent() {
        return mHTTPContent;
    }

    @Override
    public HTTPRequest setParam(String key, String value) {
        if (key != null) {
            mParams.put(key, value);
        }
        return this;
    }

    @Override
    public HTTPRequest setParam(String key, int value) {
        if (key != null) {
            mParams.put(key, value);
        }
        return this;
    }

    @Override
    public HTTPRequest setParam(String key, long value) {
        if (key != null) {
            mParams.put(key, value);
        }
        return this;
    }

    @Override
    public HTTPRequest setParam(String key, boolean value) {
        if (key != null) {
            mParams.put(key, value);
        }
        return this;
    }

    @Override
    public HTTPRequest setParam(String key, float value) {
        if (key != null) {
            mParams.put(key, value);
        }
        return this;
    }

    @Override
    public HTTPRequest setParam(String key, double value) {
        if (key != null) {
            mParams.put(key, value);
        }
        return this;
    }

    @Override
    public HTTPRequest setParam(String key, File file) {
        return setParam(key, new SmallFileBody(file));
    }

    @Override
    public HTTPRequest setParam(String key, SmallFileBody file) {
        if (key != null) {
            mParams.put(key, file);
        }
        return this;
    }

    @Override
    public HTTPRequest setParams(SmallParamsParser parser, boolean isForce) {
        if (isForce) {
            if (parser == null) {
                mParams.clear();
            } else {
                setParams(parser.smallParse(), isForce);
            }

        } else {
            if (parser != null) {
                setParams(parser.smallParse(), isForce);
            }

        }
        return this;
    }

    @Override
    public HTTPRequest setParams(Map<String, Object> params, boolean isForce) {
        if (isForce) {
            if (params == null) {
                mParams.clear();
            } else {
                mParams = new TreeMap<String, Object>(params);
            }
        } else {
            if (params != null)
                mParams.putAll(params);
        }
        return this;
    }

    @Override
    public HTTPRequest clearParam(String key) {
        if (key != null) {
            mParams.remove(key);
        }
        return this;
    }

    @Override
    public HTTPRequest clearParams() {
        mParams.clear();
        return this;
    }

    @Override
    public <T> T getParam(String key) {
        return (T) mParams.get(key);
    }

    @Override
    public Map<String, Object> getParams() {
        return new TreeMap<String, Object>(mParams);
    }

//    @Override
//    public HTTPRequest setIgnoreSslCertificate() {
//        isIgnoreSslCertificate = true;
//        mSslSocketFactory = null;
//        return this;
//    }

//    @Override
//    public HTTPRequest setTrustedSslCertificate(SSLSocketFactory factory) {
//        if (factory == null) {
//            isIgnoreSslCertificate = true;
//        } else {
//            isIgnoreSslCertificate = false;
//            mSslSocketFactory = factory;
//        }
//        return this;
//    }

    @Override
    public boolean isIgnoreSslCertificate() {
        return true;
    }

//    @Override
//    public SSLSocketFactory getTrustedSslCertificate() {
//        return mSslSocketFactory;
//    }


    @Override
    public HTTPRequest setCacheAction(CacheAction action) {
        mCacheAction = action;
        return this;
    }

    @Override
    public CacheAction getCacheAction() {
        return mCacheAction;
    }

    @Override
    public HTTPRequest setCacheManager(SmallCache cache) {
        mCacheManager = cache;
        return this;
    }

    @Override
    public SmallCache getCacheManager() {
        return mCacheManager;
    }

    @Override
    public HTTPRequest setRequestAddress(String address) {
        if (mRemoteResource == null) {
            mRemoteResource = RemoteResources.create();
        }
        mRemoteResource.setResourceAddress(address);
        return this;
    }

    @Override
    public HTTPRequest setRemoteResource(RemoteResource resources) {
        mRemoteResource = resources;
        return this;
    }

    @Override
    public RemoteResource getRemoteResource() {
        return mRemoteResource;
    }

    @Override
    public HTTPRequest setResourceDataParser(ResourceDataParser parser) {
        mDataParser = parser;
        return this;
    }

    @Override
    public <T extends ResourceDataParser> T getResourceDataParser() {
        return (T) mDataParser;
    }

    @Override
    public HTTPRequest setHTTPClient(HTTPClient client) {
        mHTTPClient = client;
        return this;
    }

    @Override
    public HTTPClient getHTTPClient() {
        return mHTTPClient;
    }

    @Override
    public HTTPMethod getHTTPMethod() {
        return mHTTPMethod;
    }

    @Override
    public HTTPRequest setHTTPMethod(HTTPMethod method) {
        mHTTPMethod = method;
        return this;
    }

    @Override
    public HTTPResponse getResponse() {
        return mHTTPResponse;
    }

    @Override
    public HTTPRequest setHTTPCallback(HTTPCallback callback) {
        mHTTPCallback = callback;
        return this;
    }

    @Override
    public HTTPCallback getHTTPCallback() {
        return mHTTPCallback;
    }

    @Override
    public HTTPResponse requestSync() {

        if (isExecuted()) {
            return mHTTPResponse;
        }

        isRequestExecuted = true;

        if (isCanceled()
                || isFinished()) {
            return mHTTPResponse;
        }

        if (getRemoteResource() == null
                || getResourceDataParser() == null) {
            return mHTTPResponse;
        }

        if (getHTTPCallback() != null) {
            getHTTPCallback().onStart(this);
        }

        if (getCacheManager() != null
                && getCacheManager().isCacheHit(this)) {
            // cache check
            getResourceDataParser().parse(this,
                    getCacheManager().getCachedData(this));

            if (isCanceled()
                    || isFinished()) {

            } else {
                mHTTPResponse.setStatusCode(StatusCode.REQUEST_SUCCESS_FROM_CACHE);

                if (getHTTPCallback() != null) {
                    getHTTPCallback().onSuccess(this);
                }
            }

        } else if (getResourceDataParser().getData() != null) {
            // check if has parsed the data
            if (isCanceled()
                    || isFinished()) {

            } else {
                mHTTPResponse.setStatusCode(StatusCode.REQUEST_SUCCESS_FROM_PARSEDDATA);
                if (getHTTPCallback() != null) {
                    getHTTPCallback().onSuccess(this);
                }
            }

        } else if (getRemoteResource().getResourceStream() != null) {
            // check if has a remote-resource-stream
            InputStream resourceStream = getRemoteResource().getResourceStream();

            int statusCode = getResourceDataParser().parse(this, resourceStream);

            if (isCanceled()
                    || isFinished()) {

            } else {
                mHTTPResponse.setStatusCode(statusCode);
                if (StatusCode.isStatusCodeSuccessful(statusCode)) {
                    mHTTPResponse.setStatusCode(StatusCode.REQUEST_SUCCESS_FROM_STREAM);
                    try {
                        mHTTPResponse.setContentLength(resourceStream.available());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (getHTTPCallback() != null) {
                        getHTTPCallback().onSuccess(this);
                    }

                } else {
                    if (getHTTPCallback() != null) {
                        getHTTPCallback().onFailure(this);
                    }

                }
            }

            try {
                resourceStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // request data from local/network
            // not support other
            switch (getRemoteResource().getResourceType()) {
                case Local: {
                    InputStream resourceStream = null;
                    try {
                        File file = new File(getRemoteResource().getResourceAddress());
                        resourceStream = new FileInputStream(file);

                        int statusCode = getResourceDataParser().parse(this, resourceStream);

                        if (isCanceled()
                                || isFinished()) {

                        } else {
                            mHTTPResponse.setStatusCode(statusCode);
                            if (StatusCode.isStatusCodeSuccessful(statusCode)) {
                                mHTTPResponse.setStatusCode(StatusCode.REQUEST_SUCCESS_FROM_FILE);
                                mHTTPResponse.setContentLength(file.length());

                                if (getHTTPCallback() != null) {
                                    getHTTPCallback().onSuccess(this);
                                }

                            } else if (!isCanceled() && !isFinished()) {
                                if (getHTTPCallback() != null) {
                                    getHTTPCallback().onFailure(this);
                                }

                            }
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        if (isCanceled()
                                || isFinished()) {

                        } else {
                            mHTTPResponse.setStatusCode(StatusCode.PARSE_ERROR_FILENOTFOUND);

                            if (getHTTPCallback() != null) {
                                getHTTPCallback().onFailure(this);
                            }
                        }
                    } finally {
                        if (resourceStream != null) {
                            try {
                                resourceStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    break;
                }
                case Network: {

                    if (getHTTPClient() == null
                            || getHTTPMethod() == null) {
                        mHTTPResponse.setStatusCode(StatusCode.REQUEST_ERROR_NOT_PREPARED);

                    } else {
                        switch (getHTTPMethod()) {
                            case GET:
                                getHTTPClient().get(this);
                                break;
                            case POST:
                                getHTTPClient().post(this);
                                break;
                            case USER:

                                break;
                        }

                    }

                    if (isCanceled()
                            || isFinished()) {

                    } else {
                        if (StatusCode.isStatusCodeSuccessful(mHTTPResponse.getStatusCode())) {
                            if (getHTTPCallback() != null) {
                                getHTTPCallback().onSuccess(this);
                            }
                        } else {
                            if (getHTTPCallback() != null) {
                                getHTTPCallback().onFailure(this);
                            }
                        }
                    }

                    break;
                }
                case Other: {

                    if (isCanceled()
                            || isFinished()) {

                    } else {
                        mHTTPResponse.setStatusCode(StatusCode.REQUEST_ERROR_OTHERRES);
                        if (getHTTPCallback() != null) {
                            getHTTPCallback().onFailure(this);
                        }
                    }
                }
            }
        }

        finish();

        return mHTTPResponse;
    }

    @Override
    public void requestAsync() {
        if (getScheduler() == null) {
            throw new NullPointerException("Scheduler is null!");
        }

        getScheduler().submit(this);
    }

    @Override
    public boolean isExecuted() {
        return isRequestExecuted;
    }

    @Override
    public HTTPRequest cancel(boolean isUserCanceled) {
        if (isCanceled()) {
            return this;
        }

        isRequestCanceled = true;
        if (getHTTPCallback() != null) {
            mHTTPResponse.setStatusCode(isUserCanceled ? StatusCode.REQUEST_ERROR_USER_CANCEL
                    : StatusCode.REQUEST_ERROR_TOOL_CANCEL);
            getHTTPCallback().onCancel(this, isUserCanceled);
        }

        finish();
        return this;
    }

    @Override
    public boolean isCanceled() {
        return isRequestCanceled;
    }

    @Override
    public HTTPRequest finish() {
        if (isFinished()) {
            return this;
        }

        isRequestFinished = true;

        if (getScheduler() != null) {
            getScheduler().finish(this);
        }

        if (getHTTPCallback() != null) {
            getHTTPCallback().onFinish(this);
        }

        // cache data
        if (getResponse().isStatusSuccessful()) {
            if (getCacheManager() != null) {
                getCacheManager().cache(this, getResourceDataParser().getData());
            }
        }

        return this;
    }

    @Override
    public boolean isFinished() {
        return isRequestFinished;
    }

    @Override
    public Object getToken() {
        return this;
    }

    public HTTPRequest setScheduler(Scheduler scheduler) {
        this.mScheduler = scheduler;
        return this;
    }

    public Scheduler getScheduler() {
        return mScheduler;
    }

}
