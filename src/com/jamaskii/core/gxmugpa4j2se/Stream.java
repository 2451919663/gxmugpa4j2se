package com.jamaskii.core.gxmugpa4j2se;

import com.jamaskii.core.gxmugpa4j2se.structrue.OnLoginDone;
import com.jamaskii.core.gxmugpa4j2se.structrue.Subject;
import com.jamaskii.core.utils.Base64Encoder;
import com.jamaskii.core.utils.Net;
import com.jamaskii.core.utils.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Stream {

    public static void login(String cookie, String username, String password, String verifyCode, OnLoginDone onLoginDone){
        Map<String,String> headers=new HashMap<String,String>();
        headers.put("Accept","text/html");
        headers.put("Accept-Language","zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
        headers.put("Host","jw.gxmu.edu.cn");
        headers.put("Origin","http://jw.gxmu.edu.cn");
        headers.put("Referer","http://jw.gxmu.edu.cn/jsxsd/");
        headers.put("Upgrade-Insecure-Requests","1");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Connection","keep-alive");
        headers.put("User-Agent","Mozilla/5.0(WindowsNT10.0;Win64;x64;rv:77.0)Gecko/20100101Firefox/77.0");
        headers.put("Cookie", cookie);

        final String encoded = "encoded=" +
                Base64Encoder.encode(username.getBytes()).replace("=", "%3d") +
                "%25%25%25"+
                Base64Encoder.encode(password.getBytes()).replace("=", "%3d")+
                "&RANDOMCODE="+verifyCode;

        try{
            Response response= Net.post("http://jw.gxmu.edu.cn/jsxsd/xk/LoginToXk",encoded.getBytes(),headers);
            if(response==null){
                onLoginDone.onLoginDone(cookie,false,"HTTP请求失败",null);
            }
            else if(response.data==null){
                onLoginDone.onLoginDone(cookie,true,null,getTerms(cookie));
            }else{
                String text=response.getText("gbk");
                Pattern pattern = Pattern.compile("<font style=\"display: inline;white-space:nowrap;\" color=\"red\">(.+?)</font>");
                Matcher matcher = pattern.matcher(text);
                String msg="未知错误";
                if (matcher.find()){
                    msg=matcher.group(1);
                }
                onLoginDone.onLoginDone(cookie,false,msg,null);
            }

        }catch (Exception e){
            onLoginDone.onLoginDone(cookie,false,e.toString(),null);
        }
    }

    public static void loginAsync(String cookie, String username, String password, String verifyCode, OnLoginDone onLoginDone){
        Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {
                login(cookie,username,password,verifyCode,onLoginDone);
            }
        });
        thread.start();
    }

    public static List<String> getTerms(String cookie){
        List<String> terms=new ArrayList<>();
        try{
            Map<String,String> headers=new HashMap<>();
            headers.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            headers.put("Host","jw.gxmu.edu.cn");
            headers.put("Referer","http://jw.gxmu.edu.cn/jsxsd/framework/xsMain.jsp");
            headers.put("Cookie",cookie);
            headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0");
            Response response=Net.get("http://jw.gxmu.edu.cn/jsxsd/kscj/cjcx_query?Ves632DSdyV=NEW_XSD_XJCJ",headers);

            Pattern pattern = Pattern.compile("<option value=\"\\d\\d\\d\\d-\\d\\d\\d\\d-\\d\">(\\d\\d\\d\\d-\\d\\d\\d\\d-\\d)</option>");
            Matcher matcher = pattern.matcher(response.getText());

            terms.add("全部");
            while (matcher.find()) {
                terms.add(matcher.group(1));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return terms;
    }

    public static float calculateGPA(List<Subject> subjects, boolean failSubjectJoined){
        float num1=0;
        float num2=0;
        for (Subject subject:subjects
             ) {
            if(subject.type.equals("必修")){
                if(subject.point>0 || (subject.point<=0 && failSubjectJoined)){
                    num1+=(subject.credit*subject.point);
                    num2+=subject.credit;
                }
            }
        }
        return num1/num2;
    }

}
