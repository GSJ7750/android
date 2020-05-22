package com.example.register;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.kch.proj_manager_v3.R;

import org.json.JSONObject;

public class CmDeleteActivity extends AppCompatActivity {

    EditText passwordtext;
    Button cancelbutton;
    Button erasebutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cm_delete);

        passwordtext = findViewById(R.id.editTextPassword);
        cancelbutton = findViewById(R.id.cmd_button);
        erasebutton = findViewById(R.id.cmd_button2);

        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        erasebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                Intent intent = new Intent(getApplicationContext(),CommunityActivity.class);
                                startActivity(intent);
                                CommunityContentActivity ca = (CommunityContentActivity) CommunityContentActivity._content_Activity;
                                ca.finish();
                                finish();
                            }
                        }
                        catch(Exception e){
                           e.printStackTrace();
                        }
                    }
                };
                Intent passedIntent = getIntent();
                String deletetext = passwordtext.getText().toString();
                String erasetime = passedIntent.getStringExtra("time");
                String pd = passedIntent.getStringExtra("pd");

                String time[] = erasetime.split(" ");
                String erasetime2 = time[0] + time[1];

                if(pd.equals(deletetext)) {
                    CmDeleteRequest cmdeleteRequest = new CmDeleteRequest(deletetext, erasetime, responseListener);
                    CmDeletereplyRequest cmDeletereplyRequest = new CmDeletereplyRequest(erasetime2,responseListener);
                    RequestQueue queue = Volley.newRequestQueue(CmDeleteActivity.this);
                    queue.add(cmdeleteRequest);
                    queue.add(cmDeletereplyRequest);
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CmDeleteActivity.this);
                    builder.setMessage("비밀번호가 틀렸습니다. 다시 입력하세요.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();

                }
            }
        });

    }
}
