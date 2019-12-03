package com.example.mypolicy;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypolicy.model.Policy;
import com.example.mypolicy.service.IApiService;
import com.example.mypolicy.service.RestClient;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class requestDialog {
    private Context context;
    SharedPreferences sharedPreferences;
    final HashMap<String,Object> requestHashMap=new HashMap<>();
    final IApiService iApiService=new RestClient("http://49.236.136.213:3000/").getApiService();
    final Call<JSONObject> requestCall=iApiService.senqRequest(requestHashMap);
    String category="";
    Spinner sp_request;

    public requestDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_request);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText message = (EditText) dlg.findViewById(R.id.et_message);
        final Button okButton = (Button) dlg.findViewById(R.id.btn_okButton);
        final Button cancelButton = (Button) dlg.findViewById(R.id.btn_cancelButton);

        sharedPreferences=context.getSharedPreferences("session",Context.MODE_PRIVATE);

        sp_request=dlg.findViewById(R.id.sp_request);
        sp_request.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category=sp_request.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("리퀘스트",""+sharedPreferences.getString("userEmail",null)+" "+ message.getText().toString()+ " "+category);

                requestHashMap.put("req_uID",sharedPreferences.getString("userEmail",null));
                requestHashMap.put("req_category",category);
                requestHashMap.put("req_contents",message.getText().toString());

                requestCall.clone().enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {

                    }
                });
                Toasty.info(context.getApplicationContext(), "관리자에게 요청 성공!!", Toast.LENGTH_SHORT, true).show();
                dlg.dismiss();




            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }

}
