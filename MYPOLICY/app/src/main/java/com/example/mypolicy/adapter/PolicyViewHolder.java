package com.example.mypolicy.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypolicy.R;

import org.w3c.dom.Text;


public class PolicyViewHolder extends RecyclerView.ViewHolder {

    public TextView policyName;
    public TextView applyStart;
    public TextView applyEnd;
    public PolicyViewHolder(@NonNull View itemView) {
        super(itemView);

        policyName=(TextView)itemView.findViewById(R.id.tv_policy_name);
        applyStart=(TextView)itemView.findViewById(R.id.tv_apply_start);
        applyEnd=(TextView)itemView.findViewById(R.id.tv_apply_end);


    }
}
