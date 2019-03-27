package com.example.a100589716.thestockexpert;

import android.Manifest;
import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ExpandableListView expandableListView = findViewById((R.id.expandableListView));

        HashMap<String, List<String>> item = new HashMap<>();
        ArrayList<String> stocks = new ArrayList<>();

        BufferedReader csvFile = null;

        try {

            csvFile = new BufferedReader(new InputStreamReader(getAssets().open("EURONEXT_metadata.csv")));

            String fileContent = csvFile.readLine();

            while (fileContent != null) {
                String[] tab = fileContent.split(",");

                fileContent = csvFile.readLine();

                if (tab[1].equalsIgnoreCase("name")) {
                    Log.d("Names", "Not added to the list");
                }
                else {
                    Log.d("Names", tab[1]);
                    item.put(tab[1], stocks);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                csvFile.close();
            } catch (IOException ex) {
                Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        MyExpandableListAdapter adapter = new MyExpandableListAdapter(item);
        expandableListView.setAdapter(adapter);
    }
}
