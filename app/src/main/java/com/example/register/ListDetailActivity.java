package com.example.register;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.kch.proj_manager_v3.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ListDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    Context mContext;
    String lost_IDtest;
    GoogleMap map;
    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    int index;
    ///슬라이드식 액티비티 종료
    private SlidrInterface slidr;

    ////////////////////////////////////////////////댓글 읽어오기 변수
    private ListView listView;
    private ReplyListAdapter adapter;
    private List<Reply> replyList;
    private List<Reply> replySaveList;


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
/////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail);


        ///슬라이드식 액티비티 종료
        slidr = Slidr.attach(this);

        mContext = this;
        //////////////////////////////////////////북마크버튼
        final ImageButton bookmarkButton = (ImageButton)findViewById(R.id.bookmarkButton2);
        index = 1;
        /////////////////////////////////////////


//////////////////////////////////////////////////////////////댓글 읽어오기


        listView = (ListView)findViewById(R.id.listView);
        replyList = new ArrayList<Reply>();

        //어댑터 초기화부분 userList와 어댑터를 연결해준다.

        //listView.setAdapter(adapter);


///////////////////////////////////////////////////////////댓글 읽어오기


        fragmentManager = getFragmentManager();
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.googleMap2);
        mapFragment.getMapAsync(this);


        final TextView lost_ID = (TextView)findViewById(R.id.lost_ID_Detail_Text);
        final TextView subject = (TextView)findViewById(R.id.subject_Detail_Text);
        TextView place = (TextView)findViewById(R.id.place_Detail_Text);
        TextView time = (TextView)findViewById(R.id.time_Detail_Text);
        TextView details = (TextView)findViewById(R.id.details_Detail_Text);
        final TextView password = (TextView)findViewById(R.id.password_Detail_Textview);
        ImageView detailImage = (ImageView)findViewById(R.id.DetatilimageView);
        Intent intent = getIntent();



        final String lost_IDText = intent.getStringExtra("lost_ID");
        final String subjectText = intent.getStringExtra("subject");
        final String placeText = intent.getStringExtra("place");
        final String timeText = intent.getStringExtra("time");
        final String time2Text = intent.getStringExtra("time2");
        final String time3Text = intent.getStringExtra("time3");
        final String detailText = intent.getStringExtra("details");
        final String url = intent.getStringExtra("url");
        //////0919추가
        final String Longitude = intent.getStringExtra("Longitude");
        final String Latitude = intent.getStringExtra("Latitude");
        final String passwordText = intent.getStringExtra("password");
        lost_IDtest= intent.getStringExtra("lost_ID");


        detailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ListDetailActivity.this, PictureDetailActivity.class);
                intent1.putExtra("url", url);
                ListDetailActivity.this.startActivity(intent1);
            }
        });


        Glide.with(getApplicationContext()).load(url).into(detailImage);
        lost_ID.setText(lost_IDText);
        subject.setText(subjectText);
        place.setText(placeText);
        time.setText(timeText +"월 "+time2Text+"일 "+time3Text+"시 경");
        details.setText(detailText);
        password.setText(passwordText);

        new replyBackgroundTask().execute(lost_ID.getText().toString());

        Button replyRegister = (Button)findViewById(R.id.replyRegister);


