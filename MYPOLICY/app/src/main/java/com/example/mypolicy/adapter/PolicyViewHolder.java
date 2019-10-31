package com.example.mypolicy.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypolicy.DetailPolicyActivity;
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

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("포지션",""+getAdapterPosition());
                Context context=view.getContext();
                Intent intent=new Intent(context, DetailPolicyActivity.class);
                intent.putExtra("position",getAdapterPosition());
                context.startActivity(intent);
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d("롱포지션", ""+ getAdapterPosition());
                return false;
            }
        });
    }
}
