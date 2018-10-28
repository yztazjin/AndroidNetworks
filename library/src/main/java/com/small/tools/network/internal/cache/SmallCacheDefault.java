package com.small.tools.network.internal.cache;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.LruCache;

import com.small.tools.network.global.Configs;
import com.small.tools.network.internal.cache.jakewharton_disk.DiskLruCache;
import com.small.tools.network.internal.interfaces.HTTPRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Author: hjq
 * Date  : 2018/10/04 22:06
 * Name  : SmallCacheDefault
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class SmallCacheDefault implements SmallCache {

    LruCache<String, Object> mRuntimeCache;
    DiskLruCache mDiskCache;

    File mDiskCacheDirectory;

    public SmallCacheDefault() {
        int nRuntimeMaxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        mRuntimeCache = new LruCache<>(nRuntimeMaxSize);
    }

    @Override
    public void cache(HTTPRequest request, Object parsedData) {
        if (parsedData == null
                || getCacheKey(request) == null) {
            return;
        }

        String key = getCacheKey(request);

        switch (request.getCacheAction()) {
            case Runtime:
                cacheToRuntime(key, parsedData);
                break;
            case Disk:
                cacheToDisk(key, parsedData);
                break;
            case All:
                cacheToRuntime(key, parsedData);
                cacheToDisk(key, parsedData);
                break;
            case None:

                break;
        }

    }

    @Override
    public SmallCache setDiskCacheDirectory(File file) {
        if (mDiskCacheDirectory != null
                && mDiskCacheDirectory.equals(file)) {
            return this;
        }

        mDiskCacheDirectory = file;

        if (file == null) {
            return this;
        }

        if (!file.exists()) {
            file.mkdirs();
        }

        if (file.exists()) {
            long nDiskMaxSize = 1024 * 1024 * 50;
            try {
                mDiskCache = DiskLruCache.open(mDiskCacheDirectory,
                        0, 1, nDiskMaxSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    @Override
    public File getDiskCacheDirectory() {
        return mDiskCacheDirectory;
    }

    @Override
    public <T> T getCachedData(HTTPRequest request) {
        if (getCacheKey(request) == null) {
            return null;
        }

        String key = getCacheKey(request);
        Object value = null;

        if (key != null) {

            switch (request.getCacheAction()) {
                case Runtime:
                    value = mRuntimeCache.get(key);
                    break;
                case Disk:
                    if (mDiskCache != null) {
                        try {
                            DiskLruCache.Snapshot snapshot = mDiskCache.get(key);
                            if (snapshot != null) {
                                request.getResourceDataParser().parse(request,
                                        snapshot.getInputStream(0));
                                snapshot.close();

                                value = request.getResourceDataParser().getData();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case All:
                    value = mRuntimeCache.get(key);

                    if (value == null
                            && mDiskCache != null) {
                        try {
                            DiskLruCache.Snapshot snapshot = mDiskCache.get(key);
                            if (snapshot != null) {
                                request.getResourceDataParser().parse(request,
                                        snapshot.getInputStream(0));
                                snapshot.close();

                                value = request.getResourceDataParser().getData();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case None:
                    value = null;
                    break;
            }

        }

        return (T) value;
    }

    @Override
    public boolean isCacheHit(HTTPRequest request) {
        if (getCacheKey(request) == null) {
            return false;
        }

        String key = getCacheKey(request);
        boolean value = false;

        if (key != null) {

            switch (request.getCacheAction()) {
                case Runtime:
                    value = mRuntimeCache.get(key) != null;
                    break;
                case Disk:
                    if (mDiskCache != null) {
                        try {
                            DiskLruCache.Snapshot snapshot = mDiskCache.get(key);

                            if (snapshot != null) {
                                value = true;
                                snapshot.close();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case All:
                    value = mRuntimeCache.get(key) != null;

                    if (!value && mDiskCache != null) {
                        try {
                            DiskLruCache.Snapshot snapshot = mDiskCache.get(key);
                            if (snapshot != null) {
                                value = true;
                                snapshot.close();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case None:
                    value = false;
                    break;
            }

        }
        return value;
    }


    String getCacheKey(HTTPRequest request) {
        if (request != null
                && request.getRemoteResource() != null
                && request.getRemoteResource().getResourceAddress() != null) {
            String key = String.valueOf(request.getRemoteResource()
                    .getResourceAddress().hashCode());

            return key;
        }
        return null;
    }

    void cacheToRuntime(String key, Object data) {
        if (key == null
                || data == null) {
            return;
        }
        mRuntimeCache.put(key, data);
    }

    void cacheToDisk(String key, Object parsedData) {
        if (mDiskCache == null
                || key == null
                || parsedData == null) {
            return;
        }
        try {
            DiskLruCache.Editor editor = mDiskCache.edit(key);
            OutputStream os = editor.newOutputStream(0);

            Class parseClass = parsedData.getClass();

            if (File.class.equals(parseClass)) {
                File file = (File) parsedData;

                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[4096];
                int length = 0;
                while ((length = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, length);
                }

                fis.close();

            } else if (String.class.equals(parseClass)) {
                mDiskCache.edit(key).set(0, (String) parsedData);

            } else if (Bitmap.class.equals(parseClass)) {
                Bitmap bm = (Bitmap) parsedData;
                bm.compress(Bitmap.CompressFormat.PNG, 100, os);

            } else if (BitmapDrawable.class.equals(parseClass)) {
                Bitmap bm = ((BitmapDrawable) parsedData).getBitmap();
                bm.compress(Bitmap.CompressFormat.PNG, 100, os);

            }

            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
