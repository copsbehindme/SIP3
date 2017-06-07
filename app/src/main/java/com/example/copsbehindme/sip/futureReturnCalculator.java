package com.example.copsbehindme.sip;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class futureReturnCalculator extends AppCompatActivity {
    PieChart pieChart;
    LineChart lineChart;
    EditText principle,time,rate,sipName;
    TextView investedValue,totalReturns;
    ArrayList<Float> moneyTrackEveryMonth = new ArrayList<Float>();
    ArrayList<Integer> monthCount = new ArrayList<Integer>();
    Button saveSIPButton,sipDeleteButton;
    Bundle bundle;
    String uname;
    double sum;
    Button doAllTheStuff;
    private static final String TAG = "futureReturnCalculator";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future_return_calcultaor);
        initializeEverything();
        sipDeleteButton.setAlpha(0f);
        checkIfValueCameFromListView();
        ifValueIsNotFromListView();


    }

    private void ifValueIsNotFromListView() {

        boolean b = getIntent().hasExtra("uname");

        saveSIPButton.setAlpha(0f);
        try{
            if (b == false){
                Log.d(TAG, "onCreate: uname is null");
                saveSIPButton.setAlpha(0f);
                sipName.setAlpha(0f);
                sipDeleteButton.setAlpha(0f);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void initializeEverything() {
        Log.d(TAG, "initializeEverything: ");
        principle = (EditText)findViewById(R.id.principle);
        time = (EditText)findViewById(R.id.time);
        rate = (EditText)findViewById(R.id.rate);
        pieChart = (PieChart)findViewById(R.id.pieChart);
        lineChart = (LineChart)findViewById(R.id.lineChart);
        sipName = (EditText)findViewById(R.id.sipName);
        investedValue = (TextView)findViewById(R.id.investedValue);
        totalReturns = (TextView)findViewById(R.id.totalReturns);
        saveSIPButton = (Button)findViewById(R.id.save_sip_button);
        sipDeleteButton = (Button)findViewById(R.id.sip_delete_button);
        doAllTheStuff = (Button)findViewById(R.id.calculate_return_button);
    }

    private void checkIfValueCameFromListView() {
        Log.d(TAG, "checkIfValueCameFromListView: ");
        bundle = getIntent().getExtras();
        if(getIntent().hasExtra("uname") && getIntent().hasExtra("sipName")){
            uname = bundle.getString("uname");
            String nameOfSIP = bundle.getString("sipName");
            UserDatabaseHelper db = new UserDatabaseHelper(this);
            Cursor cursor = db.retreiveSIP(uname,nameOfSIP);
            cursor.moveToFirst();
            principle.setText(cursor.getString(0));
            rate.setText(cursor.getString(1));
            time.setText(String.valueOf(Integer.valueOf(cursor.getString(2)) / 12));
            sipName.setText(nameOfSIP);
            doAllTheStuff.performClick();
            doAllTheStuff.setAlpha(0f);
            sipDeleteButton.setAlpha(1f);
        }
    }

    public void saveSIP(View view){
        Log.d(TAG, "saveSIP: Trying to save SIP");
        uname = getIntent().getExtras().getString("uname");
        String nameOfSIP = sipName.getText().toString();
        if(nameOfSIP.length() >=1){
            double p = Double.parseDouble(principle.getText().toString());
            int t = Integer.parseInt(time.getText().toString());
            int r = Integer.parseInt(rate.getText().toString());
            UserDatabaseHelper db = new UserDatabaseHelper(this);
            if(db.whetherSIPNameExists(nameOfSIP,uname)){
                Log.d(TAG, "saveSIP: We can use this name");
                Toast.makeText(this,"You already used this name. Try Another Name",Toast.LENGTH_SHORT).show();
                return;
            }

            if(db.addSIP(nameOfSIP,uname,p,sum,r,t*12))
                Toast.makeText(this,"SIP has been added",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this,"SIP has not been added",Toast.LENGTH_SHORT).show();
            return;
        }
        else
            Toast.makeText(this,"Don't leave SIP Name Blank", Toast.LENGTH_SHORT).show();

    }

    public void deleteSIP(View view){
        UserDatabaseHelper db = new UserDatabaseHelper(this);
        uname = getIntent().getExtras().getString("uname");
        String nameOfSIP = sipName.getText().toString();
        if(db.deleteSIP(uname,nameOfSIP)){
            Toast.makeText(this,"SIP is deleted",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(futureReturnCalculator.this,listViewActivity.class);
            intent.putExtra("uname",uname);
            startActivity(intent);
        }
        else
            Toast.makeText(this,"There was an error. Try again",Toast.LENGTH_SHORT).show();

    }

    public void calculateTotal(View view){
        if(principle.getText().length() >= 1 && rate.getText().length() >= 1 && time.getText().length() >= 1){
            Log.d(TAG, "calculateTotal: ");
            double p = Double.parseDouble(principle.getText().toString());
            int t = Integer.parseInt(time.getText().toString());
            int r = Integer.parseInt(rate.getText().toString());

            sum = 0;
            int month = 0;
            double totalInvestedMoney = p * 12 * t;
            for(int i = t*12; i>=1; i--){
                sum = sum + compoundedValue(p,r,i);
                moneyTrackEveryMonth.add((float)sum);
                monthCount.add(++month);
            }


            investedValue.setText("Invested : " + String.valueOf(totalInvestedMoney));


            totalReturns.setText("Returns : " + String.valueOf(String.format("%.2f",sum)));
            makePieChart((float) totalInvestedMoney, (float) sum);
            makeLineChart();
            lineChart.invalidate();
            pieChart.invalidate();
            saveSIPButton.setAlpha(1f);
        }
        else
            Toast.makeText(this,"Enter Valid values and don't leave them blank",Toast.LENGTH_SHORT).show();
    }
    public static double compoundedValue(double p,int r,int t){
        Log.d(TAG, "compoundedValue: ");
        return  p * Math.pow((1 + (0.01 * r / 12)),t);
    }

    public void makePieChart(float investedValue , float totalReturns){
        Log.d(TAG, "makePieChart: ");

        Description description = new Description(); //Description of pieChart
        description.setText("Invested vs Returns");
        pieChart.setDescription(description);
        pieChart.setCenterText(":)");
        pieChart.setCenterTextSize(10f);
        addDataToPieChart(investedValue,totalReturns);

    }

    public void addDataToPieChart(float investedValue , float totalReturns){
        Log.d(TAG, "addDataToPieChart: ");
        ArrayList<PieEntry> yEntries = new ArrayList<>();
        ArrayList<String> xEntries =  new ArrayList<>();
        yEntries.add(new PieEntry(investedValue));
        yEntries.add(new PieEntry(totalReturns - investedValue));

        xEntries.add("Invested Value");
        xEntries.add("Total Returns");

        //creating dataset
        PieDataSet pieDataSet = new PieDataSet(yEntries,"Invested vs return Value");
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextSize(15f);

        // adding colors to dataSet
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        pieDataSet.setColors(colors);

        // add legend to the chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);

        //creating pie dataObject
        PieData pieData = new PieData(pieDataSet);

        //setting data to piechart
        pieChart.setData(pieData);
    }

    public void makeLineChart(){
        Log.d(TAG, "makeLineChart: ");

        Description description = new Description();
        description.setText("Increasing money chart");
        lineChart.setDescription(description);
        addDataToLineChart(monthCount,moneyTrackEveryMonth );

    }

    public void addDataToLineChart(ArrayList<Integer> monthCount, ArrayList<Float> moneyTrackEveryMonth) {
        Log.d(TAG, "addDataToLineChart: ");

        // adding entries
        ArrayList<Entry> yEntries = new ArrayList<Entry>();
        ArrayList<Integer> xEntries = new ArrayList<Integer>();
        xEntries = monthCount;
        for (int i = 0; i< moneyTrackEveryMonth.size();i++)
            yEntries.add(new Entry(i,moneyTrackEveryMonth.get(i)));
        // adding entries to dataset
        LineDataSet lineDataSet = new LineDataSet(yEntries , "Increasing money");

        // add colors to dataset
        lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        // creating LineData object
        LineData lineData = new LineData(lineDataSet);

        // setting LineData to LineChart
        lineChart.setData(lineData);
        lineChart.animateXY(100,100, Easing.EasingOption.Linear, Easing.EasingOption.Linear);


    }
}
