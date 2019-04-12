package pers.asparaw.fakeneteasecloudmusic.test.callback.impl;

import pers.asparaw.fakeneteasecloudmusic.test.HTTPHelper;
import pers.asparaw.fakeneteasecloudmusic.test.callback.UniCallBack;

/**
 * Created by asparaw on 2019/3/29.
 */
public abstract class NetCallBack extends UniCallBack {

    public abstract void onSuccess(HTTPHelper.HTTPContent httpContent);
}
