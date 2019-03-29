package pers.asparaw.fakeneteasecloudmusic.test;

/**
 * Created by asparaw on 2019/3/27.
 */




import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;

import pers.asparaw.fakeneteasecloudmusic.test.impl.JsonCallBack;
import pers.asparaw.fakeneteasecloudmusic.test.impl.NetCallBack;
import pers.asparaw.fakeneteasecloudmusic.test.impl.StringCallBack;

public class HTTPHelper {

    private static final String ERROR_MESSAGE = "http error response:";
    private static final int DEFAULT=0;
    private static final int DEFAULT_WITH_LINES=1;
    private static final String _POST="POST";
    private static final String _GET="GET";
    private static String defaultContentEncoding ;

    private HTTPHelper(){
        defaultContentEncoding = Charset.defaultCharset().name();
        //do nothing
    }

    private static class instanceHolder{
        private static final HTTPHelper instance=new HTTPHelper();
    }


    public static HTTPHelper getInstance(){
        return instanceHolder.instance;
    }


    private void doJsonString(final String urlString, final Map<String,String> parameters, final Map<String,String> properties, final boolean withLine, final JsonCallBack callBack, final Class clazz){
        AExecutorPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HTTPContent httpContent =send(urlString,_POST,parameters,properties,withLine);
                    String string=httpContent.getContent();
                    Object beanObject=Ason.fromJson(string,clazz);
                    callBack.onSuccess(beanObject);
                } catch (IOException e) {
                    callBack.onFail(e);
                }
            }
        });
    }

    private void doString(final String urlString, final Map<String,String> parameters, final Map<String,String> properties, final boolean withLine,final StringCallBack callBack){
        AExecutorPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HTTPContent httpContent =send(urlString,_POST,parameters,properties,withLine);
                    String string=httpContent.getContent();
                    callBack.onSuccess(string);
                } catch (IOException e) {
                    callBack.onFail(e);
                }
            }
        });
    }
    private void doGet(final String urlString, final Map<String,String> parameters, final Map<String,String> properties, final boolean withLine,final NetCallBack callBack){
        AExecutorPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HTTPContent httpContent =send(urlString,_GET,parameters,properties,withLine);
                    callBack.onSuccess(httpContent);
                } catch (IOException e) {
                    callBack.onFail(e);
                }
            }
        });
    }

    private void doPost(final String urlString, final Map<String,String> parameters, final Map<String,String> properties, final boolean withLine,final NetCallBack callBack){
            AExecutorPool.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        HTTPContent httpContent =send(urlString,_POST,parameters,properties,withLine);
                        callBack.onSuccess(httpContent);
                    } catch (IOException e) {
                        callBack.onFail(e);
                    }
                }
            });
    }
    private HTTPContent send(String urlString, String type, Map<String,String> parameters, Map<String,String> properties,boolean withLine)throws IOException{
        HttpURLConnection httpURLConnection;
        /***
         * inbox get paras
         */
        if (type.equalsIgnoreCase(_GET)&&parameters!=null){
            if (!parameters.isEmpty()){
                StringBuilder tempUrlString=new StringBuilder(urlString);
                urlString= tempUrlString.append("?").append(paramBuilder(parameters)).toString();
            }
        }
        /***
         * set http request body
         */
        URL url=new URL(urlString);
        if(urlString.contains("https"))
            httpURLConnection = (HttpsURLConnection) url.openConnection();
        else
            httpURLConnection = (HttpURLConnection) url.openConnection();

        httpURLConnection.setConnectTimeout(5000);
        httpURLConnection.setReadTimeout(5000);

        httpURLConnection.setRequestMethod(type);
        if (properties!=null){
            for (String key:properties.keySet()){
                httpURLConnection.addRequestProperty(key,properties.get(key));
            }
        }
        /***
         * inbox post paras
         */
        if (type.equalsIgnoreCase(_POST) && parameters != null) {
            httpURLConnection.setDoInput(true);//make post-able
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.getOutputStream().write(paramBuilder(parameters).getBytes());
            httpURLConnection.getOutputStream().flush();
            httpURLConnection.getOutputStream().close();
        }
        return makeContent(urlString,httpURLConnection,withLine);
    }

    private String paramBuilder(Map<String,String> parameters){
        StringBuilder param = new StringBuilder();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            param.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        param.deleteCharAt(param.length()-1);
        return param.toString();
    }
    private HTTPContent makeContent(String urlString, HttpURLConnection httpURLConnection, boolean withLine){
        HTTPContent httpContent =new HTTPContent();
        BufferedReader bf;
        String readLine;
        boolean fail=false;
        try {
            StringBuffer temp = new StringBuffer();
            int response = httpURLConnection.getResponseCode();
            if (response == 200) {
                bf = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                if (withLine){
                    while ((readLine = bf.readLine()) != null) {
                        temp = temp .append(readLine).append("\r\n");
                    }
                }else{
                    while ((readLine = bf.readLine()) != null) {
                        temp = temp .append(readLine);
                    }
                }
            } else {
                fail=true;
                temp.append(ERROR_MESSAGE).append(response);
            }
            //set content

            String pageEncod = httpURLConnection.getContentEncoding();
            if (pageEncod == null)
                pageEncod = defaultContentEncoding;
            httpContent.urlString = urlString;
            httpContent.defaultPort = httpURLConnection.getURL().getDefaultPort();
            httpContent.file = httpURLConnection.getURL().getFile();
            httpContent.host = httpURLConnection.getURL().getHost();
            httpContent.path = httpURLConnection.getURL().getPath();
            httpContent.port = httpURLConnection.getURL().getPort();
            httpContent.protocol = httpURLConnection.getURL().getProtocol();
            httpContent.query = httpURLConnection.getURL().getQuery();
            httpContent.ref = httpURLConnection.getURL().getRef();
            httpContent.userInfo = httpURLConnection.getURL().getUserInfo();
            httpContent.content = new String(temp.toString().getBytes(), pageEncod);
            httpContent.contentEncoding = pageEncod;
            httpContent.code = httpURLConnection.getResponseCode();
            httpContent.message = httpURLConnection.getResponseMessage();
            httpContent.contentType = httpURLConnection.getContentType();
            httpContent.method = httpURLConnection.getRequestMethod();
            httpContent.connectTimeout = httpURLConnection.getConnectTimeout();
            httpContent.readTimeout = httpURLConnection.getReadTimeout();
            return httpContent;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
        }
        return null;
    }


    /***
     *
     */
    public class HTTPContent {
        String urlString;
        int defaultPort;
        String file;
        String host;
        String path;
        int port;
        String protocol;
        String query;
        String ref;
        String userInfo;
        String contentEncoding;
        String content;
        String contentType;
        int code;
        String message;
        String method;
        int connectTimeout;
        int readTimeout;
        Vector<String> contentCollection;

        public String getContent() {
            return content;
        }

        public String getContentType() {
            return contentType;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public Vector<String> getContentCollection() {
            return contentCollection;
        }

        public String getContentEncoding() {
            return contentEncoding;
        }

        public String getMethod() {
            return method;
        }

        public int getConnectTimeout() {
            return connectTimeout;
        }

        public int getReadTimeout() {
            return readTimeout;
        }

        public String getUrlString() {
            return urlString;
        }

        public int getDefaultPort() {
            return defaultPort;
        }

        public String getFile() {
            return file;
        }

        public String getHost() {
            return host;
        }

        public String getPath() {
            return path;
        }

        public int getPort() {
            return port;
        }

        public String getProtocol() {
            return protocol;
        }

        public String getQuery() {
            return query;
        }

        public String getRef() {
            return ref;
        }

        public String getUserInfo() {
            return userInfo;
        }
    }

}



/*
        URL u;
        HttpURLConnection httpURLConnection;
        BufferedReader bf;
        String readLine;
        try {
            u = new URL(url);
            httpURLConnection = (HttpURLConnection) u.openConnection();
            StringBuilder temp = null;
            int responce = httpURLConnection.getResponseCode();
            if (responce == 200) {
                bf = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                while ((readLine = bf.readLine()) != null) {
                    temp = temp .append(readLine);
                }
                return temp.toString();
            } else {
                return ERROR_MESSAGE+responce;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;*/

