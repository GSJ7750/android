package com.example.register;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kch.proj_manager_v3.R;
import com.example.register.ListmapActivity;

public class itemfragment2 extends Fragment {
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
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_list2,container,false);
        // final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
       /* final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
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
                fragmentTransaction.remove(itemfragment2.this);
                fragmentTransaction.commit();
                //fragmentManager.beginTransaction().remove(itemfragment.this).commit();


            }
        });*/


        return rootView;
    }

}