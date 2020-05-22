package com.example.register;
import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.TooltipCompat;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kch.proj_manager_v3.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity implements OnMapReadyCallback {
    EditText editLocation; // 0913추가
    ImageButton locationSearch; // 0913추가
    Button upload;// 0913추가
    ImageView imageView;// 0913추가
    ImageButton locationButton;
    final int CODE_GALLERY_REQUEST = 999;//0913추가
    Bitmap bitmap;//0913추가
    String URL = "http://skj.dothome.co.kr/upload.php";//0913추가
    String name; // 0913추가
    Geocoder geocoder;
    private FragmentManager fragmentManager;
    private MapFragment mapFragment;

    double latitude;
    double longitude;
    GoogleMap map;
    List<Address> list = null;
    String cut[];

    //////////////////////////////////////////0917 추가
    LocationManager locationManager;
    final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    boolean isAccessFineLocation = false;
    boolean isAccessCoarseLocation = false;
    boolean isPermission = false;
    double currentLat;
    double currentLon;

    // GPSTracker class
    GpsInfo gps;
///////////////////////////////////////////////////////

    protected InputFilter filter= new InputFilter() {

        public CharSequence filter(CharSequence source, int start, int end,

                                   Spanned dest, int dstart, int dend) {



            Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");

            if (!ps.matcher(source).matches()) {

                return "";

            }

            return null;

        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        locationButton = (ImageButton)findViewById(R.id.locationButton);
        //0913추가
        locationSearch = (ImageButton)findViewById(R.id.locationSearch);
        editLocation = (EditText)findViewById(R.id.editLocation);
        upload = (Button)findViewById(R.id.upload);
        imageView = (ImageView)findViewById(R.id.imageView);
        //0913추가
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        RegisterActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST);
            }
        });



        //지도 전용 변수들
        geocoder = new Geocoder(this);
        fragmentManager = getFragmentManager();
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);


        final EditText subjectText = (EditText)findViewById(R.id.subjectText);

        ArrayList<String> categories = new ArrayList<String>(
                Arrays.asList("악세사리","현금","카드,통장","가방","상,하의","신발","책","전자기기","의약,의료용품","기타"));
        final Spinner category = (Spinner)findViewById(R.id.categorySpinner);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,categories);
        category.setAdapter(categoryAdapter);

        final EditText placeText = (EditText)findViewById(R.id.zoneText);
        /*ArrayList<String> places = new ArrayList<String>(
                Arrays.asList(
                        "서울시 전체",
                        "강남구",
                        "강동구",
                        "강북구",
                        "강서구",
                        "관악구",
                        "광진구",
                        "구로구",
                        "금천구",
                        "노원구",
                        "도봉구",
                        "동대문구",
                        "동작구",
                        "마포구",
                        "서대문구",
                        "서초구",
                        "성동구",
                        "성북구",
                        "송파구",
                        "양천구",
                        "영등포구",
                        "용산구",
                        "은평구",
                        "종로구",
                        "중구",
                        "중랑구"
                ));
        final Spinner placeSpinner = (Spinner)findViewById(R.id.placesSpinner);
        ArrayAdapter<String>  arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,places);
        placeSpinner.setAdapter(arrayAdapter);*/


        // EditText timeText = (EditText)findViewById(R.id.timeText);
        ArrayList<String> months = new ArrayList<String>(
                Arrays.asList("1","2","3","4","5","6","7","8","9","10","11","12"));
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,months);
//1 3 5 7 8 10 12
        ArrayList<String> days = new ArrayList<String>(
                Arrays.asList("01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27"
                ,"28","29","30","31"));
        final ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,days);

        ArrayList<String> _30days = new ArrayList<String>(
                Arrays.asList("01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27"
                        ,"28","29","30"));
        final ArrayAdapter<String> _30dayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,_30days);

        ArrayList<String> _29days = new ArrayList<String>(
                Arrays.asList("01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27"
                        ,"28","29"));
        final ArrayAdapter<String> _29dayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,_29days);

        ArrayList<String> times = new ArrayList<String>(
                Arrays.asList("00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"));
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,times);

        final Spinner timeSpinner = (Spinner)findViewById(R.id.timeSpinner);
        timeSpinner.setAdapter(monthAdapter);

        final Spinner timeSpinner2 = (Spinner)findViewById(R.id.timeSpinner2);
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String month = timeSpinner.getSelectedItem().toString();
                if(month == "1" || month == "3" || month == "5" || month == "7" || month == "8" || month == "10" || month == "12")
                {
                    timeSpinner2.setAdapter(dayAdapter);
                }
                else if(month == "2")
                {
                    timeSpinner2.setAdapter(_29dayAdapter);
                }
                else
                {
                    timeSpinner2.setAdapter(_30dayAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        final Spinner timeSpinner3 = (Spinner)findViewById(R.id.timeSpinner3);
        timeSpinner3.setAdapter(timeAdapter);

        final EditText LatitudeText = (EditText)findViewById(R.id.LatitudeText);
        final EditText LongitudeText = (EditText)findViewById(R.id.LongitudeText);
        final EditText detailsText = (EditText)findViewById(R.id.detailsText);

        final EditText passwordText = (EditText)findViewById(R.id.passwordText);

        ImageButton backward = (ImageButton)findViewById(R.id.gobackButton2);
        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        passwordText.setFilters(new InputFilter[] {filter});

        /////////////0913 싹다바꿧음 아래 요청부분
        Button regbtn = (Button)findViewById(R.id.registerbtn);
        regbtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                if(LatitudeText.getText().toString().length() == 0)
                {
                    Toast.makeText(RegisterActivity.this, "위치를 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(passwordText.length() == 0)
                {
                    Toast.makeText(RegisterActivity.this, "비밀번호를 입력 해 주세요.",Toast.LENGTH_LONG).show();
                }
                else if(imageView.getDrawable() == null)
                {
                    Toast.makeText(RegisterActivity.this, "이미지를 삽입 해 주세요.",Toast.LENGTH_SHORT).show();
                }
                else if(detailsText.length() == 0)
                {
                    Toast.makeText(RegisterActivity.this, "물품에 대한 설명을 입력 해 주세요.",Toast.LENGTH_SHORT).show();
                }
                else
                {


                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            String subject = category.getSelectedItem().toString();//스피너
                            String place = placeText.getText().toString();
                            String details = detailsText.getText().toString();
                            int time = Integer.parseInt(timeSpinner.getSelectedItem().toString());
                            String time2 = (timeSpinner2.getSelectedItem().toString());
                            String time3 = (timeSpinner3.getSelectedItem().toString());
                            String password = passwordText.getText().toString();
                            double Latitude = Double.parseDouble(LatitudeText.getText().toString());
                            double Longitude = Double.parseDouble(LongitudeText.getText().toString());
                            name = response.substring(4, 23);
                            String url = "http://skj.dothome.co.kr/" + name;
                            Response.Listener<String> responseListener = new Response.Listener<String>() {

                                //서버로부터 여기서 데이터를 받음
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        //서버로부터 받는 데이터는 JSON타입의 객체이다.
                                        JSONObject jsonResponse = new JSONObject(response);
                                        //그중 Key값이 "success"인 것을 가져온다.
                                        boolean success = jsonResponse.getBoolean("success");

                                        //회원 가입 성공시 success값이 true임
                                        if (success) {

                                            //Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();

                                            //알림상자를 만들어서 보여줌
                                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                            builder.setMessage("register success!!")
                                                    .setPositiveButton("ok", null)
                                                    .create()
                                                    .show();

                                            //그리고 첫화면으로 돌아감
                                    /*Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    RegisterActivity.this.startActivity(intent);*/
                                            finish();

                                        }
                                        //회원 가입 실패시 success값이 false임
                                        else {
                                            Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();

                                            //알림상자를 만들어서 보여줌
                                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                            builder.setMessage("register fail!!")
                                                    .setNegativeButton("ok", null)
                                                    .create()
                                                    .show();

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            };//responseListener 끝

                            //volley 사용법
                            //1. RequestObject를 생성한다. 이때 서버로부터 데이터를 받을 responseListener를 반드시 넘겨준다.
                            ////////////////////추가쿼리 변경 부분 //////////////////////
                            RegisterRequest registerRequest = new RegisterRequest(subject, place, time, time2,time3, details, Latitude, Longitude, url,password, responseListener);
                            //2. RequestQueue를 생성한다.
                            RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                            //3. RequestQueue에 RequestObject를 넘겨준다.
                            queue.add(registerRequest);



                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            String imageData = imageToString(bitmap);
                            params.put("image", imageData);

                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                    requestQueue.add(stringRequest);
                }



                ImageButton goback = (ImageButton)findViewById(R.id.gobackButton2);
                goback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                }
            });


        editLocation.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //Enter키눌렀을떄 처리
                    locationSearch.performClick();
                    return true;
                }
                return false;
            }
        });

        final EditText zoneText = (EditText) findViewById(R.id.zoneText);
        zoneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editLocation.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });
        Toast.makeText(RegisterActivity.this,"비밀번호는 글 삭제와 댓글 창에서의 글쓴이 식별에 사용됩니다.\n남들이 쉽게 알 수 없는 비밀번호를 사용 해 주세요.",Toast.LENGTH_LONG).show();


    }




    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map = googleMap;
        final EditText LatitudeText = (EditText)findViewById(R.id.LatitudeText);
        final EditText LongitudeText = (EditText)findViewById(R.id.LongitudeText);
        final EditText zoneText = (EditText)findViewById(R.id.zoneText);

        final MarkerOptions markerOptions = new MarkerOptions();

        LatLng middle = new LatLng(37.56412734646165, 126.9858910551590); //0913수정
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(middle, (float) 11.105));//0913수정
        zoneText.setText("서울특별시"); //0913수정




        locationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // 권한 요청을 해야 함
                /*if (isPermission==false) {


                    locationButton.setImageResource(R.drawable.my_position_clicked);
                    return;
                }
                else
                {*/
                    callPermission();
                    gps = new GpsInfo(RegisterActivity.this);
                    // GPS 사용유무 가져오기
                    if (gps.isGetLocation()) {

                        currentLat = gps.getLatitude();
                        currentLon = gps.getLongitude();



                        //Toast.makeText(getApplicationContext(), "당신의 위치 - \n위도: " + currentLat + "\n경도: " + currentLon, Toast.LENGTH_LONG).show();
                        markerOptions.title("현재위치");
                        markerOptions.position(new LatLng(currentLat,currentLon));


                        // 마커 생성
                        // 마커 추가
                        map.clear();
                        map.addMarker(markerOptions);


                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLat,currentLon), 16));



                        try {
                            list = geocoder.getFromLocation(currentLat, currentLon, 10);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        LatitudeText.setText(currentLat+"");
                        LongitudeText.setText(currentLon+"");

                        cut = list.get(0).toString().split("\"");
                        String cut2;
                        cut2 = cut[1].replace("대한민국","");


                        zoneText.setText(cut2);
                        locationButton.setImageResource(R.drawable.my_position_clicked);

                    } else {
                        // GPS 를 사용할수 없으므로
                        gps.showSettingsAlert();
                    }

                }
                //}




        });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {
                map.clear();

                latitude = latLng.latitude;
                longitude = latLng.longitude;
                try {
                    list = geocoder.getFromLocation(latitude, longitude, 10);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                markerOptions.title("선택한 위치");
                markerOptions.snippet("위도 : " + latitude + " 경도 : " + longitude);
                markerOptions.position(latLng);
                LatitudeText.setText(latitude+"");
                LongitudeText.setText(longitude+"");

                cut = list.get(0).toString().split("\"");
                String cut2;
                cut2 = cut[1].replace("대한민국","");


                zoneText.setText(cut2.trim());


                map.addMarker(markerOptions);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        Toast.makeText(getApplicationContext(), "선택한 위치에서 등록하려면 등록버튼을 누르세요", Toast.LENGTH_SHORT).show();

                        return false;
                    }
                });
            }
        });


        //0913추가
        locationSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = editLocation.getText().toString();
                List<Address> addressList = null;
                try{
                    addressList = geocoder.getFromLocationName(str,10);
                    if(addressList.size() != 0 && addressList.toString().contains("대한민국")) {

                        String splitStr[] = addressList.get(0).toString().split(",");

                        String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2);
                        String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                        String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
                        LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                        // 마커 생성

                        MarkerOptions mOptions2 = new MarkerOptions();
                        mOptions2.snippet(address);
                        mOptions2.position(point);
                        // 마커 추가
                        map.clear();
                        map.addMarker(mOptions2);
                        // 해당 좌표로 화면 줌
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));

                        LatitudeText.setText(latitude+"");
                        LongitudeText.setText(longitude+"");

                        try {
                            list = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 10);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        cut = list.get(0).toString().split("\"");
                        String cut2;


                        cut2 = cut[1].replace("대한민국","");
                        zoneText.setText(cut2.trim());






                    }
                    else{
                        Toast.makeText(getApplicationContext(), "검색이 불가능한 지역입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (IOException e){
                    Log.d("register exception", e.toString());
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"검색어를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }



            }
        });




    }
    //0913추가
    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //0913추가
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE_GALLERY_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), CODE_GALLERY_REQUEST);
            }
        } else {
            //Toast.makeText(getApplicationContext(), "접근허용해주세요", Toast.LENGTH_SHORT).show();
        }



        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    //0913추가
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri filePath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }




    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }
}

