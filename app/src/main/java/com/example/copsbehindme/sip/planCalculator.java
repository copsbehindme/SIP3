package com.example.copsbehindme.sip;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class planCalculator extends AppCompatActivity {
    EditText goalMoneyEditText;
    EditText goalRateEditText;
    EditText goalTimeEditText;
    TextView goalSipTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_calculator);
        goalMoneyEditText = (EditText)findViewById(R.id.goalMoney);
        goalRateEditText = (EditText)findViewById(R.id.goalRate);
        goalTimeEditText = (EditText)findViewById(R.id.goalTime);
        goalSipTextView = (TextView)findViewById(R.id.goalSip);
    }

    public void plancalculator(View view){
        double goalMoney = Double.parseDouble(String.valueOf(goalMoneyEditText.getText().toString()));
        float goalRate = Integer.parseInt(String.valueOf(goalRateEditText.getText().toString())) * 0.01f;
        int goalTime = Integer.parseInt(String.valueOf(goalTimeEditText.getText().toString()));

        double goalSip = calculatePrinciple(goalMoney,goalRate,goalTime);
        goalSipTextView.setText("Invest " + String.valueOf(String.format("%.2f",goalSip)));

    }
    public static double calculatePrinciple(double amount, float rate , int time){
        double denominator = 0;
        for (int i = 1; i<= time*12 ;i++ ) {
            denominator = denominator + Math.pow((1 + (rate/12)) , i);
        }
        return amount / denominator;
    }
}
