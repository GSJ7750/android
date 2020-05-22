package com.example.register;


import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kch.proj_manager_v3.R;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;


public class PublicDataActivity extends Activity {


    private final String dbName = "publicdata";
    private final String tableName = "publicdata";


    private SlidrInterface slidr;



    ArrayList<HashMap<String, String>> personList;
    ListView list;
    private List<PublicData> publicList;
    private List<PublicData> saveList;//회원검색 기능용 복사본



    PublicDataListAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_data);

        slidr = Slidr.attach(this);
        ImageButton goback = (ImageButton)findViewById(R.id.P_gobackButton);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try {
            boolean bResult = isCheckDB(getApplicationContext());  // DB가 있는지?
            Log.d("MiniApp", "DB Check="+bResult);
            if(!bResult){   // DB가 없으면 복사
                copyDB(getApplicationContext());
            }else{

            }
        } catch (Exception e) {

        }

        ArrayList<String> choices = new ArrayList<String>(
                Arrays.asList("품목 이름","노선 구간", "회사명", "보관 장소","물품 번호"));
        final Spinner choice = (Spinner)findViewById(R.id.P_choiceSpinner);
        ArrayAdapter<String> choiceAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,choices);
        choice.setAdapter(choiceAdapter);



        list = (ListView) findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String,String>>();

        publicList = new ArrayList<PublicData>();
        saveList = new ArrayList<PublicData>();

        adapter = new PublicDataListAdapter(getApplicationContext(), publicList, this, saveList);//로 수정됨
        list.setAdapter(adapter);

        showList();

        final EditText search = (EditText)findViewById(R.id.P_search);


        choice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                   // search.setHint(choice.getSelectedItem().toString());
                switch(i){
                    case 4:
                        search.setHint(choice.getSelectedItem().toString() + "  ex)61715612");
                        break;
                    case 0:
                        search.setHint(choice.getSelectedItem().toString() + "  ex)신발");
                        break;
                    case 1:
                        search.setHint(choice.getSelectedItem().toString() + "  ex)대치동");
                        break;
                    case 2:
                        search.setHint(choice.getSelectedItem().toString() + "  ex)경일운수, 고려운수 등");
                        break;
                    case 3:
                        search.setHint(choice.getSelectedItem().toString() + "  ex)경찰서");
                        break;
                }


            }



            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        final LinearLayout P_layout = (LinearLayout)findViewById(R.id.P_layout);

        list.setVisibility(View.GONE);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            list.setVisibility(View.VISIBLE);
            P_layout.setVisibility(View.GONE);
                if(charSequence.length() == 0)
                {
                    list.setVisibility(View.GONE);
                    P_layout.setVisibility(View.VISIBLE);
                }
                if(choice.getSelectedItem().toString() == "물품 번호")
                {

                    search.setHint(choice.getSelectedItem().toString());
                    searchNum(charSequence.toString());//회원 검색 기능용
                }
                else if(choice.getSelectedItem().toString() == "품목 이름")
                {
                    search.setHint(choice.getSelectedItem().toString());
                    searchName(charSequence.toString());//회원 검색 기능용
                }
                else if(choice.getSelectedItem().toString() == "노선 구간")
                {
                    search.setHint(choice.getSelectedItem().toString());
                    searchDetail(charSequence.toString());//회원 검색 기능용
                }
                else if(choice.getSelectedItem().toString() == "이동 수단")
                {
                    search.setHint(choice.getSelectedItem().toString());
                    searchCompany(charSequence.toString());//회원 검색 기능용
                }
                else if(choice.getSelectedItem().toString() == "보관 장소")
                {
                    search.setHint(choice.getSelectedItem().toString());
                    searchPlace(charSequence.toString());//회원 검색 기능용
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

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

    public boolean isCheckDB(Context mContext){
        String filePath = "/data/data/" + getPackageName() + "/databases/" + "publicdata";
        File file = new File(filePath);

        if (file.exists()) {
            return true;
        }

        return false;

    }



    public void copyDB(Context mContext){
        Log.d("MiniApp", "copyDB");
        AssetManager manager = mContext.getAssets();
        String folderPath = "/data/data/" + getPackageName() + "/databases";
        String filePath = "/data/data/" + getPackageName() + "/databases/" + "publicdata";
        File folder = new File(folderPath);
        File file = new File(filePath);

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            InputStream is = manager.open("publicdata");
            BufferedInputStream bis = new BufferedInputStream(is);

            if (folder.exists()) {
            }else{
                folder.mkdirs();
            }


            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            int read = -1;
            byte[] buffer = new byte[1024];
            while ((read = bis.read(buffer, 0, 1024)) != -1) {
                bos.write(buffer, 0, read);
            }

            bos.flush();

            bos.close();
            fos.close();
            bis.close();
            is.close();

        } catch (IOException e) {
            Log.e("ErrorMessage : ", e.getMessage());
        }
    }



    public void searchNum(String search){
        publicList.clear();
        for(int i = 0; i < saveList.size(); i++)
        {
            if(saveList.get(i).getnum()/*검색하는 기준*/.contains(search)){
                publicList.add(saveList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void searchName(String search){
        publicList.clear();
        for(int i = 0; i < saveList.size(); i++)
        {
            if(saveList.get(i).getcategoryName()/*검색하는 기준*/.contains(search) || (saveList.get(i).getcategory()/*검색하는 기준*/.contains(search))){
                publicList.add(saveList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void searchDetail(String search){
        publicList.clear();
        for(int i = 0; i < saveList.size(); i++)
        {
            if(saveList.get(i).getdetail()/*검색하는 기준*/.contains(search)){
                publicList.add(saveList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void searchCompany(String search){
        publicList.clear();
        for(int i = 0; i < saveList.size(); i++)
        {
            if(saveList.get(i).getcompany()/*검색하는 기준*/.contains(search)){
                publicList.add(saveList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void searchPlace(String search){
        publicList.clear();
        for(int i = 0; i < saveList.size(); i++)
        {
            if(saveList.get(i).getplace()/*검색하는 기준*/.contains(search)){
                publicList.add(saveList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }


    protected void showList(){

        try {

            SQLiteDatabase ReadDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);


            //SELECT문을 사용하여 테이블에 있는 데이터를 가져옵니다..
            Cursor c = ReadDB.rawQuery("SELECT * FROM publicdata order by 등록일자 desc", null);

            if (c != null) {


                if (c.moveToFirst()) {
                    do {

                        //테이블에서 두개의 컬럼값을 가져와서
                        String num          = c.getString(c.getColumnIndex("분실물SEQ"));
                        String status       = c.getString(c.getColumnIndex("분실물상태"));
                        String regDate      = c.getString(c.getColumnIndex("등록일자"));
                        String recivedDate  = c.getString(c.getColumnIndex("수령일자"));
                        String categoryName = c.getString(c.getColumnIndex("분실물명"));
                        String category     = c.getString(c.getColumnIndex("분실물종류"));
                        String zone         = c.getString(c.getColumnIndex("수령자치구"));
                        String company      = c.getString(c.getColumnIndex("수령위치(회사)"));
                        String detail       = c.getString(c.getColumnIndex("수령물건"));
                        String place        = c.getString(c.getColumnIndex("분실장소"));

                        PublicData publicData = new PublicData(num, regDate, recivedDate, status, categoryName, category, zone, company, detail ,place);
                        publicList.add(publicData);
                        saveList.add(publicData);






                    } while (c.moveToNext());
                }
            }

            ReadDB.close();


            //새로운 apapter를 생성하여 데이터를 넣은 후..



            //화면에 보여주기 위해 Listview에 연결합니다.
            list.setAdapter(adapter);


        } catch (SQLiteException se) {
            Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("",  se.getMessage());
        }

    }





}