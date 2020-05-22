package com.example.register;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class RegisterRequest extends StringRequest {

    //현재 안드로이드앱을 에뮬레이터로 돌리므로 에뮬레이터가 설치된 서버에 있는 아파치 서버에 접근하려면
    //다음과 같이 10.0.2.2:포트번호 로 접근해야합니다 저는 8080 포트를 써서 다음과 같이 했습니다
    final static private String URL = "http://skj.dothome.co.kr/Register.php";
    private Map<String, String> parameters;

    //생성자////////////추가쿼리 변경부분//////////////////
    public RegisterRequest(String subject, String place, int time,String time2, String time3, String details, double Latitude, double Longitude,
                           String url, String password, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("subject", subject);
        parameters.put("place", place);
        parameters.put("details",details);
        parameters.put("time", time+"");//월
        parameters.put("time2", time2+"");//일
        parameters.put("time3", time3+"");//시간
        parameters.put("Latitude", Latitude+"");
        parameters.put("Longitude", Longitude+"");
        parameters.put("url", url);
        parameters.put("password", password);
        //분실 날짜, 물품 종류, 분실 장소, 간단한 설명, 분실 시각

    }

    //추후 사용을 위한 부분
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}

