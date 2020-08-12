package z.huang.yichao.yc_opengleslibs.utils.async;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import z.huang.yichao.yc_opengleslibs.utils.LogUtil;

public class ThreadBuilder implements ThreadFactory {
    private AtomicInteger threadCounter = new AtomicInteger();

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new NormalThread(runnable);
        thread.setName(thread.getName()+"["+threadCounter.incrementAndGet()+"]");
        LogUtil.d(ThreadBuilder.class.getSimpleName(),thread.getName());
        return thread;
    }

}
