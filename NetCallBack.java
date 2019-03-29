package pers.asparaw.fakeneteasecloudmusic.test;

/**
 * Created by asparaw on 2019/3/29.
 */
public interface NetCallBack {
    void onFail(Exception e);
    void onSuccess(HTTPHelper.HTTPContent httpContent);
}
