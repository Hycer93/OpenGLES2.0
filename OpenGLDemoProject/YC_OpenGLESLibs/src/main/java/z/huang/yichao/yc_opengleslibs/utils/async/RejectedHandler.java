package z.huang.yichao.yc_opengleslibs.utils.async;

import java.util.ArrayList;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import z.huang.yichao.yc_opengleslibs.utils.LogUtil;

public class RejectedHandler implements RejectedExecutionHandler {
    public ArrayList<Runnable> tmpList = new ArrayList<>();

    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
        tmpList.add(runnable);
        LogUtil.d(RejectedHandler.class.getSimpleName(),"rejected size - "+tmpList.size());
    }
}
