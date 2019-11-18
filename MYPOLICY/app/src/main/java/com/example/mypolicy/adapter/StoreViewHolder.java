package com.example.mypolicy.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypolicy.DetailPolicyActivity;
import com.example.mypolicy.R;
import com.example.mypolicy.service.IApiService;
import com.example.mypolicy.service.RestClient;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreViewHolder extends RecyclerView.ViewHolder {

    public TextView tv_title;
    public TextView tv_applyStart;
    public TextView tv_applyEnd;
    public TextView tv_Pcode;
    public Button btn_storeDelete;
    private TextView tv_sharedId;



    public StoreViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_title=itemView.findViewById(R.id.tv_policy_name_store);
        tv_applyStart=itemView.findViewById(R.id.tv_apply_start_store);
        tv_applyEnd=itemView.findViewById(R.id.tv_apply_end_store);
        btn_storeDelete=itemView.findViewById(R.id.btn_store_delete);
        tv_Pcode=itemView.findViewById(R.id.tv_pcode);
        tv_sharedId=(TextView)itemView.findViewById(R.id.tv_email_store);

    }
}
