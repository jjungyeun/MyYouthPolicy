package com.example.mypolicy.adapter;

import android.content.Context;
import android.content.SharedPreferences;
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

    String parseTime="";
    SharedPreferences sharedPreferences;

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
////////////////////////////댓글 한줄에해당하는 정보들 코딩하는 부분///////////////////////////////
    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        sharedPreferences=holder.itemView.getContext().getSharedPreferences("session", Context.MODE_PRIVATE);

        String pattern = "yyyy/MM/dd HH시 mm분";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        parseTime=simpleDateFormat.format(rList.get(position).getReview_time());

         //sharedPreferences.getString("userEmail",null)//아이디 꺼내는 함수
        //구현할부분
        //rList.get(position).getReview_uID와 shared.......가 같다면 xml에 버튼이 visible하게 셋팅팅
        holder.id.setText(rList.get(position).getReview_uID());// 통신에서 받아온 아이디
        holder.comment.setText(rList.get(position).getContents());// 통신에서 받아온 코멘트

       holder.time.setText(parseTime);// 통신에서 받아온 시간 ->파싱

    }

    @Override
    public int getItemCount() {
        return rList.size();
    }
}
