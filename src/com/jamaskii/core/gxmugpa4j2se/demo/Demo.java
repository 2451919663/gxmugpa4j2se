package com.jamaskii.core.gxmugpa4j2se.demo;

import com.jamaskii.core.alert.ImageAlert;
import com.jamaskii.core.gxmugpa4j2se.Stream;
import com.jamaskii.core.gxmugpa4j2se.structrue.OnLoginDone;
import com.jamaskii.core.gxmugpa4j2se.structrue.Subject;
import com.jamaskii.core.gxmugpa4j2se.structrue.VerifyCode;
import com.jamaskii.core.utils.IOUtils;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Demo {
    public static void main(String[] args){
        VerifyCode verifyCode=VerifyCode.getVerifyCode();
        ImageAlert imageAlert=new ImageAlert(verifyCode.cookie, verifyCode.image);
        imageAlert.display();
        Scanner scanner=new Scanner(System.in);
        System.out.print("验证码：");
        String verifyCodeStr=scanner.nextLine();
        Stream.login(verifyCode.cookie, "username", "passwd", verifyCodeStr, new OnLoginDone() {
            @Override
            public void onLoginDone(String cookie, boolean success, String msg, List<String> terms) {
                super.onLoginDone(cookie, success, msg, terms);
                if(success){
                    IOUtils.println("登录成功！\tCookie:"+cookie);
                    for (String term:terms
                         ) {
                        System.out.println(term);
                    }
                    List<Subject> subjects=Subject.getSubjects(cookie,terms.get(2));
                    for (Subject subject:subjects
                         ) {
                        System.out.println(subject.toString());
                    }
                    System.out.println("GPA:"+Stream.calculateGPA(subjects,false));
                    Map<String,String> detail=subjects.get(2).getDetail();
                    for (Object key:detail.keySet().toArray()
                         ) {
                        System.out.println(key+detail.get((String)key));
                    }
                }else{
                    IOUtils.println("登录失败："+msg+"\tCookie:"+cookie);
                }
            }
        });
    }
}
