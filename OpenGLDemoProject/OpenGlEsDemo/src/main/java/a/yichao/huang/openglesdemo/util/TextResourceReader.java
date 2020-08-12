package a.yichao.huang.openglesdemo.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextResourceReader {
    private static final String TAG = "TextResourceReader";

    public static String readTextFormResource(Context context, int resourceId) {
        StringBuilder body = new StringBuilder();
        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                body.append(temp);
                body.append('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not open resource: " + resourceId, e);
        } catch (Resources.NotFoundException nfe) {
            throw new RuntimeException("Resource not found: " + resourceId, nfe);
        }
        return body.toString();
    }

    public static String readTextFormAsset(Context context, String assetsSourceName) {
        StringBuilder body = new StringBuilder();
        try {
            AssetManager assetManager = context.getResources().getAssets();
            InputStream is = assetManager.open(assetsSourceName);
            InputStreamReader isReader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(isReader);
            String temps;
            while ((temps = bufferedReader.readLine()) != null) {
                body.append(temps);
                body.append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body.toString();
    }

    public static BufferedReader readBufferFormAsset(Context context, String assetsSourceName) {
        try {
            AssetManager assetManager = context.getResources().getAssets();
            InputStream is = assetManager.open(assetsSourceName);
            InputStreamReader isReader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(isReader);
            return bufferedReader;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream readByteFormAsset(Context context, String assetsSourceName) {
        try {
            AssetManager assetManager = context.getResources().getAssets();
            InputStream is = assetManager.open(assetsSourceName);
            return is;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
