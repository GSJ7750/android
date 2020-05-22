package com.example.register;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kch.proj_manager_v3.R;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class CommunityContentActivity extends AppCompatActivity {

    public static Activity _content_Activity;


    TextView titletext;
    TextView contenttext;
    TextView timetext;
    TextView usertext;
    TextView usertext2;
    EditText replytext;

    Button deletebutton;
    ImageButton backbutton;
    Button replywritebutton;
    ListView replylistview;
    ReplyAdapter replyadapter;

    String pd;
    int replynum;

    private ArrayList<CommunityReplyItem> communityReplyList;
    private ArrayList<CommunityReplyItem> communityReplysaveList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_content);


        _content_Activity = CommunityContentActivity.this;

        replyadapter = new ReplyAdapter();
        communityReplyList = new ArrayList<CommunityReplyItem>();
        communityReplysaveList = new ArrayList<CommunityReplyItem>();

        usertext= (TextView) findViewById(R.id.textView21);
        timetext= (TextView) findViewById(R.id.textView22);
        titletext = (TextView) findViewById(R.id.textView23);
        contenttext= (TextView) findViewById(R.id.textView24);
        usertext2= (TextView) findViewById(R.id.textView27);
        deletebutton = (Button) findViewById(R.id.deleteButton2);
        backbutton = (ImageButton) findViewById(R.id.backbutton7);
        replywritebutton = (Button) findViewById(R.id.replywriteButton);
        replylistview = (ListView) findViewById(R.id.replylistView2);
        replytext = (EditText) findViewById(R.id.replywritetext);


        Intent passedIntent = getIntent();
        processIntent(passedIntent);

        String Cmr_Time = timetext.getText().toString();
        String time[] = Cmr_Time.split(" ");
        final String time_replyedited = time[0]+time[1];



        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://skj.dothome.co.kr/Cmr_Requset.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    int count = 0;
                    String Cmr_Reply;
                    String Cmr_Writetime;
                    String Cmr_Userid;
                    String Cmr_Password;
                    while (count < jsonArray.length()) {
                        JSONObject object = jsonArray.getJSONObject(count);
                        Cmr_Reply = object.getString("Cmr_Reply");
                        Cmr_Writetime = object.getString("Cmr_Writetime");
                        Cmr_Userid = object.getString("Cmr_Userid");
                        Cmr_Password = object.getString("Cmr_Password");
                        communityReplyList.add(new CommunityReplyItem(Cmr_Userid, Cmr_Reply, Cmr_Writetime,Cmr_Password));
                        communityReplysaveList.add(new CommunityReplyItem(Cmr_Userid, Cmr_Reply, Cmr_Writetime,Cmr_Password));
                        replyadapter.addItem(communityReplyList.get(count));
                        count++;
                        replynum = count;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                replylistview.setAdapter(replyadapter);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Cmr_Time", time_replyedited);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(CommunityContentActivity.this);
        requestQueue.add(stringRequest);

    //        new replyBackgroundTask().execute();

        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String erasetime = timetext.getText().toString();
                Intent deleteintent = new Intent(getApplicationContext(),CmDeleteActivity.class);
                deleteintent.putExtra("time",erasetime);
                deleteintent.putExtra("pd",pd);
                startActivity(deleteintent);
            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        replywritebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (replytext.getText().toString().getBytes().length > 0) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CommunityContentActivity.this);
                LinearLayout replylayout = new LinearLayout(getApplicationContext());
                replylayout.setOrientation(LinearLayout.VERTICAL);
                builder.setTitle("아이디,비밀번호를 입력하세요");
                final EditText et = new EditText(CommunityContentActivity.this);
                et.setHint("아이디");
                replylayout.addView(et);
                final EditText et2 = new EditText(CommunityContentActivity.this);
                et2.setHint("비밀번호(영문,숫자)");
                replylayout.addView(et2);
                et2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                et2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                et2.setFilters(new InputFilter[]{filter});
                builder.setView(replylayout);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i){
                        if(et.getText().toString().getBytes().length>0&&et2.getText().toString().getBytes().length>0) {
                            String Cmr_reply = replytext.getText().toString();
                            String Cmr_time = timetext.getText().toString();
                            String time[] = Cmr_time.split(" ");
                            String time_edited = time[0] + time[1];
                            Long now = System.currentTimeMillis();
                            Date date = new Date(now);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
                            String Cmr_Writetime = sdf.format(date);
                            String Cmr_Password = et2.getText().toString();
                            String Cmr_Userid = et.getText().toString();
                            if(Cmr_Userid.equals("글쓴이"))
                            {
                                Cmr_Userid = "글쓴이 ";
                            }
                            if(Cmr_Password.equals(pd))
                            {
                                Cmr_Userid = "글쓴이";
                            }
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        replyadapter.notifyDataSetChanged();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            ReplyWriteRequest replyWriteRequest = new ReplyWriteRequest(Cmr_reply, time_edited, Cmr_Writetime,Cmr_Userid,Cmr_Password, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(CommunityContentActivity.this);
                            queue.add(replyWriteRequest);
                            communityReplyList.add(new CommunityReplyItem(Cmr_Userid, Cmr_reply, Cmr_Writetime, Cmr_Password));
                            communityReplysaveList.add(new CommunityReplyItem(Cmr_Userid, Cmr_reply, Cmr_Writetime, Cmr_Password));
                            replyadapter.claerAllItems();
                            for(int j=0;j<=replynum;j++)
                            {
                                replyadapter.addItem(communityReplyList.get(j));
                            }
                            replynum++;
                            replytext.setText("");
                            dialog.dismiss();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"비밀번호가 입력되지 않았습니다. 다시 시도해주세요.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();

                }

                else
                {
                    Toast.makeText(getApplicationContext(),"댓글 내용을 입력하세요",Toast.LENGTH_SHORT).show();
                }
            }
        });






    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CommunityContentActivity.this, CommunityActivity.class);
        startActivity(intent);
        finish();
    }

    private void processIntent(Intent intent){

        CommunityItem item = (CommunityItem) intent.getSerializableExtra("item");
        String title_cm = item.getTitle();
        String content_cm = item.getContent();
        String user_cm = item.getUserid();
        String time_cm = item.getWritetime();
        pd = item.getPassword();
        titletext.setText(title_cm);
        contenttext.setText(content_cm);
        usertext.setText(user_cm);
        timetext.setText(time_cm);

    }

    class ReplyAdapter extends BaseAdapter{

        ArrayList<CommunityReplyItem> items = new ArrayList<CommunityReplyItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(CommunityReplyItem item){
            items.add(item);
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public void claerAllItems() {
            items.clear();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final CommunityReplyItemView crview = new CommunityReplyItemView(getApplicationContext());
            final String numStr2 = String.valueOf(i);
            CommunityReplyItem item = items.get(i);
            ImageButton db2 = (ImageButton) crview.findViewById(R.id.deletebutton2);
            crview.setReplyName(item.getNickname());
            crview.setReplyContent(item.getReply());
            crview.setReplyTime(item.getDate());
            final String deletetime = crview.getTextView30().getText().toString();
            final String replydelete = item.getPassword();
            final int listnumber = i;
            db2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(CommunityContentActivity.this);
                    final EditText det = new EditText(CommunityContentActivity.this);
                    det.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    det.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    det.setFilters(new InputFilter[]{filter});
                    builder.setMessage("비밀번호 입력하세요");
                    builder.setView(det);
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogInterface, int i) {
                            String deletetext = det.getText().toString();
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try{
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");
                                        dialogInterface.dismiss();
                                        replyadapter.notifyDataSetChanged();
                                    }
                                    catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            };
                            if(replydelete.equals(deletetext)) {
                                CmrDeleteRequest cmrdeleteRequest = new CmrDeleteRequest(deletetext, deletetime, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(CommunityContentActivity.this);
                                queue.add(cmrdeleteRequest);
                                communityReplyList.remove(listnumber);
                                communityReplysaveList.remove(listnumber);
                                replynum--;
                                replyadapter.claerAllItems();
                                for(int j=0;j<replynum;j++)
                                {
                                    replyadapter.addItem(communityReplyList.get(j));
                                }
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(CommunityContentActivity.this);
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
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
            });
            return crview;
        }
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
