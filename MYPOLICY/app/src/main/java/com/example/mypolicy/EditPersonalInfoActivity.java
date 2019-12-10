package com.example.mypolicy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mypolicy.service.IApiService;
import com.example.mypolicy.service.RestClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPersonalInfoActivity extends AppCompatActivity {

    private String TAG = "EditPersonalInfoActivity";

    EditText et_userEmail, et_userName;
    Spinner sp_do, sp_si, sp_age;
    RadioButton rb_male, rb_female;
    Button btn_cancel, btn_change;
    FirebaseFirestore db;
    SharedPreferences sharedPreferences;

    String userEmail, userPW, userName, userAge, userSex;
    String region_do, region_si;

    ArrayList<String> userRegion = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);

        sharedPreferences = getSharedPreferences("session",MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();

        et_userEmail = findViewById(R.id.et_profile_email);
        et_userName = findViewById(R.id.et_profile_name);
        btn_cancel = findViewById(R.id.btn_profile_cancel);
        btn_change = findViewById(R.id.btn_profile_change);
        sp_age = findViewById(R.id.sp_profile_age);
        sp_do=findViewById(R.id.sp_profile_do);
        sp_si = findViewById(R.id.sp_profile_si);
        rb_male=findViewById(R.id.rb_male);
        rb_female = findViewById(R.id.rb_female);

        final HashMap<String,Object> userMap=new HashMap<>();
        final IApiService iApiService=new RestClient("http://49.236.136.213:3000/").getApiService();
        final Call<JSONObject> storeUserCall=iApiService.storeUser(userMap);


        et_userEmail.setText(sharedPreferences.getString("userEmail",null));
        DocumentReference userInfo = db.collection("user").document(sharedPreferences.getString("userEmail",null));
        userInfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        et_userName.setText(document.get("name").toString());

                        String region = document.get("region").toString();
                        userRegion.add(region.substring(1,3));
                        userRegion.add(region.substring(5,region.length()-1));
                        Log.d(TAG, "region: "+userRegion);

                        userAge = document.get("age").toString();
                        userSex = document.get("sex").toString();
                        userPW = document.get("password").toString();

                        if(userSex.equals("남"))
                            rb_male.toggle();
                        else
                            rb_female.toggle();

                        ArrayAdapter ageAdapter = (ArrayAdapter) sp_age.getAdapter();
                        ArrayAdapter doAdapter = (ArrayAdapter) sp_do.getAdapter();
                        sp_age.setSelection(ageAdapter.getPosition(userAge));
                        sp_do.setSelection(doAdapter.getPosition(userRegion.get(0)));
                        setList(0);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });



        sp_age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userAge = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                userAge = "";
            }
        });

        sp_do.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                region_do = parent.getItemAtPosition(position).toString();
                setList(1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                region_do = "";
            }
        });

        sp_si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                region_si = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                region_si = "";
            }
        });

        rb_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSex = "남";
            }
        });
        rb_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSex = "여";
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmail = et_userEmail.getText().toString();
                userName = et_userName.getText().toString();

                userRegion.set(0,region_do);
                userRegion.set(1,region_si);

                if(userName.isEmpty()||userAge.isEmpty()){
                    Toast.makeText(EditPersonalInfoActivity.this, "빈칸을 모두 채워주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("name", userName);
                    userInfo.put("password", userPW);
                    userInfo.put("age",userAge);
                    userInfo.put("sex",userSex);
                    userInfo.put("region", userRegion);
                    db.collection("user")
                            .document(et_userEmail.getText().toString())
                            .set(userInfo)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    Toasty.info(EditPersonalInfoActivity.this, "변경되었습니다!!", Toast.LENGTH_SHORT, true).show();
//                                    Intent intent = new Intent(EditPersonalInfoActivity.this, ProfileActivity.class);
//                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
                }
                userMap.put("uID",userEmail);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        storeUserCall.clone().enqueue(new Callback<JSONObject>() {
                            @Override
                            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                            }

                            @Override
                            public void onFailure(Call<JSONObject> call, Throwable t) {

                            }
                        });
                    }
                },3000);
            }
        });


    }

    private void setList(int mode){ //0: default, 1: select
        ArrayAdapter adapter= ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.전체,
                android.R.layout.simple_list_item_1);
        switch (region_do){
            case "서울":
                adapter = ArrayAdapter.createFromResource(
                        getApplicationContext(),
                        R.array.서울,
                        android.R.layout.simple_list_item_1);
                break;
            case "경기":
                adapter = ArrayAdapter.createFromResource(
                        getApplicationContext(),
                        R.array.경기,
                        android.R.layout.simple_list_item_1);
                break;
            default:

                adapter = ArrayAdapter.createFromResource(
                        getApplicationContext(),
                        R.array.전체,
                        android.R.layout.simple_list_item_1);
                break;
        }
        sp_si.setAdapter(adapter);

        if(mode==1)
            sp_si.setSelection(0);
        else {
            Log.d(TAG, "setList: "+ adapter.getPosition(userRegion.get(1)));
            sp_si.setSelection(adapter.getPosition(userRegion.get(1)));
            if(adapter.getPosition(userRegion.get(1))==-1){
                sp_si.setSelection(0);
                userRegion.set(1,"전체");
            }
        }
    }
}
