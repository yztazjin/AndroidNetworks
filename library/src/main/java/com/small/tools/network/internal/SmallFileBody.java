package com.small.tools.network.internal;

import java.io.File;

/**
 * Author: hjq
 * Date  : 2018/10/03 12:18
 * Name  : SmallFileBody
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class SmallFileBody {

    File mFile;
    String mFileNameDesc;

    public SmallFileBody(File file) {
        this(file, file.getName());
    }

    public SmallFileBody(File file, String filename) {
        this.mFile = file;
        this.mFileNameDesc = filename;
    }

    public SmallFileBody setFile(File file) {
        this.mFile = file;
        return this;
    }

    public SmallFileBody setFileNameDesc(String desc) {
        this.mFileNameDesc = desc;
        return this;
    }

    public File getFile() {
        return mFile;
    }

    public String getFileNameDesc() {
        return mFileNameDesc;
    }
}
