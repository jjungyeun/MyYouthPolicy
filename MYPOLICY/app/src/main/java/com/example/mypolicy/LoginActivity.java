package com.example.mypolicy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mypolicy.service.IApiService;
import com.example.mypolicy.service.RestClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private final String TAG ="LoginActivity";

    Button btn_register,btn_login;
    EditText et_userEmail, et_userPW;
    LinearLayout ll;
    InputMethodManager imm;

    private FirebaseAuth mAuth;

    SharedPreferences session, autoLogin, sharedPreferences;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        session = getSharedPreferences("session", MODE_PRIVATE);
        autoLogin = getSharedPreferences("autoLogin",MODE_PRIVATE);



        et_userEmail = findViewById(R.id.et_login_email);
        et_userPW = findViewById(R.id.et_login_pw);
        btn_register=findViewById(R.id.btn_goto_register);
        btn_login=findViewById(R.id.btn_login);

        ll = findViewById(R.id.ll_login);
        imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = et_userEmail.getText().toString();
                String userPW = et_userPW.getText().toString();

                if(userEmail.isEmpty()||userPW.isEmpty()){
                    Toast.makeText(LoginActivity.this, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.signInWithEmailAndPassword(userEmail, userPW)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        successLogIn(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "로그인 실패",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }


            }


        });



        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(!currentUser.equals(null))
//            successSignIn(currentUser);

        String doAutoLogin = autoLogin.getString("state","");
        String userEmail = session.getString("userEmail", "");

        if(doAutoLogin.equals("")){ // 앱 처음 실행
            SharedPreferences.Editor loginEditor = autoLogin.edit();
            loginEditor.putString("state", "yes");
            loginEditor.apply();
        }

        if(!userEmail.equals("") && doAutoLogin.equals("yes")){  // session정보 있음 & 자동로그인 설정 yes
                FirebaseUser currentUser = mAuth.getCurrentUser();
                successLogIn(currentUser);
        } else {
            session.edit().clear().apply();
        }
    }

    public void successLogIn(FirebaseUser user){
        et_userEmail.setText("");
        et_userPW.setText("");

        String userEmail = user.getEmail();

        // session에 필요한 값들 저장
        SharedPreferences.Editor sessionEditor = session.edit();
        sessionEditor.putString("userEmail", userEmail);
        // ...
        sessionEditor.apply();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
