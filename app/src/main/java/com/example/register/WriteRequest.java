package com.example.register;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.sql.Struct;
import java.util.HashMap;
import java.util.Map;

public class WriteRequest extends StringRequest {

    final static private String URL = "http://skj.dothome.co.kr/Cm_Register.php";
    private Map<String,String> map;
    public WriteRequest(String Cm_Title, String Cm_Content, String Cm_Time, String Cm_User, String Cm_Password, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        map = new HashMap<>();
        map.put("Cm_Title",Cm_Title);
        map.put("Cm_Content",Cm_Content);
        map.put("Cm_Time",Cm_Time);
        map.put("Cm_User",Cm_User);
        map.put("Cm_Password",Cm_Password);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
