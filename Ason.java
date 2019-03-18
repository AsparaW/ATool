package com.mantouland.fakezhihuribao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by asparaw on 2019/3/19.
 */

public class Ason {
    /***
     * pass in the bean object
     * support one generic type
     * create generic object by an anonymous inner class
     * @param jsonString
     * @param object
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String jsonString,T object){
            try {
                Type type=object.getClass().getGenericSuperclass();
                if (type instanceof  ParameterizedType){
                    JSONObject pointer=new JSONObject(jsonString);
                    return jsonDFS(pointer,object);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            return null;
        }

    /***
     * pass in the bean class
     * @param jsonString
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String jsonString, Class<T> clazz){
        try {
            JSONObject pointer=new JSONObject(jsonString);
            return jsonDFS(pointer,clazz);
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


    private static <T> T jsonDFS(JSONObject pointer, Class<T> clazz) throws IllegalAccessException, NoSuchFieldException, JSONException, InstantiationException {
        T bean=clazz.newInstance();
        Iterator<String> keys= pointer.keys();
        while (keys.hasNext()){
            String name= keys.next();
            Field field= bean.getClass().getDeclaredField(name);
            field.setAccessible(true);
            Object value=pointer.get(name);
            if (value instanceof JSONObject){
                Class setType;
                Type checkType=clazz.getClass().getGenericSuperclass();
                if (checkType instanceof  ParameterizedType){
                    setType= (Class) ((ParameterizedType)clazz.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                }else {
                    setType=field.getType();
                }
                field.set(bean,jsonDFS((JSONObject) value,setType));
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

    @SuppressWarnings("unchecked")
    private static <T> T jsonDFS(JSONObject pointer, T object) throws IllegalAccessException, NoSuchFieldException, JSONException, InstantiationException {
        T bean= (T) Objects.requireNonNull(object.getClass().getSuperclass()).newInstance();
        Iterator<String> keys= pointer.keys();
        while (keys.hasNext()){
            String name= keys.next();
            Field field= bean.getClass().getDeclaredField(name);
            field.setAccessible(true);
            Object value=pointer.get(name);
            if (value instanceof JSONObject){
                Class setType;
                Type checkType=object.getClass().getGenericSuperclass();
                if (checkType instanceof  ParameterizedType){
                    setType= (Class) ((ParameterizedType)object.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                }else {
                    setType=field.getType();
                }
                field.set(bean,jsonDFS((JSONObject) value,setType));
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