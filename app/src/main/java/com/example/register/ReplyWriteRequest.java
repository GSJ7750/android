package com.example.register;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ReplyWriteRequest extends StringRequest {

    final static private String URL = "http://skj.dothome.co.kr/Cmr_Register.php";
    private Map<String, String> map;

    public ReplyWriteRequest(String Cmr_Reply, String Cmr_Time, String Cmr_Writetime,String Cmr_Userid, String Cmr_Password, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("Cmr_Reply", Cmr_Reply);
        map.put("Cmr_Time", Cmr_Time);
        map.put("Cmr_Writetime", Cmr_Writetime);
        map.put("Cmr_Userid", Cmr_Userid);
        map.put("Cmr_Password", Cmr_Password);
    }
        @Override
        protected Map<String, String> getParams () throws AuthFailureError {
            return map;
        }
}
