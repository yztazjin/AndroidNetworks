package com.small.tools.network.support.images;

import com.small.tools.network.internal.async.Scheduler;
import com.small.tools.network.internal.async.SchedulerCallable;
import com.small.tools.network.internal.interfaces.HTTPRequest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author: hjq
 * Date  : 2018/10/06 11:08
 * Name  : SchedulerImages
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class SchedulerImages implements Scheduler {

    int nMaxRunningRequests;
    ExecutorService mExecutors;

    Map<Object, HTTPRequest> mAllRequests;

    Stack<HTTPRequest> mRunningRequests;
    Stack<HTTPRequest> mWaitingRequests;

    public SchedulerImages() {
        nMaxRunningRequests = 3;
        mExecutors = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r, "Scheduler#Task");
                        return thread;
                    }
                });

        mRunningRequests = new Stack<>();
        mWaitingRequests = new Stack<>();
        mAllRequests = new HashMap<>();
    }

    @Override
    public void setMaxRunningRequests(int number) {
        nMaxRunningRequests = number;
    }

    @Override
    public int getMaxRunningRequests() {
        return nMaxRunningRequests;
    }

    @Override
    public synchronized void submit(HTTPRequest request) {
        HTTPRequest removedRequest = mAllRequests.remove(request.getToken());
        if (removedRequest != null) {
            removedRequest.cancel(false);
        }

        mAllRequests.put(request.getToken(), request);

        if (mRunningRequests.size() < nMaxRunningRequests) {
            mRunningRequests.add(request);
            mExecutors.submit(new SchedulerCallable(request));
        } else {
            mWaitingRequests.add(request);
        }
    }

    @Override
    public synchronized void cancel(HTTPRequest request) {
        request.cancel(true);
    }

    @Override
    public synchronized void cancelAll() {
        LinkedList<HTTPRequest> runningList = new LinkedList<>(mRunningRequests);
        LinkedList<HTTPRequest> waitingList = new LinkedList<>(mWaitingRequests);

        for (HTTPRequest request : waitingList) {
            request.cancel(true);
        }

        for (HTTPRequest request : runningList) {
            request.cancel(true);
        }

    }

    @Override
    public synchronized void finish(HTTPRequest request) {
        mAllRequests.remove(request.getToken());

        boolean result = mRunningRequests.remove(request);
        if (!result) {
            mWaitingRequests.remove(request);

        } else {

            if (mRunningRequests.size() < nMaxRunningRequests
                    && mWaitingRequests.size() > 0) {
                HTTPRequest next = mWaitingRequests.pop();
                mRunningRequests.add(next);
                mExecutors.submit(new SchedulerCallable(next));
            }
        }

    }

}
