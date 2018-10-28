package com.small.tools.network.internal.async;

import com.small.tools.network.internal.interfaces.HTTPRequest;

import java.util.concurrent.Callable;

/**
 * Author: hjq
 * Date  : 2018/10/06 08:48
 * Name  : SchedulerCallable
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class SchedulerCallable implements Callable<Object> {

    HTTPRequest request;

    public SchedulerCallable(HTTPRequest request) {
        this.request = request;
    }

    @Override
    public Object call() throws Exception {
        if (request == null
                || request.isExecuted()
                || request.isCanceled()
                || request.isFinished()) {
            return null;
        }
        return request.requestSync();
    }
}
