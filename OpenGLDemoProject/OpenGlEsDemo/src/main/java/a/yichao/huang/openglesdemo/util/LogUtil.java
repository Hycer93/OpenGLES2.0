package a.yichao.huang.openglesdemo.util;
import android.util.Log;

/**
 * Log统一管理类
 */
public class LogUtil {

    private LogUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static final boolean DEBUG = true;
    private static final String TAG = "HYC->";

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (DEBUG) Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (DEBUG) Log.d(TAG, msg);
    }

    public static void w(String msg) {
        if (DEBUG) Log.w(TAG, msg);
    }

    public static void e(String msg) {
        if (DEBUG) Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (DEBUG) Log.v(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (DEBUG) Log.i(TAG + tag, msg);
    }

    public static void d(String tag, String msg) {
        if (DEBUG) Log.d(TAG + tag, msg);
    }

    public static void w(String tag, String msg) {
        if (DEBUG) Log.w(TAG + tag, msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUG) Log.e(TAG + tag, msg);
    }

    public static void v(String tag, String msg) {
        if (DEBUG) Log.v(TAG+ tag, msg);
    }
}
