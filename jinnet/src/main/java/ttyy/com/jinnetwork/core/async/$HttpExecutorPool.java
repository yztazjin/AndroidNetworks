package ttyy.com.jinnetwork.core.async;

/**
 * author: admin
 * date: 2017/02/27
 * version: 0
 * mail: secret
 * desc: $HttpExecutorPool
 */

public class $HttpExecutorPool {

    $HttpAsyncExecutor mDFTExecutor;

    private $HttpExecutorPool(){
        mDFTExecutor = new $HttpAsyncExecutor(50);
    }

    static class Holder{
        static $HttpExecutorPool INSTANCE = new $HttpExecutorPool();
    }

    public static $HttpExecutorPool get(){
        return Holder.INSTANCE;
    }

    public $HttpAsyncExecutor getExecutor(String token){
        return null;
    }

    public $HttpAsyncExecutor getDefaultExecutor(){
        return mDFTExecutor;
    }

}
