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
import android.widget.TextView;

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
    private PortfolioAdapter mAdapter;
    List<Transaction.PortfolioData> portfolioDataEntries = new ArrayList<>();
    List<CryptoDataPoints> cryptoDataPoints = new ArrayList<>();
    double totalPortfolioValue;


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
        System.out.println("*** portfolio file read: " + text);

        Type portfolioType = new TypeToken<ArrayList<Transaction.PortfolioData>>(){}.getType();

        portfolioDataEntries = new Gson().fromJson(text, portfolioType);
        System.out.println("*** json parsed");
        System.out.println("***  " + portfolioDataEntries.get(0).id);

        String listOfIds = "";
        for (Transaction.PortfolioData entry : portfolioDataEntries){
            listOfIds += entry.id + ",";
        }
        System.out.println("*** " + listOfIds);
        new CryptoTask(listOfIds).execute();

    }


    class CryptoTask extends AsyncTask<Void,Void,String> {

        String listOfIds;
        CryptoTask(String listOfIds) {
            this.listOfIds = listOfIds;
        }

        String portfolioApiData;

        @Override
        protected String doInBackground(Void... voids) {
            //using square OkHttp library for api callouts
            OkHttpClient client = new OkHttpClient();
            //https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=bitcoin%2Clitecoin%2Cripple
            String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=" + listOfIds;
            System.out.println("*** calling: " + url);
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                portfolioApiData = response.body().string();
                System.out.println("*** response: " + portfolioApiData);
                return portfolioApiData;
            } catch (IOException e) {
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String portfolioApiData) {

            Type cdpType = new TypeToken<ArrayList<CryptoDataPoints>>(){}.getType();
            cryptoDataPoints = new Gson().fromJson(portfolioApiData, cdpType);

            //merge portfolio data into cryptoDataPoints to display in recycler view
            for (Transaction.PortfolioData entry : portfolioDataEntries){
                for (CryptoDataPoints crypto: cryptoDataPoints){
                    if (crypto.id.equals(entry.id)){
                        crypto.totalQuantityOwned = entry.totalQuantityOwned;
                        crypto.weightedAveragePriceUSD = entry.weightedAveragePriceUSD;
                        crypto.currentValue = crypto.totalQuantityOwned * crypto.current_price;
                        totalPortfolioValue += crypto.currentValue;
                    }
                }
            }
            TextView tv_totalPortfolioValue = findViewById(R.id.total_portfolio_value);
            tv_totalPortfolioValue.setText("$" + String.format("%.2f",totalPortfolioValue));

            // Get a handle to the RecyclerView.
            mRecyclerView = findViewById(R.id.recyclerview10);//////////////////////probably should change this id
            // Create an adapter and supply the data to be displayed.
            mAdapter = new PortfolioAdapter(Portfolio.this, cryptoDataPoints);
            // Connect the adapter with the RecyclerView.
            mRecyclerView.setAdapter(mAdapter);
            // Give the RecyclerView a default layout manager.
            mRecyclerView.setLayoutManager(new LinearLayoutManager(Portfolio.this));
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
                mAdapter = new PortfolioAdapter(Portfolio.this, cryptoList.getListCDP());/////////////this was getting an error
                // Connect the adapter with the RecyclerView.
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }

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
}


