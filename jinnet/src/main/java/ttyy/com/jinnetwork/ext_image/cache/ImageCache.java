package ttyy.com.jinnetwork.ext_image.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.LruCache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ttyy.com.jinnetwork.core.config.__Log;

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

    private ImageCache() {
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        mRuntimeCache = new LruCache<>(maxSize);
    }

    static class Holder {
        static ImageCache INSTANCE = new ImageCache();
    }

    public static ImageCache getInstance() {
        return Holder.INSTANCE;
    }

    public ImageCache setDiskCacheDir(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        this.mDiskCacheDir = dir;
        return this;
    }

    public ImageCache setDiskCacheDir(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            this.mDiskCacheDir = context.getExternalCacheDir();
        }
        return this;
    }

    public File getDiskCacheDir() {
        return mDiskCacheDir;
    }

    /**
     * 内存缓存是否命中
     *
     * @param url
     * @return
     */
    public boolean isRuntimeCacheHit(String url) {

        int hashCode = getCacheToken(url);
        if (hashCode != -1) {
            return mRuntimeCache.get(hashCode) != null;
        }

        return false;
    }

    public Bitmap getRuntimeCache(String url) {
        int hashCode = getCacheToken(url);
        if (hashCode != -1) {
            return mRuntimeCache.get(hashCode);
        }

        return null;
    }


    /**
     * 磁盘缓存是否命中
     *
     * @param url
     * @return
     */
    public boolean isDiskCacheHit(String url) {
        if (mDiskCacheDir == null) {
            return false;
        }

        int hashCode = getCacheToken(url);
        if (hashCode != -1) {
            return getDiskThumbCacheFile(hashCode).exists();
        }

        return false;
    }

    public File getDiskCache(String url) {
        if (mDiskCacheDir == null) {
            return null;
        }

        int hashCode = getCacheToken(url);
        if (hashCode == -1) {
            return null;
        }

        File cachedFile = getDiskThumbCacheFile(hashCode);
        if (cachedFile.exists()) {
            return cachedFile;
        }
        return null;
    }

    /**
     * 缓存唯一标示符
     *
     * @param url
     * @return
     */
    private int getCacheToken(String url) {
        int hashCode = url == null ? -1 : url.hashCode();
        return hashCode;
    }

    /**
     * 存入缓存
     *
     * @param url
     * @param bm
     * @param cacheType
     */
    public void setIntoCache(String url, Bitmap bm, ImageCacheType cacheType) {
        if (bm == null) {
            return;
        }

        cacheType = cacheType == null ? ImageCacheType.NoneCache : cacheType;
        if (cacheType.useRuntimeCache()) {
            setIntoRuntimeCache(url, bm);
        }

        if (cacheType.useDiskCache()) {
            setIntoDiskCache(url, bm);
        }
    }

    /**
     * 内存缓存
     *
     * @param url
     * @param bm
     */
    public void setIntoRuntimeCache(String url, Bitmap bm) {
        __Log.i("Images", "setIntoRuntimeCache "+url);
        if (bm == null) {
            return;
        }

        int token = getCacheToken(url);
        if (token != -1) {
            mRuntimeCache.put(token, bm);
        }
    }

    /**
     * 磁盘缓存
     *
     * @param url
     * @param bm
     */
    public void setIntoDiskCache(String url, Bitmap bm) {
        __Log.i("Images", "setIntoDiskCache "+url);
        if (bm == null) {
            return;
        }

        int token = getCacheToken(url);
        if (mDiskCacheDir != null) {
            File cachedFile = getDiskThumbCacheFile(token);
            try {
                FileOutputStream fos = new FileOutputStream(cachedFile, false);
                bm.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                __Log.w("Cache", "cache image fail file not found");
            } catch (IOException e) {
                __Log.w("Cache", "cache image fail io exception");
            }
        } else {
            __Log.w("Cache", "Hasn't Set DiskCacheDir!");
        }
    }

    /**
     * 获取磁盘缓存的缩略图
     * @param token
     * @return
     */
    private File getDiskThumbCacheFile(int token) {
        File cachedFileDir = new File(mDiskCacheDir, "thumb");
        if (!cachedFileDir.exists()) {
            cachedFileDir.mkdirs();
        }
        File cachedFile = new File(cachedFileDir, String.valueOf(token));
        return cachedFile;
    }
}
