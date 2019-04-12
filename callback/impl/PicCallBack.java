package pers.asparaw.fakeneteasecloudmusic.test.callback.impl;

import android.graphics.Bitmap;

import pers.asparaw.fakeneteasecloudmusic.test.callback.UniCallBack;

/**
 * Created by asparaw on 2019/4/3.
 */
public abstract class PicCallBack extends UniCallBack {
    public abstract void onSuccess(Bitmap bitmap);
    public abstract void onFail(Exception e);
}
