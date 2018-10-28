package com.small.tools.network.internal.async;

import com.small.tools.network.internal.interfaces.HTTPRequest;

/**
 * Author: hjq
 * Date  : 2018/10/28 19:44
 * Name  : ThreadScheduler
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class ThreadScheduler implements Scheduler {

    @Override
    public final void setMaxRunningRequests(int number) {

    }

    @Override
    public final int getMaxRunningRequests() {
        return 1;
    }

    @Override
    public final <T extends HTTPRequest> void submit(final T request) {
        if (request == null
                || request.isExecuted()
                || request.isCanceled()
                || request.isFinished()) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (request == null
                        || request.isExecuted()
                        || request.isCanceled()
                        || request.isFinished()) {
                    return;
                }
                request.requestSync();
            }
        }).start();
    }

    @Override
    public final <T extends HTTPRequest> void cancel(T request) {
        if (request != null
                && !request.isCanceled()) {
            request.cancel(true);
        }
    }

    @Override
    public void cancelAll() {

    }

    @Override
    public final <T extends HTTPRequest> void finish(T request) {
        if (request != null
                && !request.isFinished()) {
            request.finish();
        }
    }
}
