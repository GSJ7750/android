package com.example.register;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kch.proj_manager_v3.R;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        AppHelper.openDatabase(getApplicationContext(),"user1");
        AppHelper.createTable("userlist1");

        ImageButton managementButton = (ImageButton)findViewById(R.id.manageButton);
        ImageButton registerButton = (ImageButton)findViewById(R.id.registerButton);
        ImageButton communityButton = (ImageButton)findViewById(R.id.communityButton);
        ImageButton mapButton = (ImageButton)findViewById(R.id.mapButton);
        ImageButton publicdataButton = (ImageButton)findViewById(R.id.publicdataButton);
        ImageButton bookmarkButton = (ImageButton)findViewById(R.id.bookmarkButton);
        final ImageButton copyrightButton = (ImageButton)findViewById(R.id.copyrightButton);


        copyrightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent copyrightIntent = new Intent(MainActivity.this, CopyrightActivity.class);
                MainActivity.this.startActivity(copyrightIntent);
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                MainActivity.this.startActivity(registerIntent);
            }
        });

        managementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundTask().execute();
            }
        });

        communityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent communityIntent = new Intent(getApplicationContext(),CommunityActivity.class);
                startActivity(communityIntent);
            }
        });
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundTask2().execute();
            }
        });

        publicdataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent publicdataintent = new Intent(getApplicationContext(),PublicDataActivity.class);
                startActivity(publicdataintent);
            }
        });
        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bookmarkintent = new Intent(getApplicationContext(),BookmarkActivity.class);
                startActivity(bookmarkintent);
            }
        });
    }

    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target;

        @Override
        protected void onPreExecute() {
            //User.php은 파싱으로 가져올 웹페이지
            target = "http://skj.dothome.co.kr/Lookup.php";

        }
        @Override
        protected String doInBackground(Void... voids) {




            try{
                URL url = new URL(target);//URL 객체 생성

                ////////////
                TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

                    public X509Certificate[] getAcceptedIssuers() {

                        return null;

                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {

                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {

                    }

                } };



                SSLContext sc = SSLContext.getInstance("SSL");

                sc.init(null, trustAllCerts, new SecureRandom());

                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                //////////////







                //URL을 이용해서 웹페이지에 연결하는 부분
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                //바이트단위 입력스트림 생성 소스는 httpURLConnection
                InputStream inputStream = httpURLConnection.getInputStream();

                //웹페이지 출력물을 버퍼로 받음 버퍼로 하면 속도가 더 빨라짐
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;

                //문자열 처리를 더 빠르게 하기 위해 StringBuilder클래스를 사용함
                StringBuilder stringBuilder = new StringBuilder();

                //한줄씩 읽어서 stringBuilder에 저장함
                while((temp = bufferedReader.readLine()) != null){
                    stringBuilder.append(temp + "\n");//stringBuilder에 넣어줌
                }

                //사용했던 것도 다 닫아줌
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("MaResponseStringBuilder",stringBuilder.toString().trim());
                return stringBuilder.toString().trim();//trim은 앞뒤의 공백을 제거함

            }catch(Exception e){
                e.printStackTrace();
            }
            return null;


        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(MainActivity.this, ManagementActivity.class);
            intent.putExtra("userList", result);//파싱한 값을 넘겨줌
            MainActivity.this.startActivity(intent);//ManagementActivity로 넘어감

        }



    }

    class BackgroundTask2 extends AsyncTask<Void, Void, String>
    {
        String target;

        @Override
        protected void onPreExecute() {
            //User.php은 파싱으로 가져올 웹페이지
            target = "http://skj.dothome.co.kr/Lookup.php";

        }
        @Override
        protected String doInBackground(Void... voids) {

            try{
                URL url = new URL(target);//URL 객체 생성

                //URL을 이용해서 웹페이지에 연결하는 부분
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                //바이트단위 입력스트림 생성 소스는 httpURLConnection
                InputStream inputStream = httpURLConnection.getInputStream();

                //웹페이지 출력물을 버퍼로 받음 버퍼로 하면 속도가 더 빨라짐
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;

                //문자열 처리를 더 빠르게 하기 위해 StringBuilder클래스를 사용함
                StringBuilder stringBuilder = new StringBuilder();

                //한줄씩 읽어서 stringBuilder에 저장함
                while((temp = bufferedReader.readLine()) != null){
                    stringBuilder.append(temp + "\n");//stringBuilder에 넣어줌
                }

                //사용했던 것도 다 닫아줌
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("MaResponseStringBuilder",stringBuilder.toString().trim());
                return stringBuilder.toString().trim();//trim은 앞뒤의 공백을 제거함

            }catch(Exception e){
                e.printStackTrace();
            }
            return null;


        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(MainActivity.this, ListmapActivity.class);
            intent.putExtra("userList", result);//파싱한 값을 넘겨줌
            MainActivity.this.startActivity(intent);//ManagementActivity로 넘어감

        }



    }




}