/////////////////////////////////////즐겨찾기 이미지 변화

        SQLiteDatabase sqLiteDatabase =AppHelper.openDatabase(getApplicationContext(),"user1");
        String tableName = "userlist1";
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + tableName, null);

        int check = 1;

        if(cursor.getCount() == 0){


        }else{
            for(int i=0; i<cursor.getCount(); i++){
                cursor.moveToNext();
                if(lost_IDText.equals(cursor.getString(7))){
                    check = 0;
                    bookmarkButton.setImageResource(R.drawable.filled_star_icon);
                    break;
                }
                else
                {
                    check = 1;
                    bookmarkButton.setImageResource(R.drawable.empty_star_icon);
                }
            }
        }

        /////////////////////////////////////



        ImageButton goback = (ImageButton)findViewById(R.id.detail_gobackButton);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        replyRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                final EditText IDText = (EditText)findViewById(R.id.replyRegisterID);
                final EditText PWText = (EditText)findViewById(R.id.replyRegisterPW);
                final EditText ContentsText = (EditText)findViewById(R.id.replyRegisterContents);

                PWText.setFilters(new InputFilter[] {filter});

                final String ID;
                final String PW;
                final String Contents;
                final int lost_ID_register;


                if(ContentsText.getText().length() == 0 || IDText.getText().length() == 0 || PWText.getText().length() == 0)
                {
                    Toast.makeText(getApplicationContext(),"아이디, 비밀번호, 내용을 모두 입력 해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    lost_ID_register = Integer.parseInt(lost_ID.getText().toString());
                    ID = IDText.getText().toString();
                    PW = PWText.getText().toString();
                    Contents = ContentsText.getText().toString();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {

                        //서버로부터 여기서 데이터를 받음
                        @Override
                        public void onResponse(String response) {
                            try {

                                //서버로부터 받는 데이터는 JSON타입의 객체이다.
                                JSONObject jsonResponse = new JSONObject(response);
                                //그중 Key값이 "success"인 것을 가져온다.
                                boolean success = jsonResponse.getBoolean("success");

                                //회원 가입 성공시 success값이 true임
                                if (success) {

                                    //Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();

                                    //알림상자를 만들어서 보여줌
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ListDetailActivity.this);
                                    builder.setMessage("register success!!")
                                            .setPositiveButton("ok", null)
                                            .create()
                                            .show();
                                    IDText.setText("");
                                    PWText.setText("");
                                    ContentsText.setText("");


                                    //그리고 첫화면으로 돌아감
                                    /*Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    RegisterActivity.this.startActivity(intent);*/



                                }
                                //회원 가입 실패시 success값이 false임
                                else {
                                    Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();

                                    //알림상자를 만들어서 보여줌
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ListDetailActivity.this);
                                    builder.setMessage("register fail!!")
                                            .setNegativeButton("ok", null)
                                            .create()
                                            .show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };//responseListener 끝

                    //volley 사용법
                    //1. RequestObject를 생성한다. 이때 서버로부터 데이터를 받을 responseListener를 반드시 넘겨준다.
                    ////////////////////추가쿼리 변경 부분 //////////////////////


                    ReplyRegisterRequest registerRequest = new ReplyRegisterRequest(lost_ID_register,ID,PW,Contents,responseListener);
                    //2. RequestQueue를 생성한다.
                    RequestQueue queue = Volley.newRequestQueue(ListDetailActivity.this);
                    //3. RequestQueue에 RequestObject를 넘겨준다.
                    queue.add(registerRequest);

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    new replyBackgroundTask().execute(lost_ID.getText().toString());

                }


            }
        });


        //////////////////////////////북마크버튼
        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                index = 1;


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                SQLiteDatabase sqLiteDatabase =AppHelper.openDatabase(getApplicationContext(),"user1");
                String tableName = "userlist1";
                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + tableName, null);

                if(cursor.getCount() == 0){
                    //AppHelper.insertData(subjectText,placeText,timeText,time2Text,time3Text,detailText,lost_IDText,Latitude,Longitude,url);
                    index = 1;

                }else{
                    for(int i=0; i<cursor.getCount(); i++){
                        cursor.moveToNext();
                        if(lost_IDText.equals(cursor.getString(7))){
                            index = 0;
                        }
                    }
                }

                if(index == 0){
                    alertDialog.setMessage("이미 즐겨찾기된 물건 입니다.");
                    alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alertDialog.show();
                }else{
                    bookmarkButton.setImageResource(R.drawable.filled_star_icon);
                    alertDialog.setMessage("즐겨찾기에 추가 되었습니다.");
                    alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AppHelper.insertData(subjectText,placeText,timeText,time2Text,time3Text,detailText,lost_IDText,Latitude,Longitude,url, passwordText);
                        }
                    });
                    alertDialog.show();

                }




            }
        });


    }


    ///슬라이드식 액티비티 종료
    public void lockSlide(View v)
    {
        slidr.lock();
    }

    public void unlockSlide(View v)
    {
        slidr.unlock();
    }
    ///슬라이드식 액티비티 종료

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        Intent intent = getIntent();
        Double Latitude = Double.parseDouble(intent.getStringExtra("Latitude"));
        Double Longitude = Double.parseDouble(intent.getStringExtra("Longitude"));

        LatLng location = new LatLng(Longitude, Latitude);
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(location);
        map.addMarker(markerOptions);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
    }


    protected String uniToKsc(String uni) throws UnsupportedEncodingException {

        return new String (uni.getBytes("8859_1"),"KSC5601");

    }

    ////////////읽어오기
    class replyBackgroundTask extends AsyncTask<String,Void,String>
    {
        String target;

        @Override
        protected void onPreExecute(){
            target =  "http://skkj.dothome.co.kr/ReplyLookup.php";
        }



        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected  String doInBackground(String... lost_ID){
            try {

                String searchKeyword1 = lost_ID[0];
                String postParameters =  "lost_ID_Foreign="+searchKeyword1;


                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter =
                        new OutputStreamWriter(outputStream);
                PrintWriter printWriter =
                        new PrintWriter(outputStreamWriter);
                printWriter.write(postParameters);
                printWriter.flush();
                outputStream.close();


                /*OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(postParameters);
                writer.flush();
                writer.close();
                os.close();*/


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("responseStatusCode", ""+responseStatusCode);

                InputStream inputStream = httpURLConnection.getInputStream();


                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(temp);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("ResponseStringBuilder",stringBuilder.toString().trim());

                return StringEscapeUtils.unescapeJava(stringBuilder.toString().trim());///유니코드 한글 변환
                //return stringBuilder.toString().trim();

            }

            catch (Exception e){


                e.printStackTrace();

            }

            return null;
        }

        @Override
        public void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);
        }
        @Override
        public void onPostExecute(String result) {
            try{


                //EditText ex = (EditText)findViewById(R.id.editText6);
                TextView ex2 = (TextView)findViewById(R.id.asdf);

                String lost_ID, ReplyIndex ,ID,Password, Contents;

                //ex.setText(result.toString());
                ArrayList<String> list = new ArrayList<String>();
                String cut[];
                String temp = "";
                cut = result.split("\"");
                int j=0;


                for(int i = 0; i<cut.length; i++)
                {
                    if((i>1)&&(i-1)%4 == 0)
                    {
                        temp = temp+cut[i]+"#";
                        j++;
                        if(j==5)
                        {
                            list.add(temp);
                            temp="";
                            j=0;
                        }
                    }

                }
                ex2.setText(temp);
                //ex.setText(list.get(0));//확인용

///////////////////글쓴이//////////////////
                Intent intent = getIntent();
                String password = intent.getStringExtra("password");
                String admin = "0";
///////////////////글쓴이//////////////////

                int i = 0;
                while(i<list.size())
                {
                    lost_ID = list.get(i).split("#")[2];
                    ReplyIndex = list.get(i).split("#")[1];
                    ID =  list.get(i).split("#")[0];
                    Password = list.get(i).split("#")[3];
                    Contents = list.get(i).split("#")[4];
///////////////////글쓴이//////////////////
                    if(Password.equals(password))
                    {
                        admin = "1";
                    }
///////////////////글쓴이//////////////////

                    Reply reply = new Reply(lost_ID, ID, Password, ReplyIndex,Contents, admin);

                    replyList.add(reply);
                    ///////
                    admin = "0";
                    //////
                    i++;
                }


                /*JSONObject jsonObject = new JSONObject(result);//여기서 막힘
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                String data = "[{\"userName\": \"sandeep\",\"age\":30},{\"userName\": \"vivan\",\"age\":5}]  ";
                JSONArray jsonArray = new JSONArray(data);




                int count = 0;



                while(count < jsonArray.length()){
                    JSONObject object = jsonArray.getJSONObject(count);
                    lost_ID = object.getString("lost_ID_Foreign");
                    ReplyIndex = object.getString("ReplyIndex");
                    ID = object.getString("ID");
                    Password = object.getString("Password");
                    Contents = object.getString("Contents");


                    Reply reply = new Reply(lost_ID, ID, Password, ReplyIndex,Contents);

                    replyList.add(reply);
                    count++;
                }*/

            }catch (Exception e){
                e.printStackTrace();
                Log.d("jsonerror",e.toString());
            }

            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
            Log.d("response", "response - " + result);

            ListView replyListView = (ListView)findViewById(R.id.replyListView);


            adapter = new ReplyListAdapter(getApplicationContext(), replyList, ListDetailActivity.this,replySaveList, replyListView);

            replyListView.setAdapter(adapter);
            setListViewHeightBasedOnChildren(replyListView);
        }
    }
    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {

            // pre-condition

            return;

        }



        int totalHeight = 0;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);



        for (int i = 0; i < listAdapter.getCount(); i++) {

            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += listItem.getMeasuredHeight();

        }



        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);

        listView.requestLayout();

    }

}
