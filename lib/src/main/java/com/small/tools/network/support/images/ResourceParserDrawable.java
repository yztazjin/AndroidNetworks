package com.small.tools.network.support.images;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.small.tools.network.internal.interfaces.StatusCode;
import com.small.tools.network.internal.interfaces.HTTPRequest;
import com.small.tools.network.internal.interfaces.ResourceDataParser;

import java.io.InputStream;

/**
 * Author: hjq
 * Date  : 2018/10/04 11:08
 * Name  : ResourceParserBitmap
 * Intro : Edit By hjq
 * Version : 1.0
 */
class ResourceParserDrawable implements ResourceDataParser<Drawable> {

    Drawable drawable;

    public void parse(Context context, int drawableId) {
        if (context == null
                || !"drawable".equals(context.getResources().getResourceTypeName(drawableId))) {
            throw new UnsupportedOperationException("can only parse drawable resource");
        }

        drawable = context.getResources().getDrawable(drawableId);

        if (drawable == null) {
            throw new NullPointerException("drawable not exist!");
        }

    }

    @Override
    public int parse(HTTPRequest request, InputStream is) {
        return StatusCode.PARSE_SUCCESS;
    }

    @Override
    public void parse(HTTPRequest request, Drawable data) {
        drawable = data;
    }

    @Override
    public Drawable getData() {
        return drawable;
    }

    @Override
    public Class<Drawable> getParseType() {
        return Drawable.class;
    }

    @Override
    public ResourceParserDrawable newInstance() {
        return new ResourceParserDrawable();
    }
}
