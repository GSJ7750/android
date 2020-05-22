package com.example.register;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.kch.proj_manager_v3.R;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity{

    /////////////////////////

    private ListView listView;
    private UserListAdapter adapter;
    List<User> bookMarkUser;
    private List<User> saveList;//회원검색 기능용 복사본
    private final String tableName = "userlist1";
    private SlidrInterface slidr;

    ///////////////////////////////////////////////
    String sql = "select subject, place ,time, time2, time3, details, lost_ID, Latitude, Longitude, url, password from " + tableName;

    String subject;
    String place ;
    String time ;
    String time2 ;
    String time3 ;
    String details ;
    String lost_ID ;
    String Latitude ;
    String Longitude ;
    String url ;
    String password;

//////////////////////////////////////


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 || resultCode == RESULT_OK){
            bookMarkUser.clear();
            SQLiteDatabase sqLiteDatabase = AppHelper.openDatabase(getApplicationContext(),"user1");
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + tableName, null);
            for(int i=0; i<cursor.getCount(); i++){
                cursor.moveToNext();
                subject = cursor.getString(1);
                place = cursor.getString(2);
                time = cursor.getString(3);
                time2 = cursor.getString(4);
                time3 = cursor.getString(5);
                details = cursor.getString(6);
                lost_ID = cursor.getString(7);
                Latitude = cursor.getString(8);
                Longitude = cursor.getString(9);
                url = cursor.getString(10);
                password = cursor.getString(11);

                bookMarkUser.add(new User(subject,place,time,time2,time3,details,lost_ID,Latitude,Longitude,url,password));


            }


            adapter = new UserListAdapter(getApplicationContext(), bookMarkUser, this, saveList);//로 수정됨

            listView.setAdapter(adapter);

        }
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        slidr = Slidr.attach(this);

        /////////////////////////////
        bookMarkUser = new ArrayList<User>();
/*        TextView userListTextView = (TextView)findViewById(R.id.listView);

        //ManagementActivity는 MainActivity에서 호출되므로 호출시 넘겨준 데이터를 뿌려주는 역할을 한다
        Intent intent = getIntent();
        //intent.putExtra("userList", result); 에서 userList에 저장했으므로 아래와 같이 쓰게됨
        userListTextView.setText(intent.getStringExtra("userList"));*/

//////////////////////////////////////////////뒤로가기 버튼
        ImageButton goback = (ImageButton)findViewById(R.id.gobackButton);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        ///////////////////////////////////////////////////새로고침 부분

        ///////////////////////////////////////////////////




        listView = (ListView)findViewById(R.id.listView);
        SQLiteDatabase sqLiteDatabase = AppHelper.openDatabase(getApplicationContext(),"user1");
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + tableName, null);
        for(int i=0; i<cursor.getCount(); i++){
            cursor.moveToNext();
            subject = cursor.getString(1);
            place = cursor.getString(2);
            time = cursor.getString(3);
            time2 = cursor.getString(4);
            time3 = cursor.getString(5);
            details = cursor.getString(6);
            lost_ID = cursor.getString(7);
            Latitude = cursor.getString(8);
            Longitude = cursor.getString(9);
            url = cursor.getString(10);
            password = cursor.getString(11);

            bookMarkUser.add(new User(subject,place,time,time2,time3,details,lost_ID,Latitude,Longitude,url,password));


        }


        adapter = new UserListAdapter(getApplicationContext(), bookMarkUser, this, saveList);//로 수정됨

        listView.setAdapter(adapter);




/////////////////09/08//////////////////////////
        registerForContextMenu(listView);

//////////////////////////////////////////////////////////////////////////////항목 눌렀을때 자세히 보는 부분
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), BookMarkDetailActivity.class);
                intent.putExtra("lost_ID", bookMarkUser.get(i).getLost_ID());
                intent.putExtra("subject", bookMarkUser.get(i).getSubject());
                intent.putExtra("place", bookMarkUser.get(i).getPlace());
                intent.putExtra("time", bookMarkUser.get(i).getTime());
                intent.putExtra("time2", bookMarkUser.get(i).getTime2());
                intent.putExtra("time3", bookMarkUser.get(i).getTime3());
                intent.putExtra("details", bookMarkUser.get(i).getDetails());
                intent.putExtra("Latitude", bookMarkUser.get(i).getLatitude());
                intent.putExtra("Longitude",bookMarkUser.get(i).getLongitude());
                intent.putExtra("url", bookMarkUser.get(i).getUrl());
                intent.putExtra("password", bookMarkUser.get(i).getPassword());

                startActivityForResult(intent,1);
            }
        });
////////////////////////////////////////////




        //조회 코드 한 세트


        /*category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(category.getSelectedItem().toString() == "전체")
                {
                    search.setText("");
                }
                else
                {
                    search.setText(category.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });*/

///////////////////////////////////////////////////////////////////




    }
}
