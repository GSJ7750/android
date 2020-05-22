package com.example.register;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kch.proj_manager_v3.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListmapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    List<User> userList;
    User user;
    User restoreduser;
    GoogleMap map;
    JSONArray jsonArray;
    Intent intent;
    android.support.v4.app.FragmentManager fragmentManager1;
    FragmentTransaction fragmentTransaction;

/*
    @Override
    protected void onResume() {
        super.onResume();
        Intent intet = getIntent();
        finish();
        startActivity(intent);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listmap);
        fragmentManager1 = getSupportFragmentManager();

        intent = getIntent();
        fragmentManager = getFragmentManager();
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);
        userList = new ArrayList<User>();


        try{
            //intent로 값을 가져옵니다 이때 JSONObject타입으로 가져옵니다
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("userList"));


            //List.php 웹페이지에서 response라는 변수명으로 JSON 배열을 만들었음..
            jsonArray = jsonObject.getJSONArray("response");
            int count = 0;

            //////////////조회쿼리 변경부분 ////////////////////
            String subject , place , time, time2 ,time3, details ,lost_ID, Latitude, Longitude, url,password ;

            //JSON 배열 길이만큼 반복문을 실행
            while(count < jsonArray.length()){
                //count는 배열의 인덱스를 의미
                JSONObject object = jsonArray.getJSONObject(count);

                //////////////조회쿼리 변경부분 ////////////////////
                subject = object.getString("subject");//여기서 ID가 대문자임을 유의
                place = object.getString("place");
                time = object.getString("time");
                time2 = object.getString("time2");
                time3 = object.getString("time3");
                details = object.getString("details");
                lost_ID = object.getString("lost_ID");
                Latitude = object.getString("Latitude");
                Longitude = object.getString("Longitude");
                url = object.getString("url");
                password = object.getString("password");

                //값들을 User클래스에 묶어줍니다

                userList.add(new User(subject, place,time,time2,time3,details, lost_ID, Latitude, Longitude,url, password));//리스트뷰에 값을 추가해줍니다
                count++;
            }


        }catch(Exception e){
            e.printStackTrace();
        }




    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        if(NetworkStatus.getConnectivityStatus(ListmapActivity.this) != 3)
        {

            int i=0;
            String latitude[] = new String[jsonArray.length()];
            String longtitude[] = new String[jsonArray.length()];


            while(i<jsonArray.length()) {


                user = new User(userList.get(i).getSubject(), userList.get(i).getPlace(),userList.get(i).getTime(),userList.get(i).getTime2(),userList.get(i).getTime3(), userList.get(i).getDetails(), userList.get(i).getLost_ID(),userList.get(i).getLatitude(),userList.get(i).getLongitude(),userList.get(i).getUrl(),userList.get(i).getPassword());

                latitude[i] = userList.get(i).getLatitude();
                longtitude[i] = userList.get(i).getLongitude();

                Marker marker = map.addMarker(new MarkerOptions()
                        .position(new LatLng( Double.parseDouble(userList.get(i).getLongitude()), Double.parseDouble(userList.get(i).getLatitude()))));



                marker.setTag(user);
                i++;
            }
        }
        else{
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ListmapActivity.this);

            alertDialog.setTitle("3G/4G 경고알림");
            alertDialog.setMessage("데이터 셋팅이 되지 않았을수도 있습니다. \n 설정창으로 가시겠습니까?");

            // OK 를 누르게 되면 설정창으로 이동합니다.
            alertDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(intent);
                        }
                    });
            // Cancle 하면 종료 합니다.
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            alertDialog.show();
            Toast.makeText(getApplicationContext(),"인터넷 연결을 해주세요.", Toast.LENGTH_LONG).show();
        }


        LatLng middle = new LatLng(37.56412734646165,126.9858910551590);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(middle, (float) 11.105));


        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                itemfragment2 fragment = new itemfragment2();
                fragmentTransaction = fragmentManager1.beginTransaction();

                fragmentTransaction.setCustomAnimations(R.anim.slide_in_bottom,R.anim.slide_out_top);

                fragmentTransaction.replace(R.id.container,fragment);
                fragmentTransaction.commit();


            }
        });



        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                itemfragment fragment = new itemfragment();
                fragmentTransaction = fragmentManager1.beginTransaction();

                fragmentTransaction.setCustomAnimations(R.anim.slide_in_bottom,R.anim.slide_out_top);

                fragmentTransaction.replace(R.id.container,fragment); Log.d("마커클릭","ㅁㅁㅁ");
                fragmentTransaction.commit();
                // getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();

                // LatLng now_position = new LatLng(marker.getPosition().latitude-1.0, marker.getPosition().longitude-1.0);

                restoreduser = (User) marker.getTag();


                return false;
            }
        });
    }
        /*
        public void close(){
        fragmentManager.beginTransaction().remove(itemfragment.this).commit();
        }*/
}