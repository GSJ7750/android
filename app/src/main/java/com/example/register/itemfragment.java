package com.example.register;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.kch.proj_manager_v3.R;

import java.util.List;

public class itemfragment extends Fragment {
    ListmapActivity activity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (ListmapActivity)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_list,container,false);
        // final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        TextView subject = (TextView)rootView.findViewById(R.id.subject);
        TextView place = (TextView)rootView.findViewById(R.id.place);
        TextView time = (TextView)rootView.findViewById(R.id.time);
        TextView detail = (TextView)rootView.findViewById(R.id.detail);
        ImageView imageView = rootView.findViewById(R.id.imageView);
        Button button = rootView.findViewById(R.id.button);

        subject.setText(activity.restoreduser.getSubject());
        place.setText(activity.restoreduser.getPlace());
        time.setText(activity.restoreduser.getTime() + "월" + activity.restoreduser.getTime2() + "일" + activity.restoreduser.getTime3() + "시");
        detail.setText(activity.restoreduser.getDetails());
        //Toast.makeText(rootView.getContext(),activity.restoreduser.getUrl(),Toast.LENGTH_SHORT).show();
        Glide.with(rootView.getContext()).load(activity.restoreduser.getUrl()).into(imageView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_top,R.anim.slide_out_bottom);
                fragmentTransaction.remove(itemfragment.this);
                fragmentTransaction.commit();
                //fragmentManager.beginTransaction().remove(itemfragment.this).commit();


            }
        });

        Button goButton = (Button)rootView.findViewById(R.id.GoButton);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ListDetailActivity.class);
                intent.putExtra("lost_ID",  activity.restoreduser.getLost_ID());
                intent.putExtra("subject",         activity.restoreduser.getSubject());
                intent.putExtra("place",           activity.restoreduser.getPlace());
                intent.putExtra("time",            activity.restoreduser.getTime());
                intent.putExtra("time2",           activity.restoreduser.getTime2());
                intent.putExtra("time3",           activity.restoreduser.getTime3());
                intent.putExtra("details",         activity.restoreduser.getDetails());
                intent.putExtra("Latitude",        activity.restoreduser.getLatitude());
                intent.putExtra("Longitude",       activity.restoreduser.getLongitude());
                intent.putExtra("url",             activity.restoreduser.getUrl());
                intent.putExtra("password",        activity.restoreduser.getPassword());

                startActivity(intent);
            }
        });


        return rootView;
    }

}