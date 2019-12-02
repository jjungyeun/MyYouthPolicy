package com.example.mypolicy.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypolicy.R;

public class RankingViewHolder extends RecyclerView.ViewHolder {
    public TextView p_code;
    public TextView title;
    public TextView views;
    public TextView wee;

    public RankingViewHolder(@NonNull View itemView)
    {
        super(itemView);
//        p_code=(TextView)itemView.findViewById(R.id.tv_ranking_pcode);
//        title=(TextView)itemView.findViewById(R.id.tv_ranking_title);
//        views=(TextView)itemView.findViewById(R.id.tv_);
        p_code=(TextView)itemView.findViewById(R.id.tv_ranking_pcode);
        title=(TextView)itemView.findViewById(R.id.tv_ranking_title);
        views=(TextView)itemView.findViewById(R.id.tv_ranking_view);
        wee=itemView.findViewById(R.id.tv_wee);
    }
}
