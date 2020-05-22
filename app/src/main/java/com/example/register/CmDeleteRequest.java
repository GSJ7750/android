package com.example.register;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CmDeleteRequest extends StringRequest {

    final static private String URL = "http://skj.dothome.co.kr/Cm_Delete.php";
    private Map<String, String> parameters;
    public CmDeleteRequest(String Cm_Password,String Cm_Time, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);//Post방식임
        parameters = new HashMap<>();//해쉬맵 생성후 parameters 변수에 값을 넣어줌
        parameters.put("Cm_Password", Cm_Password);
        parameters.put("Cm_Time", Cm_Time);
    }
    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
