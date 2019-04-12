package pers.asparaw.fakeneteasecloudmusic.test.cachestrategy.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import pers.asparaw.fakeneteasecloudmusic.test.cachestrategy.CacheStrategy;

/**
 * Created by asparaw on 2019/4/9.
 */
public class StorCache implements CacheStrategy {
    private String dir ;

    private static class instanceHolder{
        private static final StorCache instance = new StorCache();
    }
    private StorCache(){

    }

    public static StorCache getInstance(){
        return instanceHolder.instance;
    }
    @Override
    public void put(String name, Bitmap bitmap) {
        if (dir == null)
            return;
        FileOutputStream fos = null;
        try {
            File file=new File(dir,name);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()&&!parentFile.mkdirs()){
                return;
            }
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public Bitmap get(String name) {
        return BitmapFactory.decodeFile(dir+name);
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir  = dir.endsWith("/") ?  dir : dir+"/";
    }
}
