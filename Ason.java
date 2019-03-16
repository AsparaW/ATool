package com.mantouland.fakezhihuribao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Ason {

    public static <T> T fromJson(String jsonString, Class<T> clazz){
        try {
            JSONObject pointer=new JSONObject(jsonString);
            T bean=jsonDFS(pointer,clazz);
            return bean;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T jsonDFS(JSONObject pointer, Class<T> clazz) throws IllegalAccessException, NoSuchFieldException, JSONException, InstantiationException {
        T bean=clazz.newInstance();
        Iterator<String> keys= pointer.keys();
        while (keys.hasNext()){
            String name= keys.next();
            Field field= bean.getClass().getDeclaredField(name);
            field.setAccessible(true);
            Object value=pointer.get(name);
            if (value instanceof JSONObject){
                field.set(bean,jsonDFS((JSONObject) value,field.getType()));
            }else if (value instanceof JSONArray){
                JSONArray jsonArray= (JSONArray) value;
                List list=new ArrayList();
                if(jsonArray.length()==0){
                    //DO NOTHING
                }else if (jsonArray.get(0) instanceof JSONArray || jsonArray.get(0) instanceof  JSONObject){
                    ParameterizedType pt = (ParameterizedType) field.getGenericType();
                    Class type = (Class) pt.getActualTypeArguments()[0];
                    //get the generic type from the original list
                    for (int i=0;i<jsonArray.length();i++){
                        list.add(jsonDFS((JSONObject) jsonArray.get(i),type));
                    }
                }else {
                    for (int i=0;i<jsonArray.length();i++){
                       list.add(jsonArray.get(i));
                    }
                }
                field.set(bean,list);
            }else {
                field.set(bean,value);
            }
        }
        return bean;
    }


}