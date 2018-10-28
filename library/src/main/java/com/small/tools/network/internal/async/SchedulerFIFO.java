package com.small.tools.network.internal.async;

import com.small.tools.network.internal.interfaces.HTTPRequest;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author: hjq
 * Date  : 2018/10/03 22:39
 * Name  : SchedulerFIFO
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class SchedulerFIFO implements Scheduler {

    int nMaxRunningRequests;
    ExecutorService mExecutors;

    Deque<HTTPRequest> mRunningRequests;
    Deque<HTTPRequest> mWaitingRequests;

    public SchedulerFIFO() {
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

        mRunningRequests = new ArrayDeque<>();
        mWaitingRequests = new ArrayDeque<>();
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
    public synchronized void submit(final HTTPRequest request) {
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

        boolean result = mRunningRequests.remove(request);
        if (!result) {
            result = mWaitingRequests.remove(request);

            if (result && !request.isFinished()) {
                request.finish();
            }

        } else {

            if (!request.isFinished()) {
                request.finish();
            }

            if (mRunningRequests.size() < nMaxRunningRequests
                    && mWaitingRequests.size() > 0) {
                HTTPRequest next = mWaitingRequests.pop();
                mRunningRequests.add(next);
                mExecutors.submit(new SchedulerCallable(next));
            }

        }
    }
}
