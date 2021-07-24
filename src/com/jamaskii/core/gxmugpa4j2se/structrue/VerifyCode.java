package com.jamaskii.core.gxmugpa4j2se.structrue;

import com.jamaskii.core.utils.Net;
import com.jamaskii.core.utils.Response;

import java.util.HashMap;
import java.util.Map;

public class VerifyCode {
    public byte[] image=null;
    public String cookie;

    public static VerifyCode getVerifyCode(){
        VerifyCode verifyCode=new VerifyCode();
        try{
            Map<String,String> headers=new HashMap<String,String>();
            headers.put("Accept","image/webp,*/*");
            headers.put("Host","jw.gxmu.edu.cn");
            headers.put("Referer","http://jw.gxmu.edu.cn/jsxsd/");
            headers.put("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            Response response=Net.get("http://jw.gxmu.edu.cn/jsxsd/verifycode.servlet",headers);
            verifyCode.image=response.data;
            verifyCode.cookie=response.cookies;
        }catch(Exception e){}
        return verifyCode;
    }

    public static void getVerifyCodeAsync(OnVerifyCodeGot onVerifyCodeGot){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                onVerifyCodeGot.onGot(VerifyCode.getVerifyCode());
            }
        });
        thread.start();
    }
}
