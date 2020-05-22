package com.example.register;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ReplyRegisterRequest extends StringRequest {
    //현재 안드로이드앱을 에뮬레이터로 돌리므로 에뮬레이터가 설치된 서버에 있는 아파치 서버에 접근하려면
    //다음과 같이 10.0.2.2:포트번호 로 접근해야합니다 저는 8080 포트를 써서 다음과 같이 했습니다
    final static private String URL = "http://skkj.dothome.co.kr/ReplyRegister.php";
    private Map<String, String> parameters;

    //생성자////////////추가쿼리 변경부분//////////////////
    public ReplyRegisterRequest(int lost_ID_Foreign, String ID ,String Password, String Contents,Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("lost_ID_Foreign", lost_ID_Foreign+"");
        parameters.put("ID",ID);
        parameters.put("Password", Password);
        parameters.put("Contents", Contents);
        //분실 날짜, 물품 종류, 분실 장소, 간단한 설명, 분실 시각

    }//lost_ID_Foreign ,ReplyIndex ,ID ,Password, Contents

    //추후 사용을 위한 부분
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}


