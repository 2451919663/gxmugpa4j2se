package com.jamaskii.core.utils;

import java.io.*;

public class IOUtils {
    public static String readString(InputStream inputStream){
        try{
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while( (len=inputStream.read(buffer)) != -1 ){
                outStream.write(buffer, 0, len);
            }
            inputStream.close();
            return outStream.toString();
        }catch (IOException e) {
            return null;
        }
    }

    public static byte[] readBytes(InputStream inputStream){
        try{
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while( (len=inputStream.read(buffer)) != -1 ){
                outStream.write(buffer, 0, len);
            }
            inputStream.close();
            return outStream.toByteArray();
        }catch (IOException e) {
            return null;
        }
    }

    public static void println(Object object){
        System.out.println(object);
    }

    public static boolean checkDirectoryByFileName(String fileName){
        try {
            File objFile = new File(fileName);
            File objDir = new File(objFile.getParent());
            if (objDir.exists()) {
                return true;
            } else {
                objDir.mkdirs();
                return true;
            }
        }catch (Exception e) {
            return false;
        }
    }

    public static boolean checkDirectoryByDirectoryName(String dirName){
        try {
            File objDir = new File(dirName);
            if (objDir.exists()) {
                return true;
            } else {
                objDir.mkdirs();
                return true;
            }
        }catch (Exception e) {
            return false;
        }
    }

    public static boolean checkFile(String fileName){
        if(!checkDirectoryByFileName(fileName)){
            return false;
        }else
        {
            try{
                File objFile=new File(fileName);
                objFile.createNewFile();
                return true;
            }catch (IOException e){
                return false;
            }
        }
    }

    public static String readFileString(String fileName){
        try{
            FileInputStream fileInputStream = new FileInputStream(fileName);
            return readString(fileInputStream);
        }catch (FileNotFoundException e){
            return null;
        }
    }

    public static byte[] readFileBytes(String fileName){
        try{
            FileInputStream fileInputStream = new FileInputStream(fileName);
            return readBytes(fileInputStream);
        }catch (FileNotFoundException e){
            return null;
        }
    }

    public static boolean fileWriteBytes(String fileName, byte[] bytes){
        try{
            if(!checkFile(fileName)){
                return false;
            }
            FileOutputStream fileOutputStream=new FileOutputStream(fileName);
            fileOutputStream.write(bytes);
            return true;
        }catch (IOException e){
            return false;
        }
    }

    public static boolean fileWriteString(String fileName, String text){
        return fileWriteBytes(fileName, text.getBytes());
    }


}
