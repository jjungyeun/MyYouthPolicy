package com.example.mypolicy.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypolicy.R;

public class ReviewViewHolder extends RecyclerView.ViewHolder {

    public TextView id;
    public TextView comment;
    public ReviewViewHolder(@NonNull View itemView) {
        super(itemView);

        id=(TextView)itemView.findViewById(R.id.tv_comment_id);
        comment=(TextView)itemView.findViewById(R.id.tv_comment_content);
    }
}
