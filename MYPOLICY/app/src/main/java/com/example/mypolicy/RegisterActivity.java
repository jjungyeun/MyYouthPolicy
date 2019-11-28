package com.example.mypolicy;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private final String TAG ="RegisterActivity";

    EditText et_userEmail, et_userPW, et_userName, et_userAge;
    EditText et_userRegion,et_userSex;
    Button btn_cancel, btn_join;
    LinearLayout ll;
    InputMethodManager imm;

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
        et_userAge = findViewById(R.id.et_register_age);
        et_userRegion=findViewById(R.id.et_register_location);
        et_userSex=findViewById(R.id.et_register_sex);
        btn_cancel = findViewById(R.id.btn_register_cancel);
        btn_join = findViewById(R.id.btn_register_join);

        ll = findViewById(R.id.ll_register);
        imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = et_userEmail.getText().toString();
                String userPW = et_userPW.getText().toString();
                String userName = et_userName.getText().toString();
                String userAge = et_userAge.getText().toString();

                if(userEmail.isEmpty()||userPW.isEmpty()||userName.isEmpty()||userAge.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "빈칸을 모두 채워주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.createUserWithEmailAndPassword(userEmail, userPW)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail: success");
                                        Toast.makeText(RegisterActivity.this, "회원가입 성공. 로그인 해주세요!", Toast.LENGTH_SHORT).show();
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

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("password", et_userPW.getText().toString());
        userInfo.put("name", et_userName.getText().toString());
        userInfo.put("age",et_userAge.getText().toString());
        userInfo.put("sex",et_userSex.getText().toString());
        userInfo.put("region",et_userRegion.getText().toString());

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
