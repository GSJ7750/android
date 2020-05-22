package com.example.register;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CmDeletereplyRequest extends StringRequest {

    final static private String URL = "http://skj.dothome.co.kr/Cm_Deletereply.php";
    private Map<String, String> parameters;
    public CmDeletereplyRequest(String Cmr_Time, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);//Post방식임
        parameters = new HashMap<>();//해쉬맵 생성후 parameters 변수에 값을 넣어줌
        parameters.put("Cmr_Time", Cmr_Time);
    }
    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
