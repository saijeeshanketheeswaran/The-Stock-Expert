package com.example.a100589716.thestockexpert;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class CSVGetter extends AppCompatActivity {
    String name;
    String start_date;
    String end_date;
 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public String getData(String name) throws ExecutionException, InterruptedException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -3);
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        start_date = format.format(date);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DATE, -1);
        Date date2 = calendar2.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        end_date = df.format(date2);

        String url = "https://www.quandl.com/api/v3/datasets/EURONEXT/" + name + ".csv?start_date=" + start_date +
                "&end_date=" + end_date + "&api_key=ArWycGC8XQ6yrWfxPnt4";

        Log.d("URL", url);
        return url;


        //DownloadCSVLinkTask csv = new  DownloadCSVLinkTask();
        //csv.execute(url);

        //ArrayList<String> data = new DownloadCSVLinkTask().execute(url).get();

        //Log.d("Data", String.valueOf(data));

    }









}


class DownloadCSVLinkTask extends AsyncTask<String,Void,  ArrayList<String>> {
    private Exception exception = null;
    //String jsonData = "";
    ArrayList<String> combined = new ArrayList<String>();
    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> open = new ArrayList<>();
    ArrayList<String> high = new ArrayList<>();
    ArrayList<String> low = new ArrayList<>();
    ArrayList<String> last = new ArrayList<>();
    ArrayList<String> volume = new ArrayList<>();
    ArrayList<String> turnover = new ArrayList<>();


    @Override
    protected ArrayList<String> doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream in = conn.getInputStream();
            BufferedReader buffRead = new BufferedReader(new InputStreamReader(in));
            //StringBuilder sBuilder = new StringBuilder();
            //buffRead.readLine();
            String line = buffRead.readLine();
            while (line != null) {
                String[] CSV = line.split(",");
                date.add(CSV[0]);
                open.add(CSV[1]);
                high.add(CSV[2]);
                low.add(CSV[3]);
                last.add(CSV[4]);
                volume.add(CSV[5]);
                turnover.add(CSV[6]);
            }
            in.close();

            combined.addAll(date);
            combined.addAll(open);
            combined.addAll(high);
            combined.addAll(low);
            combined.addAll(last);
            combined.addAll(volume);
            combined.addAll(turnover);

            Log.d("Combined", String.valueOf(date));




/*
            jsonData = sBuilder.toString();


            JSONObject jsonObject= new JSONObject(jsonData);

            JSONArray numbers = jsonObject.getJSONArray("data");
            int length = numbers.length();

            for(int j=0; j< length; j++) {
                JSONObject json = numbers.getJSONObject(j);
                String w = json.toString();
                data_set.add(w);

            }
*/


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        //} catch (JSONException e) {
         //   e.printStackTrace();
        }

        return combined;
    }

    @Override
    protected void onPostExecute(ArrayList<String> data) {
        if (exception != null) {
            return;
        }
    }


}
