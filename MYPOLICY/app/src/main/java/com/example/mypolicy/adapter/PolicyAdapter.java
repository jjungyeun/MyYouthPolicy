package com.example.mypolicy.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypolicy.R;
import com.example.mypolicy.model.Policy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PolicyAdapter extends RecyclerView.Adapter<PolicyViewHolder> {
   String[] eng_mon={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
   String[] kor_mon={"1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"};
   String[] eng_day={"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
   String[] kor_day={"월요일","화요일","수요일","목요일","금요일","토요일","일요일"};

    private ArrayList<Policy> pList;
    public PolicyAdapter(ArrayList<Policy> list) {
        pList = list;
    }
    @NonNull
    @Override
    public PolicyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row, parent, false);
        return new PolicyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PolicyViewHolder holder, final int position) {
//        String format=(pList.get(position).getApply_start());
Log.d("d",""+pList.get(position).getApply_start());
        Log.d("d",""+pList.get(position).getApply_end());
        holder.policyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("위치확인 시작",""+pList.get(position).getApply_start());
                Log.d("위치확인 끝",""+pList.get(position).getApply_end());

            }
        });
        holder.policyName.setText(pList.get(position).getTitle());
        if(pList.get(position).getApply_start()==null) holder.applyStart.setText("없음");
        if(pList.get(position).getApply_end()==null) holder.applyEnd.setText("없음");
        if(pList.get(position).getApply_end()!=null &&pList.get(position).getApply_start()!=null)
        {
            String start=pList.get(position).getApply_start().toString();
            String end=pList.get(position).getApply_end().toString();

            holder.applyStart.setText(date_parse(start));
            holder.applyEnd.setText(date_parse(end));

        }
//        holder.policyName.setText(pList.get(position).getTitle());

    }

    private String date_parse(String rawDate) {
        //Log.d("길이",""+rawDate.);
        StringBuffer sb=new StringBuffer();

        for(int i=0;i<12;i++)
        {
            //Log.d("결과","여긴");
            if(rawDate.contains(eng_mon[i]))
            {
                sb.append(kor_mon[i]);
                Log.d("결과",""+sb);

            }
        }
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

    @Override
    public int getItemCount() {
        return pList.size();
    }
}
