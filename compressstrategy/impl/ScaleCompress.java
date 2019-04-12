package pers.asparaw.fakeneteasecloudmusic.test.compressstrategy.impl;

import android.graphics.Bitmap;

import pers.asparaw.fakeneteasecloudmusic.test.compressstrategy.CompressStrategy;
import pers.asparaw.fakeneteasecloudmusic.test.compressstrategy.option.CompressOption;

/**
 * Created by asparaw on 2019/4/12.
 */
public class ScaleCompress implements CompressStrategy {
    private static class instanceHolder{
        private static final ScaleCompress instance = new ScaleCompress();
    }
    public ScaleCompress(){

    }
    public static ScaleCompress getInstance(){
        return ScaleCompress.instanceHolder.instance;
    }

    @Override
    public Bitmap compress(Bitmap bitmap, CompressOption option) {

            int srcWidth = bitmap.getWidth(),
                    srcHeight = bitmap.getHeight(),
                    outWidth = option.getWidth(),
                    outHeight = option.getHeight();
            float srcRatio = 1f * srcWidth / srcHeight;

            if (outHeight <= 0 && outWidth <= 0) {
                return bitmap;
            } else if (outHeight <= 0) {
                outHeight = (int) (outWidth / srcRatio);
            } else if (outWidth <= 0) {
                outWidth = (int) (outHeight * srcRatio);
            }else {
                float outRatio = 1f * outWidth / outHeight;
                if (outRatio < srcRatio) {
                    outHeight = (int) (outWidth / srcRatio);
                } else if (outRatio > srcRatio) {
                    outWidth = (int) (outHeight * srcRatio);
                }
            }
            return Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, true);

    }
}
