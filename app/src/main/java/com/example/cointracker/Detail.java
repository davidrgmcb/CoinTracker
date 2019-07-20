package com.example.cointracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Detail extends AppCompatActivity implements ListOfCrypto.Listener {
    private ListOfCrypto cryptoList = null;
    //CryptoDataPoints cryptoDetailArray[];
    int arrayPosition;
    String id;
    List<Transaction.PortfolioData> portfolioDataEntries = new ArrayList<>();
    CryptoDataPoints cryptoDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("*** Detail onCreate running...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_detail);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_home);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        System.out.println("*** getting data from ListOfCrypto...");
        cryptoList = ListOfCrypto.getInstance();
        //cryptoDetailArray = cryptoList.getListCDP();
        //get the array position of the coin by subtracting 1 from the crypto rank passed in
        arrayPosition = (int)getIntent().getDoubleExtra("rank", -1) - 1;

        cryptoDetail = cryptoList.getListCDP().get(arrayPosition);

        TextView name = findViewById(R.id.cryptoName);
        name.setText(cryptoDetail.name);//[arrayPosition].name);

        TextView currentPrice = findViewById(R.id.currentPrice);
        currentPrice.setText("$" + cryptoDetail.current_price);//[arrayPosition].current_price);

        TextView priceChanged = findViewById(R.id.priceChange);
        priceChanged.setText(cryptoDetail.price_change_percentage_24h + "%");//[arrayPosition].price_change_percentage_24h + "%");

        id = cryptoDetail.id;//[arrayPosition].id;

        //load crypto image
        new DownloadImageTask((ImageView) findViewById(R.id.cryptoLogo)).execute(cryptoDetail.image);//[arrayPosition].image);

        String currency = "usd";
        int days = 180;

        new CryptoTask(id, currency, days).execute();

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

        //merge portfolio data into cryptoDetail to display on activity
        for (Transaction.PortfolioData entry : portfolioDataEntries){
            if (entry.id.equals(cryptoDetail.id)){
                cryptoDetail.totalQuantityOwned = entry.totalQuantityOwned;
                cryptoDetail.weightedAveragePriceUSD = entry.weightedAveragePriceUSD;
                cryptoDetail.currentValue = cryptoDetail.totalQuantityOwned * cryptoDetail.current_price;
            }
        }


        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();

                Intent i = new Intent(view.getContext(), Transaction.class);
                i.putExtra("arrayPosition", arrayPosition);
                view.getContext().startActivity(i);
            }
        });



    }


    @Override
    public void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }


    public class MarketChartDataPoints{

        public List<double[]> prices = new ArrayList<>();
        //double[] prices;
    }


    class CryptoTask extends AsyncTask<Void,Void,String> {
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
            MarketChartDataPoints marketData = new Gson().fromJson(chartData, MarketChartDataPoints.class);
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



    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent myIntent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    myIntent = new Intent(Detail.this, MainActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);
                    return true;
                case R.id.navigation_portfolio:
                    myIntent = new Intent(Detail.this, Portfolio.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);
                    return true;
                case R.id.navigation_all_crypto:
                    myIntent = new Intent(Detail.this, AllCryptos.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);
                    return true;
            }
            return false;
        }
    };


}
