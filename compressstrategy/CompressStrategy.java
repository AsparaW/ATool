package pers.asparaw.fakeneteasecloudmusic.test.compressstrategy;

import android.graphics.Bitmap;

import pers.asparaw.fakeneteasecloudmusic.test.compressstrategy.option.CompressOption;

/**
 * Created by asparaw on 2019/4/9.
 */
public interface CompressStrategy {
    Bitmap compress(Bitmap bitmap, CompressOption option);
}
