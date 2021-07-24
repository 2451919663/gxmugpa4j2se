package com.jamaskii.core.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class Net {

    public static Response get(String url, Map<String,String> headers){
        Response response=new Response();
        try{
            HttpURLConnection connection=(HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(15000);

            if(headers!=null)
            {
                Object[] keys=headers.keySet().toArray();
                for (Object key:keys
                     ) {
                    connection.setRequestProperty((String)key, (String)headers.get(key));
                }
            }

            connection.connect();

            response.data= IOUtils.readBytes(connection.getInputStream());
            response.cookies=connection.getHeaderField("Set-Cookie");
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }

    public static Response post(String url, byte[] data, Map<String,String> headers){
        Response response=new Response();
        try{
            HttpURLConnection connection=(HttpURLConnection) new URL(url).openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(15000);

            if(headers!=null){
                Object[] keys=headers.keySet().toArray();
                for (Object key:keys
                ) {
                    connection.setRequestProperty((String)key, (String)headers.get(key));
                }
            }

            connection.connect();
            connection.getOutputStream().write(data);

            response.data=IOUtils.readBytes(connection.getInputStream());
            response.cookies=connection.getHeaderField("Set-Cookie");
        }
        catch (FileNotFoundException e){ }
        catch(IOException e){
            e.printStackTrace();
        }
        return response;
    }

}
