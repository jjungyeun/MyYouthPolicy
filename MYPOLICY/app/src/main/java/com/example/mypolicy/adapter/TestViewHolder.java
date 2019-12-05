package com.example.mypolicy.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypolicy.R;

import org.w3c.dom.Text;

public class TestViewHolder extends RecyclerView.ViewHolder{

    public TextView tv_title;
    public TextView tv_apply_start;
    public TextView tv_apply_end;



    public TestViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_title=(TextView)itemView.findViewById(R.id.tv_policy_name_test);
        tv_apply_start=(TextView)itemView.findViewById(R.id.tv_apply_start_test);
        tv_apply_end=(TextView)itemView.findViewById(R.id.tv_apply_end_test);

    }
}
