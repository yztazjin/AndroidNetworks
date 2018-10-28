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

    Thread mThread;
    HTTPRequest mRequest;

    @Override
    public final void setMaxRunningRequests(int number) {

    }

    @Override
    public final int getMaxRunningRequests() {
        return 1;
    }

    @Override
    public final <T extends HTTPRequest> void submit(T request) {
        this.mRequest = request;
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (mRequest == null
                        || mRequest.isExecuted()
                        || mRequest.isCanceled()
                        || mRequest.isFinished()) {
                    return;
                }
                mRequest.requestSync();
            }
        });
        mThread.start();
    }

    @Override
    public final <T extends HTTPRequest> void cancel(T request) {
        if (mRequest != null
                && mRequest == request)
            mRequest.cancel(true);
    }

    @Override
    public void cancelAll() {
        if (mRequest != null)
            mRequest.cancel(true);
    }

    @Override
    public final <T extends HTTPRequest> void finish(T request) {
        if (mRequest == null) {
            return;
        }

        if (!mRequest.isFinished()) {
            mRequest.finish();
        }

        this.mRequest = null;
    }
}
