package a.yichao.huang.openglesdemo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.util.HashMap;
import java.util.Set;

public class TextureHelper {
    private static final String TAG = "TextureHelper";

    public static int loadTexture(Context context, int resourceId) {
        final int[] textureObjectIds = new int[1];
        //生成n个纹理名称ID
        GLES20.glGenTextures(textureObjectIds.length, textureObjectIds, 0);
        if (textureObjectIds[0] == 0) {
            LogUtil.w(TAG, "Could not generate a new OpenGL texture object.");
            return 0;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                resourceId, options);
        if (bitmap == null) {
            LogUtil.w(TAG, "Resource ID " + resourceId + " could not be decoded.");
            GLES20.glDeleteTextures(1, textureObjectIds, 0);
            return 0;
        }
        //绑定的纹理对象
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0]);
        //缩小使用三线性过滤
        GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        //放大使用双线性过滤
        GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        //通知OpenGL加载bitmap数据，并将其复制到当前绑定的纹理对象中
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        //通知OpenGL生成所有可能用到的不同等级纹理
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        //解绑纹理对象
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        return textureObjectIds[0];
    }

    public static int loadTexture(Bitmap bitmap) {
        final int[] textureObjectIds = new int[1];
        //生成n个纹理名称ID
        GLES20.glGenTextures(textureObjectIds.length, textureObjectIds, 0);
        if (textureObjectIds[0] == 0) {
            LogUtil.w(TAG, "Could not generate a new OpenGL texture object.");
            return 0;
        }
        //绑定的纹理对象
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0]);
        //缩小使用三线性过滤
        GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        //放大使用双线性过滤
        GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        //通知OpenGL加载bitmap数据，并将其复制到当前绑定的纹理对象中
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        //通知OpenGL生成所有可能用到的不同等级纹理
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        //解绑纹理对象
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        return textureObjectIds[0];
    }

    public static HashMap<String,Integer> loadTexture(HashMap<String,Bitmap> bitmapHashMap) {
        final int[] textureObjectIds = new int[bitmapHashMap.size()];
        //生成n个纹理名称ID
        GLES20.glGenTextures(textureObjectIds.length, textureObjectIds, 0);
        final HashMap<String,Integer> results = new HashMap<>();
        LogUtil.w(TAG, "bitmapHashMap size ["+bitmapHashMap.size()+"]");
        Set<String> keys = bitmapHashMap.keySet();
        LogUtil.w(TAG, "keys size ["+keys.size()+"]");
        int index = 0;
        for (String key : keys) {
            if (textureObjectIds[index] == 0) {
                LogUtil.w(TAG, "Could not generate a new OpenGL texture["+index+"] object.");
                index++;
                continue;
            }
            LogUtil.w(TAG, "create texture ["+key+"] - id["+textureObjectIds[index]+"]");
            int textureObjectId = textureObjectIds[index];
            //激活纹理单元
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0+(textureObjectIds[index] - 1));
            //绑定的纹理对象
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectId);
            //缩小使用三线性过滤
            GLES20.glTexParameteri(
                    GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
            //放大使用双线性过滤
            GLES20.glTexParameteri(
                    GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            //通知OpenGL加载bitmap数据，并将其复制到当前绑定的纹理对象中
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmapHashMap.get(key), 0);
            //通知OpenGL生成所有可能用到的不同等级纹理
            GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
            //解绑纹理对象
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
            results.put(key,textureObjectId);
            index++;
        }
        return results;
    }
}
