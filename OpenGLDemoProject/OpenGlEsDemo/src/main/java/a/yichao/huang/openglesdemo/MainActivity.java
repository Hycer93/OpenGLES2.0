package a.yichao.huang.openglesdemo;

import android.app.ActivityManager;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;

import a.yichao.huang.openglesdemo.util.LogUtil;
import z.huang.yichao.yc_opengleslibs.utils.async.AsyncTaskUtil;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private GLSurfaceView glSurfaceView;
    private final ObjRenderer objRenderer = new ObjRenderer(this);
    private boolean renderSet = false;

    private AsyncTaskUtil asyncTaskUtil;

    private List<String> tgaList;
    private HashMap<String, Bitmap> tgaMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(TAG, "onCreate: " + Thread.currentThread().getId());
        asyncTaskUtil = new AsyncTaskUtil();
//        asyncTaskUtil.summit(loadObj);
//        asyncTaskUtil.summit(loadMtl);
        glSurfaceView = new GLSurfaceView(this);
        if (isSupportGLES2()) {
            glSurfaceView.setEGLContextClientVersion(2);
            glSurfaceView.setRenderer(objRenderer);
            glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
            renderSet = true;
            setContentView(glSurfaceView);
        } else {
            setContentView(R.layout.activity_main);
        }
    }

    private boolean isSupportGLES2() {
        final ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        if (am == null) return false;
        final ConfigurationInfo configurationInfo = am.getDeviceConfigurationInfo();
        return configurationInfo.reqGlEsVersion >= 0x20000
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                && (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (renderSet) {
            glSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (renderSet) {
            glSurfaceView.onPause();
        }
    }
}
