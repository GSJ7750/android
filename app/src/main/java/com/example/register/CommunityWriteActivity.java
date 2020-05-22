package com.example.register;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.kch.proj_manager_v3.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class CommunityWriteActivity extends AppCompatActivity {

    private EditText editTextTitle,editTextContent,editTextUser,editTextPassword;
    private Button btnfinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_write);

        editTextTitle = findViewById(R.id.editText4);
        editTextContent = findViewById(R.id.editText5);
        editTextUser = findViewById(R.id.editText);
        editTextPassword = findViewById(R.id.editText2);
        editTextPassword.setFilters(new InputFilter[]{filter});

        btnfinish = findViewById(R.id.finishButton);


        editTextContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().getBytes().length >= 2000) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CommunityWriteActivity.this);
                    builder.setMessage("글자수 제한입니다.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            }
        });
        btnfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Cm_Title = editTextTitle.getText().toString();
                String Cm_Content = editTextContent.getText().toString();
                String Cm_User = editTextUser.getText().toString();
                String Cm_Password = editTextPassword.getText().toString();


                final int tlength = Cm_Title.getBytes().length;
                final int ulength = Cm_User.getBytes().length;
                final int plength = Cm_Password.getBytes().length;

                if (tlength > 0 && ulength > 0 && plength > 0) {
                    Long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String Cm_Time = sdf.format(date);

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Intent intent = new Intent(CommunityWriteActivity.this, CommunityActivity.class);
                                startActivity(intent);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    WriteRequest writeRequest = new WriteRequest(Cm_Title, Cm_Content, Cm_Time, Cm_User, Cm_Password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(CommunityWriteActivity.this);
                    queue.add(writeRequest);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"아이디,비밀번호,제목은 꼭 입력하세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CommunityWriteActivity.this, CommunityActivity.class);
        startActivity(intent);
        finish();
    }
    protected InputFilter filter= new InputFilter() {

        public CharSequence filter(CharSequence source, int start, int end,

                                   Spanned dest, int dstart, int dend) {



            Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");

            if (!ps.matcher(source).matches()) {

                return "";

            }

            return null;

        }
    };

}
