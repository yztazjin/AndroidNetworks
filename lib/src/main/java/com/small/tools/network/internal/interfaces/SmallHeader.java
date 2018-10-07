package com.small.tools.network.internal.interfaces;

/**
 * Author: hjq
 * Date  : 2018/10/03 14:04
 * Name  : SmallHeader
 * Intro : Edit By hjq
 * Version : 1.0
 */
public abstract class SmallHeader {

    public SmallHeader() {
    }

    public static abstract class ContentType extends SmallHeader {

        private ContentType() {
        }

        public static final ContentType ApplicationJson = new ContentType() {

            @Override
            public String getValue() {
                return "application/json";
            }
        };

        public static final ContentType FormURLEncoded = new ContentType() {

            @Override
            public String getValue() {
                return "application/x-www-form-urlencoded";
            }
        };

        public static final ContentType MultipartFormdata = new ContentType() {

            @Override
            public String getValue() {
                return "multipart/form-data";
            }
        };

        @Override
        public final String getName() {
            return "Content-Type";
        }
    }


    public abstract String getName();

    public abstract String getValue();

}
