package z.huang.yichao.yc_opengleslibs.utils.async;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyncTaskUtil {
    private static final String TAG = "AsyncTaskUtil";
    private ThreadPoolExecutor threadPoolExecutor;
    private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    private static final int MAX_SIZE = (Runtime.getRuntime().availableProcessors() * 2)+1;
    private static final long KEEP_LIVE_TIME = 1;
    private static final TimeUnit TIME_UNIT = TimeUnit.HOURS;
    private BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(100);
    private ThreadFactory factory = new ThreadBuilder();
    private RejectedExecutionHandler rejectedHandler = new RejectedHandler();

    public AsyncTaskUtil() {
        threadPoolExecutor = new ThreadPoolExecutor(
                POOL_SIZE,
                MAX_SIZE,
                KEEP_LIVE_TIME,
                TIME_UNIT,
                workQueue,
                factory,
                rejectedHandler);
        threadPoolExecutor.prestartCoreThread();
    }

    public void summit(Runnable runnable){
        threadPoolExecutor.submit(runnable);
    }

    public void execute(Runnable runnable){
        threadPoolExecutor.execute(runnable);
    }

    public void release(){
        class ShutDownThread extends Thread{
            @Override
            public void run() {
                super.run();
                threadPoolExecutor.shutdownNow();
            }
        }
        Thread t = new ShutDownThread();
        t.start();
        try {
            t.join(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
