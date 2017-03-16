package ttyy.com.jinnetwork.core.work.inner;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import ttyy.com.jinnetwork.core.callback.HTTPCallback;
import ttyy.com.jinnetwork.core.work.HTTPRequest;
import ttyy.com.jinnetwork.core.work.HTTPResponse;

/**
 * author: admin
 * date: 2017/02/28
 * version: 0
 * mail: secret
 * desc: $HTTPResponse
 */

public class $HttpResponse implements HTTPResponse {

    /**
     * 请求对象
     */
    HTTPRequest request;
    /**
     * 返回内容长度
     */
    long mContentLength;
    /**
     * Https Status Code
     */
    int mHttpStatusCode;
    /**
     * 错误消息
     */
    String mErrorMessage;
    /**
     * 请求返回的Header
     */
    Map<String, String> mHeadersDict;
    /**
     * 内容Str
     */
    String mContentStr;
    /**
     * 内容比特
     */
    byte[] mContentBytes;

    public $HttpResponse(HTTPRequest request){
        this.request = request;
        this.mHeadersDict = new HashMap<>();
    }

    @Override
    public int getStatusCode() {
        return mHttpStatusCode;
    }

    @Override
    public String getHeader(String key) {
        return mHeadersDict.get(key);
    }

    @Override
    public Map<String, String> getHeaders() {
        return mHeadersDict;
    }

    @Override
    public HTTPRequest getHttpRequest() {
        return request;
    }

    @Override
    public long getContentLength() {
        return mContentLength;
    }

    @Override
    public byte[] getContent() {
        return mContentBytes;
    }

    @Override
    public String getConentToString() {
        if(mContentStr == null){
            if(mContentBytes == null
                    || mContentBytes.length == 0){
                mContentStr = "";
            }else {
                mContentStr = new String(mContentBytes);
            }
        }
        return mContentStr;
    }

    public $HttpResponse setStatusCode(int code){
        mHttpStatusCode = code;
        return this;
    }

    public $HttpResponse setContentLength(long contentLength){
        mContentLength = contentLength;
        return this;
    }

    public $HttpResponse addHeader(String key, String value){
        mHeadersDict.put(key, value);
        return this;
    }

    public $HttpResponse readContentFromStream(InputStream stream){
        if(request.getDownloadFile() != null){
            // 下载文件模式
            readContentToFile(stream);
        }else {
            // 普通模式
            readContentToBytes(stream);
        }

        return this;
    }

    private void readContentToFile(InputStream stream){
        HTTPCallback callback = request.getHttpCallback();
        File file = request.getDownloadFile();
        long totalLength = 0;
        if(file != null){
            totalLength = file.length() + mContentLength;
        }
        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            byte[] buffer = new byte[4096];
            int length = 0;
            while ((length = stream.read(buffer)) != -1){
                fos.write(buffer, 0, length);

                if(request.isCanceled()){
                    break;
                }
                if(callback != null){
                    callback.onProgress(this, file.length() , totalLength);
                }
            }
            fos.flush();
            fos.close();
            stream.close();
        } catch (FileNotFoundException e) {
            Log.w("Https", "Error "+e.getMessage());
            file.delete();
            setStatusCode(-1);
            setErrorMessage("FileNotFoundException "+file.getPath());
        } catch (IOException e) {
            Log.w("Https", "Error "+e.getMessage());
            file.delete();
            setStatusCode(-1);
            setErrorMessage("IOException "+e.getMessage());
        }
    }

    private void readContentToBytes(InputStream stream){
        HTTPCallback callback = request.getHttpCallback();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int length = 0;
            int currLength = 0;
            while ((length = stream.read(buffer)) != -1){
                baos.write(buffer, 0, length);

                currLength += length;
                if(request.isCanceled()){
                    break;
                }

                if(callback != null){
                    callback.onProgress(this, currLength , mContentLength);
                }
            }

            setContentBytes(baos.toByteArray());

            baos.flush();
            baos.close();
            stream.close();
        } catch (IOException e) {
            Log.w("Https", "Error "+e.getMessage());
            setStatusCode(-1);
            setErrorMessage("IOException "+e.getMessage());
        }

    }

    public $HttpResponse setContentBytes(byte[] bytes){
        mContentBytes = bytes;
        return this;
    }

    public $HttpResponse setException(Exception exception){
        setErrorMessage(ex2Str(exception));
        return this;
    }

    public $HttpResponse setErrorMessage(String text){
        mErrorMessage = text;
        return this;
    }

    @Override
    public String getErrorMessage(){
        return mErrorMessage;
    }

    /**
     * Exception 转化为 String
     *
     * @param ex
     */
    private static String ex2Str(Throwable ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}
