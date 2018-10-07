package com.small.tools.network.internal.async;

import com.small.tools.network.internal.interfaces.HTTPRequest;

/**
 * Author: hjq
 * Date  : 2018/10/03 21:41
 * Name  : Scheduler
 * Intro : Edit By hjq
 * Version : 1.0
 */
public interface Scheduler {

    void setMaxRunningRequests(int number);

    int getMaxRunningRequests();

    <T extends HTTPRequest> void submit(T request);

    <T extends HTTPRequest> void cancel(T request);

    void cancelAll();

    <T extends HTTPRequest> void finish(T request);

}
