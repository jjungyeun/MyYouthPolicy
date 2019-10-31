package com.example.mypolicy;

import android.util.Log;

public class DataParser {
    String[] eng_mon={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    String[] kor_mon={"1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"};
    String[] eng_day={"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
    String[] kor_day={"월요일","화요일","수요일","목요일","금요일","토요일","일요일"};

    private String date_parse(String rawDate) {
        //Log.d("길이",""+rawDate.);
        StringBuffer sb=new StringBuffer();
        String copyrawDate=rawDate;
        for(int i=0;i<12;i++)
        {
            //Log.d("결과","여긴");
            if(rawDate.contains(eng_mon[i]))//월
            {
                sb.append(kor_mon[i]);
                Log.d("결과",""+sb);

            }
        }
        sb.append(" ");
        String[] words=copyrawDate.split("\\s");
        sb.append(words[2]);
        sb.append("일");
        sb.append(" ");
        for(int i=0;i<7;i++)
        {
            if(rawDate.contains(eng_day[i]))
            {
                sb.append(kor_day[i]);
                Log.d("결과",""+sb);

            }
        }

        return sb.toString();
    }
}
