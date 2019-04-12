package pers.asparaw.fakeneteasecloudmusic.test.cachestrategy;

import android.graphics.Bitmap;

/**
 * Created by asparaw on 2019/4/9.
 */
public interface CacheStrategy {
   void put (String name, Bitmap bitmap);
   Bitmap get (String name);
}
