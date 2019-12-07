package com.example.mypolicy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mypolicy.model.Referral;

public class GuideFragment2 extends Fragment {

    private int bgRes;
    private Referral referral;
    private ImageView imageView;
    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bgRes = getArguments().getInt("data");
//        referral = (Referral) getArguments().get("data");
//        name=getArguments().getString("data");

    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        imageView = (ImageView) getView().findViewById(R.id.image);

        imageView.setBackgroundResource(bgRes);
//        textView=getView().findViewById(R.id.tv_policy_name_test);
//        textView.setText(referral.getTitle());
    }
}
