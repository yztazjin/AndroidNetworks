package ttyy.com.jinnetwork.core.async;

import android.os.Process;

import java.util.LinkedList;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import ttyy.com.jinnetwork.core.config.__Log;
import ttyy.com.jinnetwork.core.work.HTTPRequest;
import ttyy.com.jinnetwork.core.work.HTTPResponse;

/**
 * author: admin
 * date: 2017/02/28
 * version: 0
 * mail: secret
 * desc: $HttpAsyncExecutor
 */

public class $HttpAsyncExecutor implements HttpExecutor{

    protected BlockingDeque<HTTPRequest> mWorkers;
    protected LinkedList<HTTPRequest> mWaitingWorkers;

    protected int mBlockingDequeSize;

    protected LoopingThread mLoopingThread;

    protected boolean isStop = true;

    protected QueueMode mQueueMode;

    protected $HttpAsyncExecutor(int size, QueueMode mode){
        mBlockingDequeSize = size;
        mWorkers = new LinkedBlockingDeque<>(size);
        mWaitingWorkers = new LinkedList<>();
        mQueueMode  = mode == null ? QueueMode.FirstInFirstOut : mode;
    }

    @Override
    public $HttpAsyncExecutor addRequest(HTTPRequest request){
        if(mWorkers.size() == mBlockingDequeSize){

            // 暂时只在排队中做此处理
            if(request.isEnableRemoveSameRequest()){
                // 移除相同的请求 以最后一次为准
                boolean success = mWaitingWorkers.remove(request);
            }

            switch (mQueueMode){
                case FirstInFirstOut:
                    mWaitingWorkers.add(request);
                    break;
                case LastInFirstOut:
                    mWaitingWorkers.addFirst(request);
                    break;
            }

        }else {
            switch (mQueueMode){
                case FirstInFirstOut:
                    mWorkers.add(request);
                    break;
                case LastInFirstOut:
                    mWorkers.addFirst(request);
                    break;
            }
        }
        return this;
    }

    public void moveWaitingWorkerToDeque(){
        while (mWaitingWorkers.size() > 0){
            HTTPRequest request = mWaitingWorkers.poll();
            if(!request.isCanceled()){
                addRequest(request);
                break;
            }
        }
    }

    @Override
    public void start(){
        if(isStop
                || mLoopingThread == null){
            mLoopingThread = new LoopingThread();
            isStop = false;
            mLoopingThread.start();
        }
    }

    class LoopingThread extends Thread{

        @Override
        public void run() {
            super.run();
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            while (true){
                try {
                    HTTPRequest request = mWorkers.take();

                    if(!request.isCanceled()){
                        HTTPResponse response = request.request();
                    }else {
                        __Log.w("AsyncExecutor", "Request Is Canceled !");
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    isStop = true;
                    break;
                } finally {
                    if(mWorkers.isEmpty()){
                        moveWaitingWorkerToDeque();
                    }
                }
            }
        }
    }
}
