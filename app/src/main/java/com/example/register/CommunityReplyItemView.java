package com.example.register;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kch.proj_manager_v3.R;

public class CommunityReplyItemView extends LinearLayout {

    TextView textView27;
    TextView textView29;
    TextView textView30;

    public CommunityReplyItemView(Context context) {
        super(context);
        init(context);
    }

    public CommunityReplyItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.communityreplyitem,this,true);

        textView27 = (TextView) findViewById(R.id.textView27);
        textView29 = (TextView) findViewById(R.id.textView29);
        textView30 = (TextView) findViewById(R.id.textView30);

    }
    public void setReplyName(String nickname){
        if(nickname.equals("글쓴이"))
        {
            textView27.setTypeface(textView27.getTypeface(), Typeface.BOLD);
            textView27.setTextColor(Color.parseColor("#7C4DFF"));
            textView27.setText("글쓴이");
        }
        else{
        textView27.setText(nickname);} }
    public void setReplyContent(String reply){ textView29.setText(reply);}
    public void setReplyTime(String date){ textView30.setText(date);}
    public TextView getTextView30() {
        return textView30;
    }
}
