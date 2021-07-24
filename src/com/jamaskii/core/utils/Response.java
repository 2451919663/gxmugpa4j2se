package com.jamaskii.core.utils;

public class Response {
    public String cookies;
    public byte[] data;

    public String getText(){
        if(this.data!=null){
            try{
                return new String(data,"utf-8");
            }catch (Exception e){
                return null;
            }
        }else{
            return null;
        }
    }
    public String getText(String encoding){
        if(this.data!=null){
            try{
                return new String(data,encoding);
            }catch (Exception e){
                return null;
            }
        }else{
            return null;
        }
    }
}
