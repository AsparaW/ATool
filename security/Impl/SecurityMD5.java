package pers.asparaw.fakeneteasecloudmusic.test.security.Impl;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import pers.asparaw.fakeneteasecloudmusic.test.security.Security;


/***
 * String SecurityMD5
 * @author asparaw
 * @version 1
 */
public class SecurityMD5 implements Security {


    private static class instanceHolder{
        private static final SecurityMD5 instance =new SecurityMD5();
    }
    private SecurityMD5(){

    }
    public static SecurityMD5 getInstance(){
        return instanceHolder.instance;
    }

    /***
     * pass in the string return the md5
     * @param string
     * @return
     */
    public String encode(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 ;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
