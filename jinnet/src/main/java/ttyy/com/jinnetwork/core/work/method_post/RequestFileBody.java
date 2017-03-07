package ttyy.com.jinnetwork.core.work.method_post;

import java.io.File;

/**
 * Author: hjq
 * Date  : 2017/03/07 20:40
 * Name  : RequestFileBody
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class RequestFileBody {

    File mFile;
    String mFileNameDesc;

    public RequestFileBody(File file){
        this(file, file.getName());
    }

    public RequestFileBody(File file, String filename){
        this.mFile = file;
        this.mFileNameDesc = filename;
    }

    public RequestFileBody setFile(File file){
        this.mFile = file;
        return this;
    }

    public RequestFileBody setFileNameDesc(String desc){
        this.mFileNameDesc = desc;
        return this;
    }

    public File getFile(){
        return mFile;
    }

    public String getFileNameDesc(){
        return mFileNameDesc;
    }

}
