package ttyy.com.jinnetwork.ext_image.compressor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

/**
 * Author: hjq
 * Date  : 2017/04/22 21:10
 * Name  : JinCompressor
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class JinCompressor {

    private JinCompressor(){

    }

    static class Holder{
        static JinCompressor INSTANCE = new JinCompressor();
    }

    public static JinCompressor get(){
        return Holder.INSTANCE;
    }

    public Bitmap compress(String path){

        return compress(Config.getInstance(), path);
    }

    public Bitmap compress(File file){

        return compress(file.getAbsolutePath());
    }

    public Bitmap compress(Config config, String path){

        if(config == null){
            return null;
        }

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);

        int i = 0;
        int byteFactor = getByteFactor(config.getBitmapConfig());
        int maxMemorySize = config.getMaxMemorySize();
        while ((opts.outHeight >> i) * (opts.outWidth >> i) * byteFactor > maxMemorySize ){

            i++;
        }

        opts.inSampleSize = i;
        opts.inPreferredConfig = config.getBitmapConfig();
        opts.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(path, opts);

        return bm;
    }

    private int getByteFactor(Bitmap.Config config){

        if(Bitmap.Config.ALPHA_8 == config){

            return 4;
        }else if (Bitmap.Config.ARGB_4444 == config){

            return 2;
        }else if(Bitmap.Config.ARGB_8888 == config){

            return 4;
        }else if(Bitmap.Config.RGB_565 == config){

            return 2;
        }

        return 4;
    }

    public static class Config {

        private Bitmap.Config mConfig;
        private int mMaxMemorySize;

        private Config(){
            mConfig = Bitmap.Config.RGB_565;
            mMaxMemorySize = 1024 * 1024; // 图片最大占用内存不能超过1M
        }

        static class Holder{
            static Config INSTANCE = new Config();
        }

        public static Config getInstance(){
            return Holder.INSTANCE;
        }

        public static Config createInstance(){
            return new Config();
        }

        public Config setBitmapConfig(Bitmap.Config config){
            this.mConfig = config;
            return this;
        }

        public Config setMaxMemorySize(int byteSize){
            this.mMaxMemorySize = byteSize;
            return this;
        }

        public int getMaxMemorySize(){
            return this.mMaxMemorySize;
        }

        public Bitmap.Config getBitmapConfig(){
            return this.mConfig;
        }

    }
}
