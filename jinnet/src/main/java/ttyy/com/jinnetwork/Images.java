package ttyy.com.jinnetwork;

import ttyy.com.jinnetwork.ext_image.HTTPRequestImageBuilder;
import ttyy.com.jinnetwork.ext_image.ImageRequestBuilder;

/**
 * Author: hjq
 * Date  : 2017/03/03 21:10
 * Name  : Images
 * Intro : Edit By hjq
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2017/03/03    hjq   1.0              1.0
 */
public class Images {

    private Images(){

    }

    public static ImageRequestBuilder get(){
        return new HTTPRequestImageBuilder();
    }

}
