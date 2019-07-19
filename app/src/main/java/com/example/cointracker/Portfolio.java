package com.example.cointracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Portfolio extends AppCompatActivity implements ListOfCrypto.Listener {

    private ListOfCrypto cryptoList = null;
    private RecyclerView mRecyclerView;
    private AllCryptoAdapter mAdapter;
    List<Transaction.PortfolioData> portfolioDataEntries = new ArrayList<>();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent myIntent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    myIntent = new Intent(Portfolio.this, MainActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);
                    return true;
                case R.id.navigation_portfolio:
                    return true;
                case R.id.navigation_all_crypto:
                    myIntent = new Intent(Portfolio.this, AllCryptos.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_portfolio);

        cryptoList = ListOfCrypto.getInstance();
        cryptoList.registerListener(this);

        // read portfolio file from internal storage
        BufferedReader input = null;
        try {
            input = new BufferedReader(
                    new InputStreamReader(openFileInput("myPortfolio.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = null;
        StringBuffer buffer = new StringBuffer();
        while (true) {
            try {
                if (!((line = input.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            buffer.append(line + "\n");
        }
        String text = buffer.toString();
        System.out.println("*** portfolio file read: "+text);

        Type portfolioType = new TypeToken<ArrayList<Transaction.PortfolioData>>(){}.getType();

        portfolioDataEntries = new Gson().fromJson(text, portfolioType);
        System.out.println("*** json parsed");



    }


    class CryptoTask extends AsyncTask<Void,Void,String> {
        //https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=bitcoin%2Clitecoin%2Cripple&order=market_cap_desc&page=1&sparkline=false
        String _id;
        String _currency;
        int _days;
        CryptoTask(String id, String currency, int days) {
            _id = id;
            _currency = currency;
            _days = days;
        }

        String chartData = null;

        @Override
        protected String doInBackground(Void... voids) {
            //using square OkHttp library for api callouts
            OkHttpClient client = new OkHttpClient();

            //example call: https://api.coingecko.com/api/v3/coins/bitcoin/market_chart?vs_currency=usd&days=180
            String url = "https://api.coingecko.com/api/v3/coins/" + _id +
                    "/market_chart?vs_currency=" + _currency + "&days=" + _days;
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                chartData = response.body().string();
                System.out.println("*** " + chartData);
                return chartData;
            } catch (IOException e) {
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String chartData) {
            //chart historical crypto data
            CryptoDetail.MarketChartDataPoints marketData = new Gson().fromJson(chartData, CryptoDetail.MarketChartDataPoints.class);
            LineChart chart = findViewById(R.id.chart);
            XAxis xAxis = chart.getXAxis();
            xAxis.setDrawLabels(false);
            //map data from MarketChartDataPoints to objects for chart
            List<Entry> entries = new ArrayList<Entry>();
            for (double[] data : marketData.prices) {
                // turn your data into Entry objects
                entries.add(new Entry((float)data[0], (float)data[1] ));
            }
            LineDataSet dataSet = new LineDataSet(entries, "Price - USD");
            LineData lineData = new LineData(dataSet);
            chart.setData(lineData);
            chart.invalidate(); // refresh
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_portfolio);
    }

    @Override
    public void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter = new AllCryptoAdapter(Portfolio.this, cryptoList.getListCDP());
                // Connect the adapter with the RecyclerView.
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }
}


