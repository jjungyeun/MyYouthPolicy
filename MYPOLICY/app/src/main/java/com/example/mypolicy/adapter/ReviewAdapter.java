package com.example.mypolicy.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypolicy.R;
import com.example.mypolicy.model.Policy;
import com.example.mypolicy.model.Review;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    String[] eng_mon={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    String[] kor_mon={"1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"};
    String[] eng_day={"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
    String[] kor_day={"월요일","화요일","수요일","목요일","금요일","토요일","일요일"};
    String parseTime="";

    public ArrayList<Review> rList;
    public ReviewAdapter(ArrayList<Review> list) {
        rList = list;
    }
    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row, parent, false);
        return new ReviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        String pattern = "yyyy/MM/dd HH시 mm분";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        parseTime=simpleDateFormat.format(rList.get(position).getReview_time());

        Log.d("날짜원본",""+parseTime);
        holder.id.setText(rList.get(position).getReview_uID());
        holder.comment.setText(rList.get(position).getContents());

       holder.time.setText(parseTime);
        String tmp=rList.get(position).getReview_time().toString();


        String[] words= tmp.split("\\s");
        StringBuilder sb=new StringBuilder();
        StringBuilder result=new StringBuilder();
       // Log.d("뭐가나오니",""+tmp);
        for(String wo: words)
        {
            Log.d("뭐가나오니",""+wo);
            sb.append(" ");
            sb.append(wo);
        }
//        Log.d("뭐가나오니gg",""+sb.toString()[5]);

        for(int i=0;i<eng_mon.length;i++)
        {
        if(sb.toString().contains(eng_mon[i]))
            result.append(kor_mon[i]);
        }
        //result.append()
        for(int i=0;i<eng_day.length;i++)
        {
            if(sb.toString().contains(eng_day[i]))
                result.append(kor_day[i]);
        }
    }

    @Override
    public int getItemCount() {
        return rList.size();
    }
}
