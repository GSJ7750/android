package com.example.register;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.kch.proj_manager_v3.R;

import org.json.JSONObject;

import java.util.List;


/**
 * Created by kch on 2018. 2. 17..
 */

public class ReplyListAdapter  extends BaseAdapter {

    private Context context;
    private List<Reply> replyList;
    private Activity parentActivity;//회원삭제 강의때 추가
    private List<Reply> replysaveList;
    private ListView listView;
    //여기서 Actvitivy parentActivity가 추가됨 회원삭제 및 관리자기능 예제
    public ReplyListAdapter(Context context, List<Reply> reply, Activity parentActivity, List<Reply> replysaveList, ListView listview){
        this.context = context;
        this.replyList = reply;
        this.parentActivity = parentActivity;//회원삭제 강의때 추가
        this.replysaveList = replysaveList;//회원검색 강의때 추가
        this.listView = listview;

    }



    public ReplyListAdapter(Context context, List<Reply> reply){
        this.context = context;
        this.replyList = reply;
    }

    //출력할 총갯수를 설정하는 메소드
    @Override
    public int getCount() {
        return replyList.size();
    }

    //특정한 유저를 반환하는 메소드
    @Override
    public Object getItem(int i) {
        return replyList.get(i);
    }

    //아이템별 아이디를 반환하는 메소드
    @Override
    public long getItemId(int i) {
        return i;
    }

    //가장 중요한 부분
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(context, R.layout.detail_view_reply, null);


        //////////////조회쿼리 변경부분 ////////////////////
        //뷰에 다음 컴포넌트들을 연결시켜줌

        TextView replylost_ID = (TextView)v.findViewById(R.id.replylost_IDTextVIew);
        TextView replyID = (TextView)v.findViewById(R.id.reply_IDTextView);
        TextView replyPW = (TextView)v.findViewById(R.id.reply_PW_TextView);
        TextView replyContents = (TextView)v.findViewById(R.id.replyContentsTextView);
        final TextView replyIndex = (TextView)v.findViewById(R.id.reply_index_TextView);

        replylost_ID.setText(replyList.get(i).getlost_ID());
        replyID.setText(replyList.get(i).getID());
        replyPW.setText(replyList.get(i).getPassword());
        replyContents.setText(replyList.get(i).getContents());
        replyIndex.setText(replyList.get(i).getReplyIndex());
        if(replyList.get(i).getAdmin() == "1")
        {
            replyID.setTypeface(replyID.getTypeface(), Typeface.BOLD);
            replyID.setTextColor(Color.parseColor("#7C4DFF"));
            replyID.setText("글쓴이");
        }


        //////////////조회쿼리 변경부분 ////////////////////



        //이렇게하면 findViewWithTag를 쓸 수 있음 없어도 되는 문장임
        v.setTag(replyList.get(i).getlost_ID());

        //삭제 버튼 객체 생성

        TextView deleteButton = (TextView)v.findViewById(R.id.deleteBtnTextView);


        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                builder.setTitle("댓글 지우기");
                builder.setMessage("정말로 지우시겠습니까?");
                final EditText et = new EditText(parentActivity);
                et.setHint("비밀번호를 입력해주세요");
                et.setInputType(0x00000081);
                et.setWidth(100);
                builder.setView(et);





                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        String value = et.getText().toString();


                        if(replyList.get(i).getPassword().equals(value))
                        {
                            Response.Listener<String> responseListener = new Response.Listener<String>(){
                                @Override
                                public void onResponse(String response) {
                                    try{
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");
                                        //Toast.makeText(context,String.valueOf(success),Toast.LENGTH_SHORT).show();
                                        //받아온 값이 success면 정상적으로 서버로부터 값을 받은 것을 의미함
                                        if(success){
                                            replyList.remove(i);//리스트에서 해당부분을 지워줌
                                            notifyDataSetChanged();//데이터가 변경된 것을 어댑터에 알려줌

                                            ListDetailActivity activity = new ListDetailActivity();
                                            activity.setListViewHeightBasedOnChildren(listView);

                                        }
                                    }
                                    catch(Exception e){
                                        Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
                                        Log.i("tagconvertstr", "["+response+"]");
                                        e.printStackTrace();
                                    }
                                }
                            };
                            //volley 사용법
                            //1. RequestObject를 생성한다. 이때 서버로부터 데이터를 받을 responseListener를 반드시 넘겨준다.
                            //위에서 userID를 final로 선언해서 아래 처럼 가능함
                            ReplyDeleteRequest replyDeleteRequest = new ReplyDeleteRequest(Integer.parseInt(replyIndex.getText().toString()), responseListener);

                            Toast.makeText(context,"댓글이 삭제되었습니다.",Toast.LENGTH_LONG).show();
                            //2. RequestQueue를 생성한다.
                            //#############제목 기준으로 지울거면 parseint 지우고 deleterequest에서 int형 String으로 바꾸고
                            //#############php.delete에서 변수명 바꿔주고 전달 자료형 i -> s로 바꿔줘야 함
                            //여기서 UserListAdapter는 Activity에서 상속받은 클래스가 아니므로 Activity값을 생성자로 받아서 사용한다

                            RequestQueue queue = Volley.newRequestQueue(parentActivity);
                            //3. RequestQueue에 RequestObject를 넘겨준다.
                            queue.add(replyDeleteRequest);
                        }
                        else
                        {
                            Toast.makeText(parentActivity,"비밀번호가 틀렸습니다.",Toast.LENGTH_SHORT).show();
                        };


                    }
                });

                builder.show();



                //4. 콜백 처리부분(volley 사용을 위한 ResponseListener 구현 부분)

            }//onclick
        });

        //만든뷰를 반환함
        return v;
    }



}


