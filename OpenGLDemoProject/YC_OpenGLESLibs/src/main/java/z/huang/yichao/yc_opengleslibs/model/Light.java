package z.huang.yichao.yc_opengleslibs.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import z.huang.yichao.yc_opengleslibs.define.Constants;

public class Light {
    public FloatBuffer lightBuffer;

    public void initLight(float x, float y, float z){
        float[] lightLocation = new float[3];
        lightLocation[0] = x;
        lightLocation[1] = y;
        lightLocation[2] = z;
        ByteBuffer bf = ByteBuffer.allocateDirect(lightLocation.length * Constants.BYTES_PER_FLOAT);
        bf.order(ByteOrder.nativeOrder());
        lightBuffer = bf.asFloatBuffer();
        lightBuffer.put(lightLocation);
        lightBuffer.position(0);
    }
}
