package com.example.register;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.kch.proj_manager_v3.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class CommunitySearchActivity extends AppCompatActivity {

    Button backbutton5;
    Button searchbutton6;
    EditText searchtext5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_search);

        backbutton5 = findViewById(R.id.backbutton5);
        searchtext5 = findViewById(R.id.searchtext5);
        searchbutton6 = findViewById(R.id.searchbutton6);

        backbutton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchbutton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }

/*

    class BackgroundTask extends AsyncTask<Void,Void,String>
    {
        String target;

        @Override
        protected void onPreExecute(){
            target =  "http://skj.dothome.co.kr/Cm_Requset.php";
        }

        @Override
        protected  String doInBackground(Void... voids){
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(temp+'\n');
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (Exception e){
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
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String Cm_Title,Cm_Content,Cm_Time,Cm_User,Cm_Password;
                while(count < jsonArray.length()){
                    JSONObject object = jsonArray.getJSONObject(count);
                    Cm_Title = object.getString("Cm_Title");
                    Cm_Content = object.getString("Cm_Content");
                    Cm_Time = object.getString("Cm_Time");
                    Cm_User = object.getString("Cm_User");
                    Cm_Password = object.getString("Cm_Password");
                    communityList.add(new CommunityItem(Cm_Title,Cm_Content,Cm_Time,Cm_User,Cm_Password));
                    adapter.addItem(communityList.get(count));
                    count++;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            listView.setAdapter(adapter);
        }
    }

    public void onBackPressed() {
        finish();
    }
    */
}
