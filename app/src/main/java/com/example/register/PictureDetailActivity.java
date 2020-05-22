package com.example.register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.kch.proj_manager_v3.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

public class PictureDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_detail);



        PhotoView photoView = (PhotoView)findViewById(R.id.pictureImageView) ;

        Intent intent = getIntent();

        String url = intent.getStringExtra("url");
        Glide.with(getApplicationContext()).load(url).into(photoView);


    }
}
