package com.example.mypolicy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CategoryEditActivity extends AppCompatActivity {

    TextView tv_job_score,tv_business_score,tv_life_score,tv_house_score;
    ImageView btn_job_plus, btn_job_minus, btn_business_plus, btn_business_minus,
        btn_life_plus, btn_life_minus, btn_house_plus, btn_house_minus;
    Button btn_cancel, btn_ok;
    View.OnClickListener listener;

    int[] scoreList = {0,0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_edit);

        tv_job_score=(TextView)findViewById(R.id.tv_job_score);
        tv_business_score=(TextView)findViewById(R.id.tv_business_score);
        tv_life_score=(TextView)findViewById(R.id.tv_life_score);
        tv_house_score=(TextView)findViewById(R.id.tv_house_score);

        btn_cancel = (Button) findViewById(R.id.btn_category_cancel);
        btn_ok = (Button) findViewById(R.id.btn_category_ok);

        btn_job_plus = (ImageView) findViewById(R.id.btn_job_plus);
        btn_job_minus = (ImageView) findViewById(R.id.btn_job_minus);
        btn_business_plus = (ImageView) findViewById(R.id.btn_business_plus);
        btn_business_minus = (ImageView) findViewById(R.id.btn_business_minus);
        btn_life_plus = (ImageView) findViewById(R.id.btn_life_plus);
        btn_life_minus = (ImageView) findViewById(R.id.btn_life_minus);
        btn_house_plus = (ImageView) findViewById(R.id.btn_house_plus);
        btn_house_minus = (ImageView) findViewById(R.id.btn_house_minus);

        listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btn_job_plus:
                        plusScore(tv_job_score);
                        break;
                    case R.id.btn_job_minus:
                        minusScore(tv_job_score);
                        break;
                    case R.id.btn_business_plus:
                        plusScore(tv_business_score);
                        break;
                    case R.id.btn_business_minus:
                        minusScore(tv_business_score);
                        break;
                    case R.id.btn_life_plus:
                        plusScore(tv_life_score);
                        break;
                    case R.id.btn_life_minus:
                        minusScore(tv_life_score);
                        break;
                    case R.id.btn_house_plus:
                        plusScore(tv_house_score);
                        break;
                    case R.id.btn_house_minus:
                        minusScore(tv_house_score);
                        break;
                }
            }
        };

        btn_job_plus.setOnClickListener(listener);
        btn_job_minus.setOnClickListener(listener);
        btn_business_plus.setOnClickListener(listener);
        btn_business_minus.setOnClickListener(listener);
        btn_life_plus.setOnClickListener(listener);
        btn_life_minus.setOnClickListener(listener);
        btn_house_plus.setOnClickListener(listener);
        btn_house_minus.setOnClickListener(listener);


        // ***********************************************
        // score 초기값 불러오기



        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreList[0] = Integer.parseInt(tv_job_score.getText().toString());
                scoreList[1] = Integer.parseInt(tv_business_score.getText().toString());
                scoreList[2] = Integer.parseInt(tv_life_score.getText().toString());
                scoreList[3] = Integer.parseInt(tv_house_score.getText().toString());

                // *****************************************************
                // scoreList (int[]) 서버로 보내기

                finish();
            }
        });
    }

    private void plusScore(TextView tv){
        String current_score = tv.getText().toString();
        if(!current_score.equals("5")){
            int score = Integer.parseInt(current_score);
            score += 1;
            current_score = Integer.toString(score);
            tv.setText(current_score);
        }
    }

    private void minusScore(TextView tv){
        String current_score = tv.getText().toString();
        if(!current_score.equals("0")){
            int score = Integer.parseInt(current_score);
            score -= 1;
            current_score = Integer.toString(score);
            tv.setText(current_score);
        }
    }
}
