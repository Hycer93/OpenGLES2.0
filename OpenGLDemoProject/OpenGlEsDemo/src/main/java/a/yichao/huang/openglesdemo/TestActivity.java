package a.yichao.huang.openglesdemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import a.yichao.huang.openglesdemo.util.LogUtil;
import a.yichao.huang.openglesdemo.util.TextResourceReader;
import z.huang.yichao.yc_opengleslibs.utils.TgaLoader;


public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TgaLoader";
    ImageView imageView;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(TgaLoader.loadTga(TextResourceReader.readByteFormAsset(
                this,
                "goodyear_sidewall.tga"
        )));
        LogUtil.d(TAG, "onCreate: ");
    }
}
