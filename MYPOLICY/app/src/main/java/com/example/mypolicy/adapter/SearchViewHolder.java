package com.example.mypolicy.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypolicy.R;


public class SearchViewHolder extends RecyclerView.ViewHolder {

    public TextView p_code;
    public TextView title;
    public TextView apply_start;
    public TextView apply_end;
    public TextView category;
    public TextView region;
    public TextView match_score;

    public SearchViewHolder(@NonNull View itemView) {
        super(itemView);

        p_code=(TextView)itemView.findViewById(R.id.tv_search_pcode);
        title=(TextView)itemView.findViewById(R.id.tv_search_policy_name);
        apply_start=(TextView)itemView.findViewById(R.id.tv_search_apply_start);
        apply_end=(TextView)itemView.findViewById(R.id.tv_search_apply_end);
        category=(TextView)itemView.findViewById(R.id.tv_search_category);
        region=(TextView)itemView.findViewById(R.id.tv_search_region);
        match_score=(TextView)itemView.findViewById(R.id.tv_search_match_score);
    }


}
