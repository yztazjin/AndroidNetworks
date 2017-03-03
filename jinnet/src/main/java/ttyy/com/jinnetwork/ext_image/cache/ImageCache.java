package ttyy.com.jinnetwork.ext_image.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * author: admin
 * date: 2017/03/03
 * version: 0
 * mail: secret
 * desc: ssc
 */

public class ImageCache {

    LruCache<Integer, Bitmap> mRuntimeCache;

    File mDiskCacheDir;

    private ImageCache(){
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        mRuntimeCache = new LruCache<>(maxSize);
    }

    static class Holder{
        static ImageCache INSTANCE = new ImageCache();
    }

    public static ImageCache getInstance(){
        return Holder.INSTANCE;
    }

    public ImageCache setDiskCacheDir(File dir){
        if(!dir.exists()){
            dir.mkdirs();
        }
        this.mDiskCacheDir = dir;
        return this;
    }

    public ImageCache setDiskCacheDir(Context context){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            this.mDiskCacheDir = context.getExternalCacheDir();
        }
        return this;
    }

    public File getDiskCacheDir(){
        return mDiskCacheDir;
    }

    /**
     * 内存缓存是否命中
     * @param url
     * @return
     */
    public boolean isRuntimeCacheHit(String url){

        int hashCode = getCacheToken(url);
        if(hashCode != -1){
            return mRuntimeCache.get(hashCode) != null;
        }

        return false;
    }

    public Bitmap getRuntimeCache(String url){
        int hashCode = getCacheToken(url);
        if(hashCode != -1){
            return mRuntimeCache.get(hashCode);
        }

        return null;
    }


    /**
     * 磁盘缓存是否命中
     * @param url
     * @return
     */
    public boolean isDiskCacheHit(String url){
        if(mDiskCacheDir == null){
            return false;
        }

        int hashCode = getCacheToken(url);
        if(hashCode != -1){
            return new File(mDiskCacheDir, String.valueOf(hashCode)).exists();
        }

        return false;
    }

    public File getDiskCache(String url){
        if(mDiskCacheDir != null){
            return null;
        }

        int hashCode = getCacheToken(url);
        if(hashCode != -1){
            return null;
        }

        File cachedFile = new File(mDiskCacheDir, String.valueOf(hashCode));
        if(cachedFile.exists()){
            return cachedFile;
        }
        return null;
    }

    /**
     * 缓存唯一标示符
     * @param url
     * @return
     */
    private int getCacheToken(String url){
        int hashCode = url == null?-1:url.hashCode();
        return hashCode;
    }

    /**
     * 进入缓存
     * @param url
     * @param bm
     */
    public void setIntoCache(String url, Bitmap bm){
        if(bm == null){
            return;
        }

        int hashCode = getCacheToken(url);
        if(hashCode != -1){
            mRuntimeCache.put(hashCode, bm);
        }

        if(mDiskCacheDir != null){
            File cachedFile = new File(mDiskCacheDir, String.valueOf(hashCode));
            if(!cachedFile.exists()){
                try {
                    FileOutputStream fos = new FileOutputStream(cachedFile);
                    bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    Log.w("Cache", "cache image fail file not found");
                } catch (IOException e) {
                    Log.w("Cache", "cache image fail io exception");
                }
            }
        }

    }
}
