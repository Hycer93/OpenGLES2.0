package z.huang.yichao.yc_opengleslibs.utils;


import android.graphics.Bitmap;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TgaLoader {
    private static final String TAG = "TgaLoader";
    private static int offset;

    public static Bitmap loadTga(InputStream is) {
        try {
            byte[] result = new byte[0];
            byte[] bytes = new byte[1024];
            BufferedInputStream bis = new BufferedInputStream(is);
            int n;
            try {
                while ((n = bis.read(bytes)) != -1) {
                    if (n < bytes.length) {
                        byte[] lastBytes = new byte[n];
                        System.arraycopy(bytes, 0, lastBytes, 0, n);
                        result = byteMerger(result,lastBytes);
                    } else {
                        result = byteMerger(result,bytes);
                    }
                }
                return decode(result);
            } finally {
                is.close();
                bis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] byteMerger(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

    private static Bitmap decode(byte[] buf) throws IOException {
        offset = 0;
        // Reading header bytes
        // buf[2]=image type code 0x02=uncompressed BGR or BGRA
        // buf[12]+[13]=width
        // buf[14]+[15]=height
        // buf[16]=image pixel size 0x20=32bit, 0x18=24bit
        // buf{17]=Image Descriptor Byte=0x28 (00101000)=32bit/origin
        //         upperleft/non-interleaved
        for (int i = 0; i < 12; i++)
            read(buf);
        int width = read(buf) + (read(buf) << 8);   // 00,04=1024
        int height = read(buf) + (read(buf) << 8);  // 40,02=576
        read(buf);
        read(buf);

        int n = width * height;
        int[] pixels = new int[n];
        int idx = 0;

        if (buf[2] == 0x02 && buf[16] == 0x20) { // uncompressed BGRA
            while (n > 0) {
                int b = read(buf);
                int g = read(buf);
                int r = read(buf);
                int a = read(buf);
                int v = (a << 24) | (r << 16) | (g << 8) | b;
                pixels[idx++] = v;
                n -= 1;
            }
        } else if (buf[2] == 0x02 && buf[16] == 0x18) {  // uncompressed BGR
            while (n > 0) {
                int b = read(buf);
                int g = read(buf);
                int r = read(buf);
                int a = 255; // opaque pixel
                int v = (a << 24) | (r << 16) | (g << 8) | b;
                pixels[idx++] = v;
                n -= 1;
            }
        } else {
            // RLE compressed
            while (n > 0) {
                int nb = read(buf); // num of pixels
                if ((nb & 0x80) == 0) { // 0x80=dec 128, bits 10000000
                    for (int i = 0; i <= nb; i++) {
                        int b = read(buf);
                        int g = read(buf);
                        int r = read(buf);
                        pixels[idx++] = 0xff000000 | (r << 16) | (g << 8) | b;
                    }
                } else {
                    nb &= 0x7f;
                    int b = read(buf);
                    int g = read(buf);
                    int r = read(buf);
                    int v = 0xff000000 | (r << 16) | (g << 8) | b;
                    for (int i = 0; i <= nb; i++)
                        pixels[idx++] = v;
                }
                n -= nb + 1;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static int byteToInt(byte b) {
        int a = b;
        return (a < 0 ? 256 + a : a);
    }

    private static int read(byte[] buf) {
        return byteToInt(buf[offset++]);
    }
}
