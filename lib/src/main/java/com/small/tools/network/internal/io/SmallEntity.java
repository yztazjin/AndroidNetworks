package com.small.tools.network.internal.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Author: hjq
 * Date  : 2018/10/03 15:43
 * Name  : SmallEntity
 * Intro : Edit By hjq
 * Version : 1.0
 */
public interface SmallEntity {

    void writeTo(OutputStream os) throws IOException;

    InputStream getContent();

    long getContentLength();

    String getContentEncoding();

}
