package com.example.copsbehindme.sip;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class listViewActivity extends AppCompatActivity {
    public static final String TAG = "listViewActivity";
    ListView listView;
    TextView noSIPtextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        listView = (ListView)findViewById(R.id.myListView);
        noSIPtextView = (TextView)findViewById(R.id.noSIP_textView);
        final String username = getIntent().getExtras().getString("uname");
        Log.d(TAG, "onCreate: user logged in : "+ username);
        UserDatabaseHelper db = new UserDatabaseHelper(this);
        ArrayList<String> sipToShow = db.retreiveAllUserSIP(username);
        if(sipToShow.size() > 0){
            noSIPtextView.setAlpha(0f);
            ArrayAdapter myAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,sipToShow);
            listView.setAdapter(myAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        default:
                            Log.d(TAG, "onItemClick: Item at Position is = " + listView.getItemAtPosition(position).toString());
                            Intent intent = new Intent(listViewActivity.this,futureReturnCalculator.class);
                            intent.putExtra("uname",username);
                            intent.putExtra("sipName",listView.getItemAtPosition(position).toString());
                            startActivity(intent);
                    }

                }
            });
        }
    }
}
