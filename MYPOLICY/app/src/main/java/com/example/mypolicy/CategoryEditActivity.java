package com.example.mypolicy;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CategoryEditActivity extends AppCompatActivity {

    TextView tv_job_score,tv_business_score,tv_life_score,tv_house_score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_edit);

        tv_job_score=(TextView)findViewById(R.id.tv_job_score);
        tv_business_score=(TextView)findViewById(R.id.tv_business_score);
        tv_life_score=(TextView)findViewById(R.id.tv_life_score);
        tv_house_score=(TextView)findViewById(R.id.tv_house_score);

    }
}
