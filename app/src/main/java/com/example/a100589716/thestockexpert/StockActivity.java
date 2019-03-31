package com.example.a100589716.thestockexpert;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StockActivity extends AppCompatActivity {

    String symbol;
    String url;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");

        CSVGetter csv = new CSVGetter();

        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(name);
        while(m.find()) {
            symbol = m.group(1);
        }

        try {
            url = csv.getData(symbol);

            TextView txtView = (TextView) findViewById(R.id.Url);
            txtView.setText(url);

            Log.d("Url Link", url);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
