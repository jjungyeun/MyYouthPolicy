package com.example.mypolicy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypolicy.DetailPolicyActivity;
import com.example.mypolicy.R;
import com.example.mypolicy.model.Policy;
import com.example.mypolicy.model.RankingData;

import java.util.ArrayList;

public class RankingAdapter extends RecyclerView.Adapter<RankingViewHolder>{

    public ArrayList<RankingData> rList;

    public RankingAdapter(ArrayList<RankingData> list) {
        rList = list;
    }

    @NonNull
    @Override
    public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_row, parent, false);
        return new RankingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingViewHolder holder, int position) {
        holder.title.setText(rList.get(position).getTitle());
        final long pcode=rList.get(position).getP_code();
        
        holder.views.setText(Integer.toString(rList.get(position).getViews()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context=view.getContext();
                Intent intent=new Intent(context, DetailPolicyActivity.class);

                intent.putExtra("position",pcode);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rList.size();
    }
}
