package com.example.mypolicy.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypolicy.DetailPolicyActivity;
import com.example.mypolicy.R;
import com.example.mypolicy.model.SearchData;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder>{


    String[] eng_mon={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    String[] kor_mon={"1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"};
    String[] eng_day={"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
    String[] kor_day={"월요일","화요일","수요일","목요일","금요일","토요일","일요일"};


    public StringBuilder startSB=new StringBuilder();
    public StringBuilder endSB=new StringBuilder();


    public ArrayList<SearchData> sList;
    public SearchAdapter(ArrayList<SearchData> list) {
        sList=list;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_keyword_row, parent, false);
        return new SearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        final int pCode=sList.get(position).getP_code();
        Log.d("피코드피코드피코드",""+pCode);

        holder.title.setText(sList.get(position).getTitle());//String이라 문제 없음
        holder.region.setText(sList.get(position).getRegion());//String이라 문제 없음



        holder.p_code.setText(Integer.toString(pCode));//int를 String으로 파싱
        holder.category.setText(sList.get(position).getCategory());

        int matchScore=sList.get(position).getMatch_score();
        holder.match_score.setText(Integer.toString(matchScore));//int를 String으로 파싱

        String applyStart=""; String applyEnd="";
        if(sList.get(position).getApply_start()==null &&sList.get(position).getApply_end()==null) {

            holder.apply_start.setText("공고 확인 후 신청 바람");
            holder.apply_end.setText("공고 확인 후 신청 바람");

        }
        else if(sList.get(position).getApply_start()==null&&sList.get(position).getApply_end()!=null) {
            holder.apply_start.setText("공고 확인 후 신청 바람");

            applyEnd=sList.get(position).getApply_end().toString();
            applyEnd=date_parse(applyEnd);

            holder.apply_end.setText(applyEnd);
        }
        else if(sList.get(position).getApply_start()!=null&&sList.get(position).getApply_end()==null) {
            applyStart=sList.get(position).getApply_start().toString();
            applyStart=date_parse(applyStart);

            holder.apply_start.setText(applyStart);
            holder.apply_end.setText("공고 확인 후 신청 바람");

        }
        else
        {
            applyStart=sList.get(position).getApply_start().toString();
            applyEnd=sList.get(position).getApply_end().toString();
            applyStart=date_parse(applyStart);
            applyEnd=date_parse(applyEnd);


            Log.d("날짜",""+applyStart);
            Log.d("날짜",""+applyEnd);
            //  Log.d("날짜",""+pList.get(position).getApply_start().toString());

//
            holder.apply_start.setText(applyStart);
            holder.apply_end.setText(applyEnd);

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context=view.getContext();
                Intent intent=new Intent(context, DetailPolicyActivity.class);
                intent.putExtra("position",pCode);
                Log.d("주소주소",""+pCode);
                context.startActivity(intent);
            }
        });


    }
    private String date_parse(String rawDate) {
        StringBuilder sb=new StringBuilder();
        Log.d("원본","data"+rawDate);

        String [] words=rawDate.split("\\s");
        for(int i=0;i<words.length;i++){
            Log.d("원본","split"+i+words[i]);
        }
//*********************************년도****************************************//
        String year=words[5];
        // Log.d("원본","년도"+year);
        sb.append(year); sb.append("년 ");

        //*********************************월****************************************//
        String month=words[1];
        for(int i=0;i<eng_mon.length;i++)
        {
            if(month.equals(eng_mon[i]))
            {
                sb.append(kor_mon[i]);
                sb.append(" ");
                break;
            }
        }

        //*********************************일****************************************//
        String day=words[2];

        sb.append(day);sb.append("일");
        sb.append(" ");


        return sb.toString();
    }
    @Override
    public int getItemCount() {
        return sList.size();
    }
}
