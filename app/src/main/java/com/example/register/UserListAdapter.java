package com.example.register;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

public class UserListAdapter extends BaseAdapter {

    private Context context;
    private List<User> userList;
    private Activity parentActivity;//회원삭제 강의때 추가
    private List<User> saveList;
    //여기서 Actvitivy parentActivity가 추가됨 회원삭제 및 관리자기능 예제
    public UserListAdapter(Context context, List<User> userList, Activity parentActivity, List<User> saveList){
        this.context = context;
        this.userList = userList;
        this.parentActivity = parentActivity;//회원삭제 강의때 추가
        this.saveList = saveList;//회원검색 강의때 추가

    }



    public UserListAdapter(Context context, List<User> userList){
        this.context = context;
        this.userList = userList;
    }

    //출력할 총갯수를 설정하는 메소드
    @Override
    public int getCount() {
        return userList.size();
    }

    //특정한 유저를 반환하는 메소드
    @Override
    public Object getItem(int i) {
        return userList.get(i);
    }

    //아이템별 아이디를 반환하는 메소드
    @Override
    public long getItemId(int i) {
        return i;
    }

    //가장 중요한 부분
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(context, R.layout.user, null);


        //////////////조회쿼리 변경부분 ////////////////////
        //뷰에 다음 컴포넌트들을 연결시켜줌
        TextView subject = (TextView)v.findViewById(R.id.subject);
        TextView place = (TextView)v.findViewById(R.id.place);
        TextView time = (TextView)v.findViewById(R.id.time);
        TextView time2 = (TextView)v.findViewById(R.id.time2);
        TextView time3 = (TextView)v.findViewById(R.id.time3);
        TextView details = (TextView)v.findViewById(R.id.details);
        TextView Latitude = (TextView)v.findViewById(R.id.Latitude);
        TextView Longitude = (TextView)v.findViewById(R.id.Longitude);
        TextView url = (TextView)v.findViewById(R.id.url);
        ImageView imageView = (ImageView)v.findViewById(R.id.imageView); // 0913추가
        final TextView lost_ID = (TextView)v.findViewById(R.id.lost_ID);

        subject.setText(userList.get(i).getSubject());
        place.setText(userList.get(i).getPlace());
        lost_ID.setText(userList.get(i).getLost_ID());
        time.setText(userList.get(i).getTime());
        time2.setText(userList.get(i).getTime2());
        time3.setText(userList.get(i).getTime3());
        details.setText(userList.get(i).getDetails());
        Latitude.setText(userList.get(i).getLatitude());
        Longitude.setText(userList.get(i).getLongitude());
        url.setText(userList.get(i).getUrl());
        Glide.with(context).load(userList.get(i).getUrl()).into(imageView); // 0913 추가
        //////////////조회쿼리 변경부분 ////////////////////



        //이렇게하면 findViewWithTag를 쓸 수 있음 없어도 되는 문장임
        v.setTag(userList.get(i).getSubject());

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
                                userList.remove(i);//리스트에서 해당부분을 지워줌
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
                //여기서 UserListAdapter는 Activity에서 상속받은 클래스가 아니므로 Activity값을 생성자로 받아서 사용한다
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

