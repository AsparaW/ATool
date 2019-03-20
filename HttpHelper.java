package com.mantouland.fakezhihuribao.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.util.Log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpHelper extends Thread {
    private String url;
    private static final String ERROR_MESSAGE = "http error response:";
    private static final int TYPE_NORMAL =1;
    private static final int TYPE_BITMAP=2;

    private int type;

    @Override
    public void run(){
        switch (type){
            case TYPE_BITMAP:
                picRunner();
                break;
            case  TYPE_NORMAL:
                URLRunner();
                break;
        }
    }

    /***
     *
     * @param url
     * @param type str=1 pic=2
     */
    public void setHelper(String url,int type) {
        this.url=url;
        this.type=type;
    }


    private String URLRunner() {
        String temp="";
        URL u;
        HttpURLConnection httpURLConnection;
        BufferedReader bf;
        String readLine;
        try {
            u = new URL(url);
            Log.d("CONNECT", "URLRunner: "+url);
            httpURLConnection = (HttpURLConnection) u.openConnection();
            int responce = httpURLConnection.getResponseCode();
            if (responce == 200) {
                bf = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                while ((readLine = bf.readLine()) != null) {
                    temp = temp + readLine + "\r\n";
                }
                return temp;
            } else {
                return ERROR_MESSAGE+responce;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

private Bitmap bitmap;
        private Bitmap picRunner() {
            try {
                Log.d("PPIC", "picRunner: "+url);
                URL u=new URL(url);
                InputStream inputStream=u.openStream();
                bitmap=BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                return bitmap;

            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
}
