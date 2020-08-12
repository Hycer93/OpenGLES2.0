package z.huang.yichao.yc_opengleslibs.utils.async;

public class NormalThread extends Thread{

    public NormalThread(Runnable runnable) {
        super(runnable);
        setName(NormalThread.class.getSimpleName());
    }
}
