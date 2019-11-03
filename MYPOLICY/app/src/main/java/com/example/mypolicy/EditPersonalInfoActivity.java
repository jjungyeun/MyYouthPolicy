package com.example.mypolicy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class EditPersonalInfoActivity extends AppCompatActivity {

    private String TAG = "EditPersonalInfoActivity";

    EditText et_userEmail, et_userPW, et_userName, et_userAge, et_userRegion;
    Button btn_cancel, btn_change;
    FirebaseFirestore db;
    SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);

        sharedPreferences = getSharedPreferences("session",MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();

        et_userEmail = findViewById(R.id.et_profile_email);
        et_userPW = findViewById(R.id.et_profile_pw);
        et_userName = findViewById(R.id.et_profile_name);
        et_userAge = findViewById(R.id.et_profile_age);
        et_userRegion = findViewById(R.id.et_profile_region);
        btn_cancel = findViewById(R.id.btn_profile_cancel);
        btn_change = findViewById(R.id.btn_profile_change);

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
                        et_userAge.setText(document.get("age").toString());
                        if(document.get("region")!=null)
                            et_userRegion.setText(document.get("region").toString());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(EditPersonalInfoActivity.this, ProfileActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userPW = et_userPW.getText().toString();
                String userName = et_userName.getText().toString();
                String userAge = et_userAge.getText().toString();
                String userRegion = et_userRegion.getText().toString();

                if(userPW.isEmpty()||userName.isEmpty()||userAge.isEmpty()){
                    Toast.makeText(EditPersonalInfoActivity.this, "빈칸을 모두 채워주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("password", userPW);
                    userInfo.put("name", userName);
                    userInfo.put("age",userAge);
                    userInfo.put("region", userRegion);
                    db.collection("user")
                            .document(et_userEmail.getText().toString())
                            .set(userInfo)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    Toast.makeText(EditPersonalInfoActivity.this, "변경되었습니다.", Toast.LENGTH_SHORT).show();
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

            }
        });

    }
}
