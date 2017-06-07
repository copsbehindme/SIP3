package com.example.copsbehindme.sip;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login_signup extends AppCompatActivity {
    private final String TAG= "login_signup";
    EditText username;
    EditText password;
    Button login,signup;
    TextView orTextView , tryText;
    UserDatabaseHelper mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        username = (EditText)findViewById(R.id.usernameEditText);
        password = (EditText)findViewById(R.id.passwordEditText);
        login =  (Button) findViewById(R.id.loginButton);
        signup = (Button) findViewById(R.id.signupButton);
        orTextView = (TextView)findViewById(R.id.orTextView);
        tryText = (TextView)findViewById(R.id.tryTextView);
        mydb = new UserDatabaseHelper(this);
    }
    public void signin(View view){
        Log.d(TAG, "signin: ");
        if(username.getText().toString().length() <1 || password.getText().toString().length() <1){
            Toast.makeText(login_signup.this,"Enter valid username and password",Toast.LENGTH_SHORT).show();
            return;
        }

        if(mydb.validate(username.getText().toString(),password.getText().toString())){
            Log.d(TAG, "signin: ");
            Intent intent = new Intent(this,home.class);
            intent.putExtra("uname",username.getText().toString()); // bundle has uname object that consist of username logged in
            startActivity(intent);
        }
        else
            Toast.makeText(login_signup.this,"Enter valid username and password",Toast.LENGTH_SHORT).show();
    }

    public void signup(View view){
        Log.d(TAG, "signup: ");
        if(username.getText().toString().length() <1 || password.getText().toString().length() <1){
            Toast.makeText(login_signup.this,"Enter valid username and password",Toast.LENGTH_SHORT).show();
            return;
        }

        if(mydb.validate(username.getText().toString(),password.getText().toString())){
            Toast.makeText(this,"Please try some other username, This one is already taken",Toast.LENGTH_SHORT).show();
            return;
        }
        if(mydb.signup(username.getText().toString(),password.getText().toString())){
            Intent intent = new Intent(this,home.class);
            intent.putExtra("uname",username.getText().toString());
            startActivity(intent);
        }
        else
            Toast.makeText(login_signup.this,"Enter valid username and password",Toast.LENGTH_SHORT).show();
    }

    public void gotoHome(View view){
        Log.d(TAG, "gotoHome: ");
        Intent intent = new Intent(this,home.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
        finish();
    }
}
