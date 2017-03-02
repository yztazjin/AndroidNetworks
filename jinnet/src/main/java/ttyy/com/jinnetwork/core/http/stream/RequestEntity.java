package ttyy.com.jinnetwork.core.http.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * author: admin
 * date: 2017/03/02
 * version: 0
 * mail: secret
 * desc: RequestEntity
 */

public interface RequestEntity {

    void writeTo(OutputStream os) throws IOException;

    InputStream getContent();

    long getContentLength();

    String getContentEncoding();
}
