package com.example.register;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.kch.proj_manager_v3.R;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;



/**
 * Created by kch on 2018. 2. 17..
 */

public class PublicDataListAdapter extends BaseAdapter {

    private Context context;
    private List<PublicData> publicList;
    private Activity parentActivity;//회원삭제 강의때 추가
    private List<PublicData> saveList;
    //여기서 Actvitivy parentActivity가 추가됨 회원삭제 및 관리자기능 예제
    public PublicDataListAdapter(Context context, List<PublicData> publicList, Activity parentActivity, List<PublicData> saveList){
        this.context = context;
        this.publicList = publicList;
        this.parentActivity = parentActivity;//회원삭제 강의때 추가
        this.saveList = saveList;//회원검색 강의때 추가

    }



    public PublicDataListAdapter(Context context, List<PublicData> publicList){
        this.context = context;
        this.publicList = publicList;
    }

    //출력할 총갯수를 설정하는 메소드
    @Override
    public int getCount() {
        return publicList.size();
    }

    //특정한 유저를 반환하는 메소드
    @Override
    public Object getItem(int i) {
        return publicList.get(i);
    }

    //아이템별 아이디를 반환하는 메소드
    @Override
    public long getItemId(int i) {
        return i;
    }

    //가장 중요한 부분
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(context, R.layout.publicdata, null);


        //////////////조회쿼리 변경부분 ////////////////////
        //뷰에 다음 컴포넌트들을 연결시켜줌
        TextView Num          = (TextView)v.findViewById(R.id.lost_num);
        TextView Status      = (TextView)v.findViewById(R.id.lost_status);
        TextView RegDate      = (TextView)v.findViewById(R.id.lost_regDate);
        TextView RecivedDate  = (TextView)v.findViewById(R.id.lost_recivedDate);
        TextView CategoryName = (TextView)v.findViewById(R.id.lost_categoryName);
        TextView Category        = (TextView)v.findViewById(R.id.lost_category);
        TextView Zone          = (TextView)v.findViewById(R.id.lost_zone);
        TextView Company        = (TextView)v.findViewById(R.id.lost_company);
        TextView Detail         = (TextView)v.findViewById(R.id.lost_detail);
        TextView Place          = (TextView)v.findViewById(R.id.lost_place);


        Num         .setText(publicList.get(i).getnum());
        Status      .setText(publicList.get(i).getstatus());
        RegDate     .setText(publicList.get(i).getregDate());
        RecivedDate .setText(publicList.get(i).getrecivedDate());
        CategoryName.setText(publicList.get(i).getcategoryName());
        Category    .setText(publicList.get(i).getcategory());
        Zone        .setText(publicList.get(i).getzone());
        Company     .setText(publicList.get(i).getcompany());
        Detail      .setText(publicList.get(i).getdetail());
        Place       .setText(publicList.get(i).getplace());

        ImageButton status = (ImageButton)v.findViewById(R.id.lost_status_button);
        TextView s_textview = (TextView)v.findViewById(R.id.statusTextview);
        if(publicList.get(i).getstatus().equals("보관")){
            status.setImageResource(R.drawable.basket_fill_icon);
            s_textview.setText("보관중");
            s_textview.setTextColor(Color.parseColor("#FF5722"));
        }
        else
        {
            status.setImageResource(R.drawable.basket_unfill_icon);
            s_textview.setText("수령됨");
            s_textview.setTextColor(Color.parseColor("#0288D1"));
        }

        //이렇게하면 findViewWithTag를 쓸 수 있음 없어도 되는 문장임
        v.setTag(publicList.get(i).getnum());

        //삭제 버튼 객체 생성
        /*
        Button deleteButton = (Button)v.findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //4. 콜백 처리부분(volley 사용을 위한 ResponseListener 구현 부분)
                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            //받아온 값이 success면 정상적으로 서버로부터 값을 받은 것을 의미함
                            if(success){
                                publicList.remove(i);//리스트에서 해당부분을 지워줌
                                notifyDataSetChanged();//데이터가 변경된 것을 어댑터에 알려줌
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                //volley 사용법
                //1. RequestObject를 생성한다. 이때 서버로부터 데이터를 받을 responseListener를 반드시 넘겨준다.
                //위에서 userID를 final로 선언해서 아래 처럼 가능함
                DeleteRequest deleteRequest = new DeleteRequest(Integer.parseInt(lost_ID.getText().toString()), responseListener);
                //2. RequestQueue를 생성한다.
                //#############제목 기준으로 지울거면 parseint 지우고 deleterequest에서 int형 String으로 바꾸고
                //#############php.delete에서 변수명 바꿔주고 전달 자료형 i -> s로 바꿔줘야 함
                //여기서 PublicDataListAdapter는 Activity에서 상속받은 클래스가 아니므로 Activity값을 생성자로 받아서 사용한다
                RequestQueue queue = Volley.newRequestQueue(parentActivity);
                //3. RequestQueue에 RequestObject를 넘겨준다.
                queue.add(deleteRequest);
            }//onclick
        });*/

/*
        View.OnCreateContextMenuListener vC = new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo contextMenuInfo) {
                contextMenu.add(0,0,0,"delete");
            }
        };

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, i + "번째 이미지 선택", Toast.LENGTH_SHORT).show();
            }
        });
        v.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view) {

                return true;
            }
        });*/


        //만든뷰를 반환함
        return v;
    }
}

