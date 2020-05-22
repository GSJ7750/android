package com.example.register;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.kch.proj_manager_v3.R;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.support.v7.app.AppCompatActivity;

public class ManagementActivity extends AppCompatActivity{

    private ListView listView;
    private UserListAdapter adapter;
    private List<User> userList;
    private List<User> saveList;//회원검색 기능용 복사본


    private SlidrInterface slidr;
////////////////////////////09/08///////////////
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,0,0,"삭제");
    }



/////////////////////////////////////////길게 눌렀을 때 삭제 메뉴 뜨는 부분
    public boolean onContextItemSelected(final MenuItem item)
    {
        final TextView lost_ID = (TextView)listView.findViewById(R.id.lost_ID);

        switch (item.getItemId())
        {
            case 0:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("확인").setMessage("정말 지우시겠습니까?");
                final EditText et = new EditText(ManagementActivity.this);
                et.setHint("비밀번호를 입력해주세요.");
                et.setInputType(0x00000081);
                et.setWidth(100);
                alertDialogBuilder.setView(et);
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {

                        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                        String password = userList.get(info.position).getPassword().toString();
                        String value = et.getText().toString();


                        if(password.equals(value)) {
                            Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");
                                        //받아온 값이 success면 정상적으로 서버로부터 값을 받은 것을 의미함
                                        if (success) {
                                            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                                            userList.remove(info.position);//context menu가 선택된 객체의 위치
                                            saveList.remove(info.position);

                                            adapter.notifyDataSetChanged();//데이터가 변경된 것을 어댑터에 알려줌
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            DeleteRequest deleteRequest = new DeleteRequest(Integer.parseInt(userList.get(info.position).getLost_ID().toString()), responseListener);
                            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                            queue.add(deleteRequest);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"비밀번호가 틀렸습니다.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Toast.makeText(getApplicationContext(), "Cancel Click", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;



        }
        return true;
    }
///////////////////////////////////////////////



//////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
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

        slidr = Slidr.attach(this);

        ///////////////////////////////////////////////////새로고침 부분
        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {finish();
                new RefreshRequest(getApplicationContext()).execute();
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });
        ///////////////////////////////////////////////////


        Intent intent = getIntent();

        listView = (ListView)findViewById(R.id.listView);
        userList = new ArrayList<User>();
        saveList = new ArrayList<User>();

        //어댑터 초기화부분 userList와 어댑터를 연결해준다.
        //adapter = new UserListAdapter(getApplicationContext(), userList, this);
        adapter = new UserListAdapter(getApplicationContext(), userList, this, saveList);//로 수정됨

        listView.setAdapter(adapter);


        try{
            //intent로 값을 가져옵니다 이때 JSONObject타입으로 가져옵니다
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("userList"));


            //List.php 웹페이지에서 response라는 변수명으로 JSON 배열을 만들었음..
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;

            //////////////조회쿼리 변경부분 ////////////////////
            String subject , place , time, time2 ,time3, details ,lost_ID, Latitude, Longitude, url, password ;

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
                User user = new User(subject, place,time,time2,time3,details, lost_ID, Latitude, Longitude, url, password);
                userList.add(user);//리스트뷰에 값을 추가해줍니다
                saveList.add(user);
                count++;
            }


        }catch(Exception e){
            e.printStackTrace();
        }
/////////////////09/08//////////////////////////
        registerForContextMenu(listView);

//////////////////////////////////////////////////////////////////////////////항목 눌렀을때 자세히 보는 부분
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ListDetailActivity.class);
                intent.putExtra("lost_ID", userList.get(i).getLost_ID());
                intent.putExtra("subject", userList.get(i).getSubject());
                intent.putExtra("place", userList.get(i).getPlace());
                intent.putExtra("time", userList.get(i).getTime());
                intent.putExtra("time2", userList.get(i).getTime2());
                intent.putExtra("time3", userList.get(i).getTime3());
                intent.putExtra("details", userList.get(i).getDetails());
                intent.putExtra("Latitude", userList.get(i).getLatitude());
                intent.putExtra("Longitude",userList.get(i).getLongitude());
                intent.putExtra("url", userList.get(i).getUrl());
                intent.putExtra("password", userList.get(i).getPassword());

                startActivity(intent);
            }
        });
