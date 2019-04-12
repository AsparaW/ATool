package pers.asparaw.fakeneteasecloudmusic.test.compressstrategy.option;

import android.widget.ImageView;

/**
 * Created by asparaw on 2019/4/9.
 */
public class CompressOption {
    private static final int DEFAULT = -1;
    private int width=DEFAULT;
    private int height=DEFAULT;
    private float ratio=0.5f;

    private float quality=0.85f;

    private int maxSize = DEFAULT;

    public ImageView.ScaleType scaleType= ImageView.ScaleType.CENTER_CROP;

    public ImageView.ScaleType getScaleType() {
        return scaleType;
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        this.scaleType = scaleType;
    }

    public int getWidth() {
        return width;
    }

    public static int getDEFAULT() {
        return DEFAULT;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public void setQuality(float quality) {
        this.quality = quality;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public float getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }


    @Override
    public String toString() {
        return "compression option  width:"
                +width +
                "height:"+height
                +"quality:"+quality
                +"ratio"+ratio;
    }
}
