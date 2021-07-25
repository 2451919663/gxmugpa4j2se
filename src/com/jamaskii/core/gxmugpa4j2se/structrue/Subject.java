package com.jamaskii.core.gxmugpa4j2se.structrue;

import com.jamaskii.core.utils.Net;
import com.jamaskii.core.utils.Response;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Subject {
    public String name;     //名称
    public float score;     //总评
    public float credit;    //学分
    public float point;     //绩点
    public String type;     //类型
    public String detail;   //学科分数构成

    public String cookie;

    @Override
    public String toString(){
        return "["+this.type+"]"+this.name+"\t"+this.score+"\t"+this.credit+"\t"+this.point+"\t"+this.detail;
    }

    public Subject(){}

    public Subject(String name,float score,float credit,String type,String detail){
        this.name=name;
        this.score=score;
        this.credit=credit;
        this.type=type;
        this.detail=detail;
    }

    public static List<Subject> getSubjects(String cookie, String term){
        ArrayList<Subject> subjects=new ArrayList<>();
        try{
            Map<String,String> headers=new HashMap<>();
            headers.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            headers.put("Accept-Language","zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
            headers.put("Connection","keep-alive");
            headers.put("Content-Type","application/x-www-form-urlencoded");
            headers.put("Cookie", cookie);
            headers.put("Referer","http://jw.gxmu.edu.cn/jsxsd/kscj/cjcx_query?Ves632DSdyV=NEW_XSD_XJCJ");
            headers.put("Host","jw.gxmu.edu.cn");
            headers.put("Origin","http://jw.gxmu.edu.cn");
            headers.put("Upgrade-Insecure-Requests","1");
            headers.put("User-Agent","Mozilla/5.0(WindowsNT10.0;Win64;x64;rv:77.0)Gecko/20100101Firefox/77.0");
            String params = "kksj="+term+"&kcxz=&kcmc=&xsfs=all";
            Response response= Net.post("http://jw.gxmu.edu.cn/jsxsd/kscj/cjcx_list",params.getBytes(),headers);

            Pattern patternRow =Pattern.compile("<tr>([\\s\\S]+?)</tr>");
            Matcher matcherRow = patternRow.matcher(response.getText());

            int i=0;
            while (matcherRow.find())//find every subject
            {
                if (i==0 || i==1)//skip the first and second tr elements which are meaningless.
                {
                    i++;
                    continue;
                }
                Pattern patternCell = Pattern.compile("<td.*?>([\\s\\S]*?)</td>");
                Matcher matcherCell = patternCell.matcher(matcherRow.group(1));
                int j=0;
                Subject subject = new Subject();
                subject.cookie=cookie;
                while (matcherCell.find())//find every single info of subject.
                {
                    switch (j)
                    {
                        case 3://subject name

                            subject.name=matcherCell.group(1);
                            break;
                        case 4://subject score
                            Pattern patternScore = Pattern.compile(">(.+?)<");
                            Matcher matcherScore = patternScore.matcher(matcherCell.group(1));
                            if (matcherScore.find())
                            {
                                subject.score=Float.parseFloat(matcherScore.group(1));
                            }

                            Pattern patternDetail = Pattern.compile("\\('(.+?)',700,500");
                            Matcher matcherDetail = patternDetail.matcher(matcherCell.group(1));
                            if(matcherDetail.find())
                            {
                                subject.detail="http://jw.gxmu.edu.cn"+matcherDetail.group(1);
                            }

                            break;
                        case 5://subject credit
                            subject.credit=Float.parseFloat(matcherCell.group(1));
                            break;
                        case 7://subject grade point
                            subject.point=Float.parseFloat(matcherCell.group(1));
                            break;
                        case 9://subject type
                            subject.type=matcherCell.group(1);
                            break;
                    }
                    j++;
                }
                subjects.add(subject);
                i++;
            }
        }catch(Exception e){}
        return subjects;
    }

    public static Map<String,String> getDetail(String cookie,Subject subject){
        Map<String,String> detail=new HashMap<>();
        try{
            Map<String,String> headers=new HashMap<>();
            headers.put("Host", "jw.gxmu.edu.cn");
            headers.put("Upgrade-Insecure-Requests", "1");
            headers.put("User-Agent", "Mozilla/5.0(WindowsNT10.0;Win64;x64;rv:77.0)Gecko/20100101Firefox/77.0");
            headers.put("Cookie", cookie);
            Response response=Net.get(subject.detail,headers);

            Pattern pattern = Pattern.compile("序号</th>([\\s\\S]*?)<!--");
            Matcher matcher= pattern.matcher(response.getText());
            String target = "";
            if (matcher.find())
            {
                target=matcher.group(1);
            }

            pattern = Pattern.compile("<th class=\"Nsb_r_list_thb\" style=\"width: 30px;\">(.+?)</th>");
            matcher = pattern.matcher(target);
            ArrayList<String> heads = new ArrayList<>();
            while(matcher.find())
            {
                heads.add(matcher.group(1));
            }

            ArrayList<String> datas = new ArrayList<>();
            pattern = Pattern.compile("<td>([\\s\\S]*?)</td>");
            matcher = pattern.matcher(response.getText());
            int i = 0;
            while (matcher.find())
            {
                if(i==0)
                {
                    i++;
                    continue;
                }else
                {
                    datas.add(matcher.group(1));
                }

            }

            /*String msg = "总成绩:"+datas.get(datas.size()-1);
            if(heads.size()==datas.size()-1){
                msg+="\n\n";
                int j = 0;
                while (j<heads.size())
                {
                    msg+=heads.get(j)+":"+datas.get(j);
                    if(j!=datas.size()-2)
                    {
                        msg+="\n";
                        if(j%2 != 0)
                        {
                            msg+="\n";
                        }
                    }
                    j++;
                }
            }*/
            for(i=0;i<heads.size();i++){
                detail.put(heads.get(i),datas.get(i));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return detail;
    }

    public Map<String,String> getDetail(){
        return Subject.getDetail(this.cookie,this);
    }

}
