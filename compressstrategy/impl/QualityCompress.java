package pers.asparaw.fakeneteasecloudmusic.test.compressstrategy.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import pers.asparaw.fakeneteasecloudmusic.test.compressstrategy.CompressStrategy;
import pers.asparaw.fakeneteasecloudmusic.test.compressstrategy.option.CompressOption;

/**
 * Created by asparaw on 2019/4/12.
 */
public class QualityCompress implements CompressStrategy {
    @Override
    public Bitmap compress(Bitmap bitmap, CompressOption option) {
        int quality = 85;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,quality,byteArrayOutputStream);
        ByteArrayInputStream byteArrayInputStream= new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        bitmap=BitmapFactory.decodeStream(byteArrayInputStream);
        while (byteArrayOutputStream.toByteArray().length > option.getMaxSize()&& quality>10 ) {
            quality -= 5;
            byteArrayOutputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, byteArrayOutputStream);
        }
        try {
            byteArrayOutputStream.close();
            byteArrayInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