////////////////////////////////////////////
        ArrayList<String> choices = new ArrayList<String>(
                Arrays.asList("물품 번호", "품목 이름","발견 위치","발견 날짜","설명"));
        final Spinner choice = (Spinner)findViewById(R.id.choiceSpinner);
        ArrayAdapter<String> choiceAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,choices);
        choice.setAdapter(choiceAdapter);

        ArrayList<String> categories = new ArrayList<String>(
                Arrays.asList("전체","악세사리","현금","카드,통장","가방","상,하의","신발","책","전자기기","의약,의료용품","기타"));
        final Spinner category = (Spinner)findViewById(R.id.categorySearch);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,categories);
        category.setAdapter(categoryAdapter);

        final ArrayList<String> months = new ArrayList<String>(
                Arrays.asList("","1","2","3","4","5","6","7","8","9","10","11","12"));
        final ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,months);

        ArrayList<String> days = new ArrayList<String>(
                Arrays.asList("","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27"
                        ,"28","29","30","31"));
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,days);

        final ArrayList<String> times = new ArrayList<String>(
                Arrays.asList("","00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"));
        final ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,times);

        final Spinner monthsearchSpinner = (Spinner)findViewById(R.id.monthsearchSpinner);
        monthsearchSpinner.setAdapter(monthAdapter);

        final Spinner datesearchSpinner = (Spinner)findViewById(R.id.datesearchSpinner);
        datesearchSpinner.setAdapter(dayAdapter);

        final Spinner timesearchSpinner = (Spinner)findViewById(R.id.timesearchSpinner);
        timesearchSpinner.setAdapter(timeAdapter);



    //조회 코드 한 세트
        final EditText search = (EditText)findViewById(R.id.search);
        final EditText search2 = (EditText)findViewById(R.id.search2);

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
        final TextView monthTextView = (TextView)findViewById(R.id.monthTextView);
        final TextView dateTextView = (TextView)findViewById(R.id.dateTextView);
        final TextView timeTextView = (TextView)findViewById(R.id.timeTextView);
        final LinearLayout MDTcontainer = (LinearLayout)findViewById(R.id.MDTcontainer);



        choice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                search.setText("");
                if(choice.getSelectedItem().toString() == "발견 날짜")
                {
                    search2.setText("");
                    search.setHint(choice.getSelectedItem().toString());
                    monthsearchSpinner.setSelection(0);
                    datesearchSpinner.setSelection(0);
                    timesearchSpinner.setSelection(0);
                    MDTcontainer.setVisibility(View.VISIBLE);


                    monthsearchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {



                            if(datesearchSpinner.getSelectedItem().toString() == "" && timesearchSpinner.getSelectedItem().toString() == "") {
                                searchMonth(monthsearchSpinner.getSelectedItem().toString());
                            }
                            else
                            {
                                search2.setText(monthsearchSpinner.getSelectedItem().toString()+datesearchSpinner.getSelectedItem().toString()
                                        +timesearchSpinner.getSelectedItem().toString());

                                searchMDT(search2.getText().toString());
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    datesearchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            if(monthsearchSpinner.getSelectedItem().toString() == "" && timesearchSpinner.getSelectedItem().toString() == "")
                            {
                                searchDate(datesearchSpinner.getSelectedItem().toString());
                            }

                            else
                            {
                                search2.setText(monthsearchSpinner.getSelectedItem().toString()+datesearchSpinner.getSelectedItem().toString()
                                        +timesearchSpinner.getSelectedItem().toString());

                                searchMDT(search2.getText().toString());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    timesearchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if(datesearchSpinner.getSelectedItem().toString() == "" && monthsearchSpinner.getSelectedItem().toString() == "")
                            {
                                searchTime(timesearchSpinner.getSelectedItem().toString());
                            }

                            else
                            {
                                search2.setText(monthsearchSpinner.getSelectedItem().toString()+datesearchSpinner.getSelectedItem().toString()
                                        +timesearchSpinner.getSelectedItem().toString());

                                searchMDT(search2.getText().toString());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                }
                else
                {
                    MDTcontainer.setVisibility(View.GONE);

                    search.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if(choice.getSelectedItem().toString() == "물품 번호")
                            {
                                search.setHint(choice.getSelectedItem().toString());
                                searchLost_ID(charSequence.toString());//회원 검색 기능용
                            }
                            else if(choice.getSelectedItem().toString() == "품목 이름")
                            {
                                search.setHint(choice.getSelectedItem().toString());
                                searchSubject(charSequence.toString());//회원 검색 기능용
                            }
                            else if(choice.getSelectedItem().toString() == "발견 위치")
                            {
                                search.setHint(choice.getSelectedItem().toString());
                                searchPlace(charSequence.toString());//회원 검색 기능용
                            }
                            else if(choice.getSelectedItem().toString() == "설명")
                            {
                                search.setHint(choice.getSelectedItem().toString());
                                searchDetails(charSequence.toString());//회원 검색 기능용
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }

                    });

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




/////////////////////////////////////////////////////////////////////////////


        ///////////////////////////여기까지/////////////////////////////
        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent1 = new Intent(getApplicationContext(), DetailviewActivity.class);
                startActivity(intent1);
            }
        });*/



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

//"물품 번호", "품목 이름","발견 위치","발견 시각","설명"
    public void searchLost_ID(String search){
        userList.clear();
        for(int i = 0; i < saveList.size(); i++)
        {
            if(saveList.get(i).getLost_ID()/*검색하는 기준*/.contains(search)){//contains메소드로 search 값이 있으면 true를 반환함
                userList.add(saveList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void searchSubject(String search){
        userList.clear();
        for(int i = 0; i < saveList.size(); i++)
        {
            if(saveList.get(i).getSubject()/*검색하는 기준*/.contains(search)){
                userList.add(saveList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void searchPlace(String search){
        userList.clear();
        for(int i = 0; i < saveList.size(); i++)
        {
            if(saveList.get(i).getPlace()/*검색하는 기준*/.contains(search)){
                userList.add(saveList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }


    public void searchDetails(String search){
        userList.clear();
        for(int i = 0; i < saveList.size(); i++)
        {
            if(saveList.get(i).getDetails()/*검색하는 기준*/.contains(search)){
                userList.add(saveList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void searchMDT(String search){
        userList.clear();
        for(int i = 0; i < saveList.size(); i++)
        {
            if(saveList.get(i).getMDT()/*검색하는 기준*/.contains(search)){
                userList.add(saveList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void searchMonth(String search){
        userList.clear();
        for(int i = 0; i < saveList.size(); i++)
        {
            if(saveList.get(i).getTime()/*검색하는 기준*/.contains(search)){
                userList.add(saveList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void searchDate(String search){
        userList.clear();
        for(int i = 0; i < saveList.size(); i++)
        {
            if(saveList.get(i).getTime2()/*검색하는 기준*/.contains(search)){
                userList.add(saveList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void searchTime(String search){
        userList.clear();
        for(int i = 0; i < saveList.size(); i++)
        {
            if(saveList.get(i).getTime3()/*검색하는 기준*/.contains(search)){
                userList.add(saveList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

}


