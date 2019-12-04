package com.example.mypolicy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mypolicy.model.Preference;
import com.example.mypolicy.service.IApiService;
import com.example.mypolicy.service.RestClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCategoryActivity extends AppCompatActivity {

    TextView tv_job_score,tv_business_score,tv_life_score,tv_house_score;
    ImageView btn_job_plus, btn_job_minus, btn_business_plus, btn_business_minus,
        btn_life_plus, btn_life_minus, btn_house_plus, btn_house_minus;
    Button btn_cancel, btn_ok,btn_profile_change;
    View.OnClickListener listener;
    SharedPreferences sharedPreferences;
    FirebaseFirestore db;


    int[] scoreList = {0,0,0,0};
    final IApiService iApiService=new RestClient("http://49.236.136.213:3000/").getApiService();
    final HashMap<String,Object> setCategoryMap=new HashMap<>();
    final Call<ArrayList<Preference>> setCategoryCall=iApiService.showPreference(setCategoryMap);


    ArrayList<String> search_region =new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("session",MODE_PRIVATE);

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

        btn_profile_change=findViewById(R.id.btn_profile_change);

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
        final String email=sharedPreferences.getString("userEmail",null);
        setCategoryMap.put("uID",email);
        setCategoryCall.clone().enqueue(new Callback<ArrayList<Preference>>() {
            @Override
            public void onResponse(Call<ArrayList<Preference>> call, Response<ArrayList<Preference>> response) {
                String score=new Gson().toJson(response.body());
                int job,changup,life,jugeo;
                try{
                    JSONArray jsonArray=new JSONArray(score);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    job=jsonObject.getInt("Employment_sup_priority");
                    Log.d("점수",""+Integer.toString(job));
                    changup=jsonObject.getInt("Startup_sup_priority");
                    Log.d("점수",""+changup);

                    life=jsonObject.getInt("Life_welfare_priority");
                    Log.d("점수",""+life);

                    jugeo=jsonObject.getInt("Residential_financial_priority");
                    Log.d("점수",""+jugeo);

                    tv_job_score.setText(Integer.toString(job));
                    tv_business_score.setText(Integer.toString(changup));
                    tv_life_score.setText(Integer.toString(life));
                    tv_house_score.setText(Integer.toString(jugeo));
                    Log.d("점수",""+job+" "+changup+"  "+life+" "+jugeo);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Preference>> call, Throwable t) {

            }
        });


        // ***********************************************
        // score 초기값 불러오기
        final DocumentReference userInfo = db.collection("user").document(sharedPreferences.getString("userEmail",null));





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

                userInfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
//                                Log.d("파이어베이스 정보", "DocumentSnapshot data: " + document.getData());
//                                Log.d("파이어베이스",""+document.get("name").toString());
//                                Log.d("파이어베이스",""+document.get("age"));
//                                Log.d("파이어베이스",""+document.get("sex"));
                                String[] words=document.get("region").toString().split(",");

                                words[0]=words[0].replace("[","");
                                words[1]=words[1].replace("]","");
                                words[0]=words[0].trim();
                                words[1]=words[1].trim();
                                search_region.add(words[0]);search_region.add(words[1]);
                                Log.d("파이어베이스11",""+words[0]);
                                Log.d("파이어베이스11",""+words[1]);
                                Log.d("파이어베이스11",""+search_region);


                                Call<JSONObject> userRegisterCall=iApiService.userRegister(email,document.get("name").toString(),search_region,
                                        Integer.parseInt(document.get("age").toString()),document.get("sex").toString(),scoreList[0],scoreList[1],scoreList[2],scoreList[3]);

                                userRegisterCall.clone().enqueue(new Callback<JSONObject>() {
                                    @Override
                                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<JSONObject> call, Throwable t) {

                                    }
                                });

//
                            } else {

                            }
                        } else {

                        }
                    }
                });
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
