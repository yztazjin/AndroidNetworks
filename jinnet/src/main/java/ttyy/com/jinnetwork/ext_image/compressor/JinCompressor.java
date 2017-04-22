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

        return compress(SourceConfig.get().setBitmapFilePath(path));
    }

    public Bitmap compress(File file){

        return compress(SourceConfig.get().setBitmapFile(file));
    }

    public Bitmap compress(SourceConfig config){

        if(config == null){
            return null;
        }

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(config.getBitmapFilePath(), opts);

        int i = 0;
        int byteFactor = getByteFactor(config.getBitmapConfig());
        int maxMemorySize = config.getMaxMemorySize();
        while ((opts.outHeight >> i) * (opts.outWidth >> i) * byteFactor > maxMemorySize ){

            i++;
        }

        opts.inSampleSize = i;
        opts.inPreferredConfig = config.getBitmapConfig();
        opts.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(config.getBitmapFilePath(), opts);

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

    static class SourceConfig{

        private String mPath;
        private Bitmap.Config mConfig;
        private int mMaxMemorySize;

        private SourceConfig(){
            mConfig = Bitmap.Config.RGB_565;
            mMaxMemorySize = 1024 * 1024; // 图片最大占用内存不能超过1M
        }

        public static SourceConfig get(){
            return new SourceConfig();
        }

        public SourceConfig setBitmapFilePath(String path){
            this.mPath = path;
            return this;
        }

        public SourceConfig setBitmapFile(File file){
            if(file != null
                    && file.exists()){
                return setBitmapFilePath(file.getAbsolutePath());
            }

            return this;
        }

        public SourceConfig setBitmapConfig(Bitmap.Config config){
            this.mConfig = config;
            return this;
        }

        public SourceConfig setMaxMemorySize(int byteSize){
            this.mMaxMemorySize = byteSize;
            return this;
        }

        public String getBitmapFilePath(){
            return this.mPath;
        }

        public int getMaxMemorySize(){
            return this.mMaxMemorySize;
        }

        public Bitmap.Config getBitmapConfig(){
            return this.mConfig;
        }

        public File getBitmapFile(){
            return new File(mPath);
        }

    }
}
