package com.example.register;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kch.proj_manager_v3.R;

public class CommunityItemView extends LinearLayout {


    TextView textView19;
    TextView textView20;
    TextView textView25;
    TextView textView28;

    public CommunityItemView(Context context) {
        super(context);

        init(context);

    }

    public CommunityItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.communityitem,this,true);

        textView19 = (TextView) findViewById(R.id.textView19);
        textView20 = (TextView) findViewById(R.id.textView20);
        textView25 = (TextView) findViewById(R.id.textView25);
        textView28 = (TextView) findViewById(R.id.textView28);
    }

    public void setTitle(String title){
        textView19.setText(title);
    }
    public void setContent(String content){ textView20.setText(content);}
    public void setWriteTime(String writetime){ textView25.setText(writetime);}
    public void setUserid(String userid){ textView28.setText(userid);}


}
