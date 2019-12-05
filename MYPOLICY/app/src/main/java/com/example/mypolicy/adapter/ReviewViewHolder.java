package com.example.mypolicy.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypolicy.R;

public class ReviewViewHolder extends RecyclerView.ViewHolder {

    public TextView id;
    public TextView comment;
    public TextView time;
    public Button delete_btn;
    public ReviewViewHolder(@NonNull View itemView) {
        super(itemView);

        id=(TextView)itemView.findViewById(R.id.tv_comment_id);
        comment=(TextView)itemView.findViewById(R.id.tv_comment_content);
        time=(TextView)itemView.findViewById(R.id.tv_time);
        delete_btn=(Button)itemView.findViewById(R.id.tv_delete_btn);
    }
}
