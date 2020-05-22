package com.example.register;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kch.proj_manager_v3.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

public class CommunityActivity extends AppCompatActivity {

    ListView listView;
    CommunityAdapter adapter;
    ArrayList<CommunityItem> communityList;
    ArrayList<CommunityItem> communitysaveList;
    EditText searchtext5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        ArrayList<String> choices = new ArrayList<String>(
                Arrays.asList("제목", "내용", "제목+내용", "아이디"));
        final Spinner searchchoice = (Spinner) findViewById(R.id.searchspinner3);
        ArrayAdapter<String> choiceAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, choices);
        searchchoice.setAdapter(choiceAdapter);

        listView = (ListView) findViewById(R.id.cmlistView);
        searchtext5 = (EditText) findViewById(R.id.serachtext5);
        communityList = new ArrayList<CommunityItem>();
        communitysaveList = new ArrayList<CommunityItem>();
        adapter = new CommunityAdapter();

        new BackgroundTask().execute();

        ImageButton writeButton = (ImageButton) findViewById(R.id.writeButton);
        ImageButton backbutton5 = (ImageButton) findViewById(R.id.backbutton5);

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent writeIntent = new Intent(getApplicationContext(), CommunityWriteActivity.class);
                startActivity(writeIntent);
                finish();
            }
        });

        backbutton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CommunityItem item = (CommunityItem) adapter.getItem(i);
                Intent contentintent = new Intent(getApplicationContext(), CommunityContentActivity.class);
                contentintent.putExtra("item", item);
                startActivity(contentintent);
                finish();

            }
        });

        searchchoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                searchtext5.setText("");
                if (searchchoice.getSelectedItem().toString() == "제목") {
                    searchtext5.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (charSequence.toString().length() > 0) {
                                searchTitle(charSequence.toString());
                            } else {
                                new BackgroundTask().execute();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                } else if (searchchoice.getSelectedItem().toString() == "내용") {
                    searchtext5.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (charSequence.toString().length() > 0) {
                                searchContent(charSequence.toString());
                            } else {
                                new BackgroundTask().execute();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                } else if (searchchoice.getSelectedItem().toString() == "아이디") {
                    searchtext5.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (charSequence.toString().length() > 0) {
                                searchId(charSequence.toString());
                            } else {
                                new BackgroundTask().execute();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                } else if (searchchoice.getSelectedItem().toString() == "제목+내용") {
                    searchtext5.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (charSequence.toString().length() > 0) {
                                searchtc(charSequence.toString());
                            } else {
                                new BackgroundTask().execute();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    class CommunityAdapter extends BaseAdapter {
        ArrayList<CommunityItem> items = new ArrayList<CommunityItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(CommunityItem item) {
            items.add(item);
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public void claerAllItems() {
            items.clear();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            CommunityItemView cview = new CommunityItemView(getApplicationContext());
            CommunityItem item = items.get(i);
            cview.setTitle(item.getTitle());
            if (item.getContent().length() > 50) {
                String littlecontent = item.getContent().substring(0, 49);
                cview.setContent(littlecontent + "...");
            } else {
                cview.setContent(item.getContent());
            }
            cview.setWriteTime(item.getWritetime());
            cview.setUserid(item.getUserid());
            return cview;
        }
    }


    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://skj.dothome.co.kr/Cm_Requset.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + '\n');
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        public void onPostExecute(String result) {
            communityList.clear();
            communitysaveList.clear();
            adapter.claerAllItems();
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String Cm_Title, Cm_Content, Cm_Time, Cm_User, Cm_Password;
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    Cm_Title = object.getString("Cm_Title");
                    Cm_Content = object.getString("Cm_Content");
                    Cm_Time = object.getString("Cm_Time");
                    Cm_User = object.getString("Cm_User");
                    Cm_Password = object.getString("Cm_Password");
                    communityList.add(new CommunityItem(Cm_Title, Cm_Content, Cm_Time, Cm_User, Cm_Password));
                    communitysaveList.add(new CommunityItem(Cm_Title, Cm_Content, Cm_Time, Cm_User, Cm_Password));
                    adapter.addItem(communityList.get(count));
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            listView.setAdapter(adapter);
        }
    }

    public void onBackPressed() {
        finish();
    }


    public void searchTitle(String search) {
        communityList.clear();
        adapter.claerAllItems();
        int count = 0;
        boolean addon = true;
        for (int i = 0; i < communitysaveList.size(); i++) {
            if (communitysaveList.get(i).getTitle().contains(search)) {
                for (int j = 0; j < communityList.size(); j++) {
                    if (communityList.get(j).getWritetime().toString().equals(communitysaveList.get(i).getWritetime().toString())) {
                        addon = true;
                    } else {
                        addon = false;
                    }
                }
                if (addon) {
                    communityList.add(communitysaveList.get(i));
                    adapter.addItem(communityList.get(count));
                    count++;
                } else {

                }
            }
        }
        adapter.notifyDataSetChanged();
    }


    public void searchContent(String search) {
        communityList.clear();
        adapter.claerAllItems();
        int count = 0;
        boolean addon = true;
        for (int i = 0; i < communitysaveList.size(); i++) {
            if (communitysaveList.get(i).getContent().contains(search)) {
                for (int j = 0; j < communityList.size(); j++) {
                    if (communityList.get(j).getWritetime().toString().equals(communitysaveList.get(i).getWritetime().toString())) {
                        addon = true;
                    } else {
                        addon = false;
                    }
                }
                if (addon) {
                    communityList.add(communitysaveList.get(i));
                    adapter.addItem(communityList.get(count));
                    count++;
                } else {

                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void searchId(String search) {
        communityList.clear();
        adapter.claerAllItems();
        int count = 0;
        boolean addon = true;
        for (int i = 0; i < communitysaveList.size(); i++) {
            if (communitysaveList.get(i).getUserid().contains(search)) {
                for (int j = 0; j < communityList.size(); j++) {
                    if (communityList.get(j).getWritetime().toString().equals(communitysaveList.get(i).getWritetime().toString())) {
                        addon = true;
                    } else {
                        addon = false;
                    }
                }
                if (addon) {
                    communityList.add(communitysaveList.get(i));
                    adapter.addItem(communityList.get(count));
                    count++;
                } else {

                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void searchtc(String search) {
        communityList.clear();
        adapter.claerAllItems();
        int count = 0;
        boolean addon = true;
        for (int i = 0; i < communitysaveList.size(); i++) {
            if (communitysaveList.get(i).getTitle().contains(search) || communitysaveList.get(i).getContent().contains(search)) {
                for (int j = 0; j < communityList.size(); j++) {
                    if (communityList.get(j).getWritetime().toString().equals(communitysaveList.get(i).getWritetime().toString())) {
                        addon = true;
                    } else {
                        addon = false;
                    }
                }
                if (addon) {
                    communityList.add(communitysaveList.get(i));
                    adapter.addItem(communityList.get(count));
                    count++;
                } else {

                }
            }
        }
        adapter.notifyDataSetChanged();

    }
}

