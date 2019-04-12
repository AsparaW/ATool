package pers.asparaw.fakeneteasecloudmusic.test.cachestrategy.impl;

import android.graphics.Bitmap;
import android.util.LruCache;

import pers.asparaw.fakeneteasecloudmusic.test.cachestrategy.CacheStrategy;

/**
 * Created by asparaw on 2019/4/9.
 */
public class MemCache implements CacheStrategy {

    private LruCache<String, Bitmap> mMemoryCache;


    private static class instanceHolder{
        private static final MemCache instance = new MemCache();
    }
    private MemCache(){
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024),
                cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }
    public static MemCache getInstance(){
        return instanceHolder.instance;
    }

    @Override
    public void put(String name, Bitmap bitmap) {
        if (mMemoryCache.get(name) != null) {
            mMemoryCache.put(name,bitmap);
        }
    }

    @Override
    public Bitmap get(String name) {
        return mMemoryCache.get(name);
    }
}
