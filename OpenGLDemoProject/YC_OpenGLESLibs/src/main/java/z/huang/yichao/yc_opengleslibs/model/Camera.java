package z.huang.yichao.yc_opengleslibs.model;

import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import z.huang.yichao.yc_opengleslibs.define.Constants;

public class Camera {

    private final float[] viewMatrix = new float[16];//视图矩阵（Camera）
    public FloatBuffer cameraBuffer;

    public Camera() {

    }

    public void initCamera(float eyeX, float eyeY, float eyeZ, float centerX, float centerY,
                           float centerZ) {
        //设置摄像头
        Matrix.setLookAtM(viewMatrix, 0,
                eyeX, eyeY, eyeZ,
                centerX, centerY, centerZ,
                0f, 1f, 0f);
        float[] cameraLocation = new float[3];//摄像机位置
        cameraLocation[0] = eyeX;
        cameraLocation[1] = eyeY;
        cameraLocation[2] = eyeZ;
        ByteBuffer bf = ByteBuffer.allocateDirect(cameraLocation.length * Constants.BYTES_PER_FLOAT);
        bf.order(ByteOrder.nativeOrder());
        cameraBuffer = bf.asFloatBuffer();
        cameraBuffer.put(cameraLocation);
        cameraBuffer.position(0);
    }

    public float[] getViewMatrix() {
        return viewMatrix;
    }
}
