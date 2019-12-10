package com.example.mypolicy;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mypolicy.service.IApiService;
import com.example.mypolicy.service.RestClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
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

public class RegisterActivity extends AppCompatActivity {

    private final String TAG ="RegisterActivity";

    EditText et_userEmail, et_userPW, et_userName;
    Spinner sp_do, sp_si, sp_age;
    RadioButton rb_male, rb_female;
    Button btn_cancel, btn_join;
    LinearLayout ll;
    InputMethodManager imm;

    String userEmail, userPW, userName, userAge, userSex;
    String region_do, region_si;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        et_userEmail = findViewById(R.id.et_register_email);
        et_userPW = findViewById(R.id.et_register_pw);
        et_userName = findViewById(R.id.et_register_name);
        sp_age = findViewById(R.id.sp_register_age);
        sp_do=findViewById(R.id.sp_register_do);
        sp_si = findViewById(R.id.sp_register_si);
        rb_male=findViewById(R.id.rb_male);
        rb_female = findViewById(R.id.rb_female);
        btn_cancel = findViewById(R.id.btn_register_cancel);
        btn_join = findViewById(R.id.btn_register_join);

        ll = findViewById(R.id.ll_register);
        imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);

        final HashMap<String,Object> userMap=new HashMap<>();
        final IApiService iApiService=new RestClient("http://49.236.136.213:3000/").getApiService();
        final Call<JSONObject> storeUserCall=iApiService.storeUser(userMap);

        userAge = ""; region_do = ""; region_si = ""; userSex = "";
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
                sp_si.setSelection(0);
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



        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmail = et_userEmail.getText().toString();
                userPW = et_userPW.getText().toString();
                userName = et_userName.getText().toString();



                if(userEmail.isEmpty()||userPW.isEmpty()||userName.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "빈칸을 모두 채워주세요", Toast.LENGTH_SHORT).show();
                }
                else if(userAge.isEmpty()||region_do.isEmpty()||region_si.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "정보를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.createUserWithEmailAndPassword(userEmail, userPW)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail: success");
                                        Toasty.info(RegisterActivity.this, "회원가입 성공. 로그인 해주세요!", Toast.LENGTH_SHORT, true).show();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        successRegister(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "회원가입 실패",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                }

                Log.d("회원가입 아이디",""+ et_userEmail.getText().toString());

                userMap.put("uID",et_userEmail.getText().toString());


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

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(et_userEmail.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(et_userPW.getWindowToken(), 0);
            }
        });

    }

    // user 정보 db에 넣기
    public void successRegister(FirebaseUser user){
        ArrayList<String> userRegion =new ArrayList<>();
        userRegion.add(region_do);
        userRegion.add(region_si);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("password", userPW);
        userInfo.put("name", userName);
        userInfo.put("age",userAge);
        userInfo.put("sex",userSex);
        userInfo.put("region",userRegion);

        db.collection("user")
                .document(et_userEmail.getText().toString())
                .set(userInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

        finish();
    }

}
