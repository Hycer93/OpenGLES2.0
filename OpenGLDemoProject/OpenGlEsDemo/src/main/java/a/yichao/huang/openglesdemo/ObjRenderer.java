package a.yichao.huang.openglesdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import java.util.HashMap;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import a.yichao.huang.openglesdemo.util.LogUtil;
import a.yichao.huang.openglesdemo.util.TextResourceReader;
import a.yichao.huang.openglesdemo.util.TextureHelper;
import z.huang.yichao.yc_opengleslibs.analyzer.ModelAnalyzer;
import z.huang.yichao.yc_opengleslibs.analyzer.MtlAnalyzer;
import z.huang.yichao.yc_opengleslibs.model.Camera;
import z.huang.yichao.yc_opengleslibs.model.Light;
import z.huang.yichao.yc_opengleslibs.model.Model;
import z.huang.yichao.yc_opengleslibs.model.MtlModel;
import z.huang.yichao.yc_opengleslibs.render.Program;
import z.huang.yichao.yc_opengleslibs.utils.TgaLoader;


public class ObjRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "ObjRenderer";
    private final float[] projectionMatrix = new float[16];//投影矩阵
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    private Context context;
    private Program program;
    private volatile boolean isVertexReady = false;
    private volatile boolean isTextureReady = false;
    private Camera camera;
    private Light light;
    private Model model;
    private MtlModel mtlModel;
    private List<String> tgaList;

    private float angle = 0;


    public ObjRenderer(Context c) {
        this.context = c;
        camera = new Camera();
        light = new Light();
    }

    private void loadObj() {
        LogUtil.d(TAG, "loadObj()");
        ModelAnalyzer objAnalyzer = new ModelAnalyzer();
        objAnalyzer.analyzeObj(
                TextResourceReader.readBufferFormAsset(context, "Huracan.obj"));
        model = objAnalyzer.getModel();
        model.setProgram(program);
        isVertexReady = true;
    }

    private void loadMtl() {
        LogUtil.d(TAG, "loadMtl()");
        MtlAnalyzer mtlAnalyzer = new MtlAnalyzer();
        mtlAnalyzer.analyzeMtl("Huracan",
                TextResourceReader.readBufferFormAsset(context, "Huracan.mtl"));
        mtlModel = mtlAnalyzer.getResult();
        tgaList = mtlAnalyzer.getTgaList();
        mtlAnalyzer.release();
        mtlModel.setTextureIds(loadTga());
        model.setMtlModel(mtlModel);
        isTextureReady = true;
    }

    private HashMap<String, Integer> loadTga() {
        HashMap<String, Bitmap> tgaMap = new HashMap<>();
        for (String tga : tgaList) {
            long startTime = SystemClock.currentThreadTimeMillis();
            Bitmap b = TgaLoader.loadTga(TextResourceReader.readByteFormAsset(
                    context, tga));
            LogUtil.d(TAG, " load " + tga + " over duration: " + (SystemClock.currentThreadTimeMillis() - startTime) / 1000f);
            tgaMap.put(tga, b);
        }
        return TextureHelper.loadTexture(tgaMap);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        LogUtil.d(TAG, "onSurfaceCreated: " + Thread.currentThread().getName());
        GLES20.glClearColor(1f, 1f, 1f, 1f);
//        // 设置为打开背面剪裁
//        GLES20.glEnable(GLES20.GL_CULL_FACE);
        // Enable depth testing for hidden-surface elimination.
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        // Enable blending for combining colors when there is transparency
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_SRC_COLOR);
        GLES20.glBlendEquation(GLES20.GL_FUNC_ADD);
        String vertexShader = TextResourceReader.readTextFormAsset(
                context, "texture_vertex_shader.glsl");
        String fragmentShader = TextResourceReader.readTextFormAsset(
                context, "texture_fragment_shader.glsl");
        program = new Program("car",vertexShader,fragmentShader);

        LogUtil.d(TAG, "loadObj: " + Thread.currentThread().getName());
        long startTime = SystemClock.currentThreadTimeMillis();
        loadObj();
        loadMtl();
        LogUtil.d(TAG, "loadObj over duration: " + (SystemClock.currentThreadTimeMillis() - startTime) / 1000f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        //设置透视视图
        Matrix.frustumM(
                projectionMatrix,
                0,
                -ratio,
                ratio,
                -1f,
                1f,
                2f,
                1000f);
        //设置摄像头
        camera.initCamera(0f, 120f, 250f, 0f, 0f, 0f);
        //设置光源
        light.initLight(-50,100,100);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        if (isVertexReady && isTextureReady){
            Matrix.multiplyMM(viewProjectionMatrix, 0,
                    projectionMatrix, 0, camera.getViewMatrix(), 0);
            Matrix.multiplyMM(modelViewProjectionMatrix, 0,
                    viewProjectionMatrix, 0, model.getModelMatrix(), 0);
            GLES20.glUniformMatrix4fv(model.getProgram().uMatrix, 1, false,
                    modelViewProjectionMatrix, 0);
            GLES20.glUniform3fv(model.getProgram().uLight,1, light.lightBuffer);
            GLES20.glUniform3fv(model.getProgram().uCamera,1,camera.cameraBuffer);
            Matrix.setIdentityM(model.getModelMatrix(), 0);
            Matrix.rotateM(model.getModelMatrix(), 0, -90f, 1f, 0f, 0f);
            Matrix.rotateM(model.getModelMatrix(), 0, angle, 0f, 0f, 1f);
            angle+=0.5f;
            if (angle == 360) {
                angle = 0;
            }
            model.draw();
        }
    }
}
