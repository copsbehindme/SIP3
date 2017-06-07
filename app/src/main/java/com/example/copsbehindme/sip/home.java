package com.example.copsbehindme.sip;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class home extends AppCompatActivity {
    private static final String TAG = "home";
    Button mySIPButton;
    Bundle extras;
    String uname;
    Boolean isUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "1ach onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mySIPButton = (Button)findViewById(R.id.mySIPButton);
        isUser = getIntent().hasExtra("uname");
        Log.d(TAG, "onCreate: isUser = " + isUser);
        uname = new String("");
        try{
            extras = getIntent().getExtras();
            uname =  extras.getString("uname");
        }
        catch (Exception e){

        }
        if(uname.equals(""))
            mySIPButton.setAlpha(0);
    }

    public void sipcalc(View view){
        Intent intent = new Intent(this, futureReturnCalculator.class);
        if(isUser)
            intent.putExtra("uname",uname);

        startActivity(intent);

    }

    public void plancalc(View view){
        startActivity(new Intent(this, planCalculator.class));
    }

    public void showList(View view){

        Intent intent = new Intent(this, listViewActivity.class);
        intent.putExtra("uname",uname);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(home.this,login_signup.class));
    }
}
