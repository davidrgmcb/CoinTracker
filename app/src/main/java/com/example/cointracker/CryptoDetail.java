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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CryptoDetail extends AppCompatActivity implements ListOfCrypto.Listener {
    private ListOfCrypto cryptoList = null;
    //CryptoDataPoints cryptoDetailArray[];
    int arrayPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_detail);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_home);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        cryptoList = ListOfCrypto.getInstance();
        //cryptoDetailArray = cryptoList.getListCDP();
        //get the array position of the coin by subtracting 1 from the crypto rank passed in
        arrayPosition = (int)getIntent().getDoubleExtra("id", -1) - 1;

        TextView name = findViewById(R.id.cryptoName);
        name.setText("$"+cryptoList.getListCDP()[arrayPosition].name);

        TextView currentPrice = findViewById(R.id.currentPrice);
        currentPrice.setText("$"+cryptoList.getListCDP()[arrayPosition].current_price);

        TextView priceChanged = findViewById(R.id.priceChange);
        priceChanged.setText(cryptoList.getListCDP()[arrayPosition].price_change_percentage_24h + "%");


        //load crypto image
        new DownloadImageTask((ImageView) findViewById(R.id.cryptoLogo))
                .execute(cryptoList.getListCDP()[arrayPosition].image);


        //chart historical crypto data
        //right now using sampleData, need to get actual data from api
        // api endpoint: https://api.coingecko.com/api/v3/coins/bitcoin/market_chart?vs_currency=usd&days=180
        MarketChartDataPoints marketData = new Gson().fromJson(sampleData, MarketChartDataPoints.class);
        LineChart chart = findViewById(R.id.chart);
        //map data from MarketChartDataPoints to objects for chart
        List<Entry> entries = new ArrayList<Entry>();
        for (double[] data : marketData.prices) {
            // turn your data into Entry objects
            entries.add(new Entry((float)data[0], (float)data[1] ));
        }
        LineDataSet dataSet = new LineDataSet(entries, "Label");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent i = new Intent(view.getContext(), Transaction.class);
                i.putExtra("id", arrayPosition);
                view.getContext().startActivity(i);
/*
                // additional action
                int wordListSize = mWordList.size();
                // Add a new word to the wordList.
                mWordList.addLast("+ Word " + wordListSize);
                // Notify the adapter, that the data has changed.
                mRecyclerView.getAdapter().notifyItemInserted(wordListSize);
                // Scroll to the bottom.
                mRecyclerView.smoothScrollToPosition(wordListSize);


  */          }
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

    String sampleData = "   {\n" +
            "    \"prices\": [\n" +
            "            [\n" +
            "            1547510400000,\n" +
            "            3648.04828565649\n" +
            "            ],\n" +
            "            [\n" +
            "            1547596800000,\n" +
            "            3567.801243199206\n" +
            "            ],\n" +
            "            [\n" +
            "            1547683200000,\n" +
            "            3597.6473233295915\n" +
            "            ],\n" +
            "            [\n" +
            "            1547769600000,\n" +
            "            3620.9812811457246\n" +
            "            ],\n" +
            "            [\n" +
            "            1547856000000,\n" +
            "            3597.7932517701506\n" +
            "            ],\n" +
            "            [\n" +
            "            1547942400000,\n" +
            "            3674.587270857304\n" +
            "            ],\n" +
            "            [\n" +
            "            1548028800000,\n" +
            "            3539.167531041029\n" +
            "            ],\n" +
            "            [\n" +
            "            1548115200000,\n" +
            "            3531.004182799933\n" +
            "            ],\n" +
            "            [\n" +
            "            1548201600000,\n" +
            "            3571.2669080928695\n" +
            "            ],\n" +
            "            [\n" +
            "            1548288000000,\n" +
            "            3555.7000025226243\n" +
            "            ],\n" +
            "            [\n" +
            "            1548374400000,\n" +
            "            3565.6420046170365\n" +
            "            ],\n" +
            "            [\n" +
            "            1548460800000,\n" +
            "            3564.822417111422\n" +
            "            ],\n" +
            "            [\n" +
            "            1548547200000,\n" +
            "            3563.614563419144\n" +
            "            ],\n" +
            "            [\n" +
            "            1548633600000,\n" +
            "            3553.121681129778\n" +
            "            ],\n" +
            "            [\n" +
            "            1548720000000,\n" +
            "            3432.215668301249\n" +
            "            ],\n" +
            "            [\n" +
            "            1548806400000,\n" +
            "            3413.3754832497975\n" +
            "            ],\n" +
            "            [\n" +
            "            1548892800000,\n" +
            "            3457.8826491759864\n" +
            "            ],\n" +
            "            [\n" +
            "            1548979200000,\n" +
            "            3431.943637864456\n" +
            "            ],\n" +
            "            [\n" +
            "            1549065600000,\n" +
            "            3464.490147562154\n" +
            "            ],\n" +
            "            [\n" +
            "            1549152000000,\n" +
            "            3486.581616258029\n" +
            "            ],\n" +
            "            [\n" +
            "            1549238400000,\n" +
            "            3454.025843825187\n" +
            "            ],\n" +
            "            [\n" +
            "            1549324800000,\n" +
            "            3464.003508861087\n" +
            "            ],\n" +
            "            [\n" +
            "            1549411200000,\n" +
            "            3469.8946770792636\n" +
            "            ],\n" +
            "            [\n" +
            "            1549497600000,\n" +
            "            3405.836861256719\n" +
            "            ],\n" +
            "            [\n" +
            "            1549584000000,\n" +
            "            3394.0146529587223\n" +
            "            ],\n" +
            "            [\n" +
            "            1549670400000,\n" +
            "            3664.2821026589836\n" +
            "            ],\n" +
            "            [\n" +
            "            1549756800000,\n" +
            "            3667.4082642899566\n" +
            "            ],\n" +
            "            [\n" +
            "            1549843200000,\n" +
            "            3675.6916291247258\n" +
            "            ],\n" +
            "            [\n" +
            "            1549929600000,\n" +
            "            3631.444539729949\n" +
            "            ],\n" +
            "            [\n" +
            "            1550016000000,\n" +
            "            3633.9650467461183\n" +
            "            ],\n" +
            "            [\n" +
            "            1550102400000,\n" +
            "            3610.0622730634022\n" +
            "            ],\n" +
            "            [\n" +
            "            1550188800000,\n" +
            "            3589.661829626318\n" +
            "            ],\n" +
            "            [\n" +
            "            1550275200000,\n" +
            "            3601.2291913968643\n" +
            "            ],\n" +
            "            [\n" +
            "            1550361600000,\n" +
            "            3622.5916530750715\n" +
            "            ],\n" +
            "            [\n" +
            "            1550448000000,\n" +
            "            3665.075530800723\n" +
            "            ],\n" +
            "            [\n" +
            "            1550534400000,\n" +
            "            3886.5202004751386\n" +
            "            ],\n" +
            "            [\n" +
            "            1550620800000,\n" +
            "            3915.0099712770552\n" +
            "            ],\n" +
            "            [\n" +
            "            1550707200000,\n" +
            "            3976.808906528991\n" +
            "            ],\n" +
            "            [\n" +
            "            1550793600000,\n" +
            "            3934.813593894817\n" +
            "            ],\n" +
            "            [\n" +
            "            1550880000000,\n" +
            "            3973.6413227746357\n" +
            "            ],\n" +
            "            [\n" +
            "            1550966400000,\n" +
            "            4115.3379267876635\n" +
            "            ],\n" +
            "            [\n" +
            "            1551052800000,\n" +
            "            3742.868946700387\n" +
            "            ],\n" +
            "            [\n" +
            "            1551139200000,\n" +
            "            3824.683504217102\n" +
            "            ],\n" +
            "            [\n" +
            "            1551225600000,\n" +
            "            3809.209774682024\n" +
            "            ],\n" +
            "            [\n" +
            "            1551312000000,\n" +
            "            3813.377891881294\n" +
            "            ],\n" +
            "            [\n" +
            "            1551398400000,\n" +
            "            3815.6076075249844\n" +
            "            ],\n" +
            "            [\n" +
            "            1551484800000,\n" +
            "            3815.753939922755\n" +
            "            ],\n" +
            "            [\n" +
            "            1551571200000,\n" +
            "            3819.645730582585\n" +
            "            ],\n" +
            "            [\n" +
            "            1551657600000,\n" +
            "            3806.9542737979564\n" +
            "            ],\n" +
            "            [\n" +
            "            1551744000000,\n" +
            "            3712.812457297799\n" +
            "            ],\n" +
            "            [\n" +
            "            1551830400000,\n" +
            "            3852.461665785486\n" +
            "            ],\n" +
            "            [\n" +
            "            1551916800000,\n" +
            "            3860.510762380684\n" +
            "            ],\n" +
            "            [\n" +
            "            1552003200000,\n" +
            "            3871.637247965991\n" +
            "            ],\n" +
            "            [\n" +
            "            1552089600000,\n" +
            "            3861.879307396147\n" +
            "            ],\n" +
            "            [\n" +
            "            1552176000000,\n" +
            "            3941.600873271771\n" +
            "            ],\n" +
            "            [\n" +
            "            1552262400000,\n" +
            "            3923.559788806432\n" +
            "            ],\n" +
            "            [\n" +
            "            1552348800000,\n" +
            "            3860.3592300287123\n" +
            "            ],\n" +
            "            [\n" +
            "            1552435200000,\n" +
            "            3876.480852754649\n" +
            "            ],\n" +
            "            [\n" +
            "            1552521600000,\n" +
            "            3865.817782663389\n" +
            "            ],\n" +
            "            [\n" +
            "            1552608000000,\n" +
            "            3876.6987080329527\n" +
            "            ],\n" +
            "            [\n" +
            "            1552694400000,\n" +
            "            3925.460579704576\n" +
            "            ],\n" +
            "            [\n" +
            "            1552780800000,\n" +
            "            4007.709463818873\n" +
            "            ],\n" +
            "            [\n" +
            "            1552867200000,\n" +
            "            3980.9901047320204\n" +
            "            ],\n" +
            "            [\n" +
            "            1552953600000,\n" +
            "            3986.7068940461468\n" +
            "            ],\n" +
            "            [\n" +
            "            1553040000000,\n" +
            "            4013.5769693454813\n" +
            "            ],\n" +
            "            [\n" +
            "            1553126400000,\n" +
            "            4045.57263350974\n" +
            "            ],\n" +
            "            [\n" +
            "            1553212800000,\n" +
            "            3983.317781297362\n" +
            "            ],\n" +
            "            [\n" +
            "            1553299200000,\n" +
            "            3989.1584912154526\n" +
            "            ],\n" +
            "            [\n" +
            "            1553385600000,\n" +
            "            4008.945386314754\n" +
            "            ],\n" +
            "            [\n" +
            "            1553472000000,\n" +
            "            3993.394900459549\n" +
            "            ],\n" +
            "            [\n" +
            "            1553558400000,\n" +
            "            3927.577296521434\n" +
            "            ],\n" +
            "            [\n" +
            "            1553644800000,\n" +
            "            3938.659743156943\n" +
            "            ],\n" +
            "            [\n" +
            "            1553731200000,\n" +
            "            4036.1609179207603\n" +
            "            ],\n" +
            "            [\n" +
            "            1553817600000,\n" +
            "            4028.3913011796208\n" +
            "            ],\n" +
            "            [\n" +
            "            1553904000000,\n" +
            "            4103.322859513963\n" +
            "            ],\n" +
            "            [\n" +
            "            1553990400000,\n" +
            "            4103.856421333923\n" +
            "            ],\n" +
            "            [\n" +
            "            1554076800000,\n" +
            "            4103.521738549332\n" +
            "            ],\n" +
            "            [\n" +
            "            1554163200000,\n" +
            "            4146.321927706636\n" +
            "            ],\n" +
            "            [\n" +
            "            1554249600000,\n" +
            "            4862.230427766917\n" +
            "            ],\n" +
            "            [\n" +
            "            1554336000000,\n" +
            "            4955.831824018296\n" +
            "            ],\n" +
            "            [\n" +
            "            1554422400000,\n" +
            "            4899.021553783287\n" +
            "            ],\n" +
            "            [\n" +
            "            1554508800000,\n" +
            "            5014.955826888765\n" +
            "            ],\n" +
            "            [\n" +
            "            1554595200000,\n" +
            "            5041.869075888309\n" +
            "            ],\n" +
            "            [\n" +
            "            1554681600000,\n" +
            "            5176.97788362072\n" +
            "            ],\n" +
            "            [\n" +
            "            1554768000000,\n" +
            "            5266.89477647303\n" +
            "            ],\n" +
            "            [\n" +
            "            1554854400000,\n" +
            "            5172.1752647597095\n" +
            "            ],\n" +
            "            [\n" +
            "            1554940800000,\n" +
            "            5308.905683164441\n" +
            "            ],\n" +
            "            [\n" +
            "            1555027200000,\n" +
            "            5047.79316241402\n" +
            "            ],\n" +
            "            [\n" +
            "            1555113600000,\n" +
            "            5082.46515242293\n" +
            "            ],\n" +
            "            [\n" +
            "            1555200000000,\n" +
            "            5072.987191337883\n" +
            "            ],\n" +
            "            [\n" +
            "            1555286400000,\n" +
            "            5149.314492798127\n" +
            "            ],\n" +
            "            [\n" +
            "            1555372800000,\n" +
            "            5039.881017404402\n" +
            "            ],\n" +
            "            [\n" +
            "            1555459200000,\n" +
            "            5213.598376962082\n" +
            "            ],\n" +
            "            [\n" +
            "            1555545600000,\n" +
            "            5218.957150384528\n" +
            "            ],\n" +
            "            [\n" +
            "            1555632000000,\n" +
            "            5274.767013336503\n" +
            "            ],\n" +
            "            [\n" +
            "            1555718400000,\n" +
            "            5280.354444383007\n" +
            "            ],\n" +
            "            [\n" +
            "            1555804800000,\n" +
            "            5307.689183437135\n" +
            "            ],\n" +
            "            [\n" +
            "            1555891200000,\n" +
            "            5311.164840299274\n" +
            "            ],\n" +
            "            [\n" +
            "            1555977600000,\n" +
            "            5380.935789575588\n" +
            "            ],\n" +
            "            [\n" +
            "            1556064000000,\n" +
            "            5557.9257276108365\n" +
            "            ],\n" +
            "            [\n" +
            "            1556150400000,\n" +
            "            5452.101753394462\n" +
            "            ],\n" +
            "            [\n" +
            "            1556236800000,\n" +
            "            5203.088598730703\n" +
            "            ],\n" +
            "            [\n" +
            "            1556323200000,\n" +
            "            5295.847320048228\n" +
            "            ],\n" +
            "            [\n" +
            "            1556409600000,\n" +
            "            5256.977802474059\n" +
            "            ],\n" +
            "            [\n" +
            "            1556496000000,\n" +
            "            5257.803249232503\n" +
            "            ],\n" +
            "            [\n" +
            "            1556582400000,\n" +
            "            5199.755008563461\n" +
            "            ],\n" +
            "            [\n" +
            "            1556668800000,\n" +
            "            5292.803974562054\n" +
            "            ],\n" +
            "            [\n" +
            "            1556755200000,\n" +
            "            5354.5868001739655\n" +
            "            ],\n" +
            "            [\n" +
            "            1556841600000,\n" +
            "            5450.706620486424\n" +
            "            ],\n" +
            "            [\n" +
            "            1556928000000,\n" +
            "            5731.493099606555\n" +
            "            ],\n" +
            "            [\n" +
            "            1557014400000,\n" +
            "            5803.943309048437\n" +
            "            ],\n" +
            "            [\n" +
            "            1557100800000,\n" +
            "            5749.323780460327\n" +
            "            ],\n" +
            "            [\n" +
            "            1557187200000,\n" +
            "            5715.7571359661015\n" +
            "            ],\n" +
            "            [\n" +
            "            1557273600000,\n" +
            "            5841.053771273751\n" +
            "            ],\n" +
            "            [\n" +
            "            1557360000000,\n" +
            "            5957.856732858399\n" +
            "            ],\n" +
            "            [\n" +
            "            1557446400000,\n" +
            "            6168.268996398258\n" +
            "            ],\n" +
            "            [\n" +
            "            1557532800000,\n" +
            "            6370.233758607852\n" +
            "            ],\n" +
            "            [\n" +
            "            1557619200000,\n" +
            "            7258.261280655222\n" +
            "            ],\n" +
            "            [\n" +
            "            1557705600000,\n" +
            "            6953.75138858638\n" +
            "            ],\n" +
            "            [\n" +
            "            1557792000000,\n" +
            "            7805.98164948938\n" +
            "            ],\n" +
            "            [\n" +
            "            1557878400000,\n" +
            "            7990.055553534356\n" +
            "            ],\n" +
            "            [\n" +
            "            1557964800000,\n" +
            "            8192.223138952013\n" +
            "            ],\n" +
            "            [\n" +
            "            1558051200000,\n" +
            "            7875.913199526066\n" +
            "            ],\n" +
            "            [\n" +
            "            1558137600000,\n" +
            "            7343.371457162994\n" +
            "            ],\n" +
            "            [\n" +
            "            1558224000000,\n" +
            "            7300.655158341227\n" +
            "            ],\n" +
            "            [\n" +
            "            1558310400000,\n" +
            "            8168.730689783752\n" +
            "            ],\n" +
            "            [\n" +
            "            1558396800000,\n" +
            "            7976.851712899625\n" +
            "            ],\n" +
            "            [\n" +
            "            1558483200000,\n" +
            "            7958.365526075088\n" +
            "            ],\n" +
            "            [\n" +
            "            1558569600000,\n" +
            "            7665.79685383355\n" +
            "            ],\n" +
            "            [\n" +
            "            1558656000000,\n" +
            "            7861.812792465447\n" +
            "            ],\n" +
            "            [\n" +
            "            1558742400000,\n" +
            "            7977.244882467973\n" +
            "            ],\n" +
            "            [\n" +
            "            1558828800000,\n" +
            "            8037.627431860584\n" +
            "            ],\n" +
            "            [\n" +
            "            1558915200000,\n" +
            "            8631.080577844017\n" +
            "            ],\n" +
            "            [\n" +
            "            1559001600000,\n" +
            "            8816.03391493351\n" +
            "            ],\n" +
            "            [\n" +
            "            1559088000000,\n" +
            "            8726.978110456395\n" +
            "            ],\n" +
            "            [\n" +
            "            1559174400000,\n" +
            "            8650.677405968292\n" +
            "            ],\n" +
            "            [\n" +
            "            1559260800000,\n" +
            "            8310.891063057417\n" +
            "            ],\n" +
            "            [\n" +
            "            1559347200000,\n" +
            "            8575.646353354166\n" +
            "            ],\n" +
            "            [\n" +
            "            1559433600000,\n" +
            "            8554.261728092017\n" +
            "            ],\n" +
            "            [\n" +
            "            1559520000000,\n" +
            "            8743.705469134235\n" +
            "            ],\n" +
            "            [\n" +
            "            1559606400000,\n" +
            "            8173.632761796738\n" +
            "            ],\n" +
            "            [\n" +
            "            1559692800000,\n" +
            "            7683.636261419786\n" +
            "            ],\n" +
            "            [\n" +
            "            1559779200000,\n" +
            "            7812.44885473215\n" +
            "            ],\n" +
            "            [\n" +
            "            1559865600000,\n" +
            "            7821.123897094859\n" +
            "            ],\n" +
            "            [\n" +
            "            1559952000000,\n" +
            "            8036.108159122215\n" +
            "            ],\n" +
            "            [\n" +
            "            1560038400000,\n" +
            "            7942.539284064289\n" +
            "            ],\n" +
            "            [\n" +
            "            1560124800000,\n" +
            "            7668.029627860183\n" +
            "            ],\n" +
            "            [\n" +
            "            1560211200000,\n" +
            "            8002.729982894047\n" +
            "            ],\n" +
            "            [\n" +
            "            1560297600000,\n" +
            "            7897.975476598364\n" +
            "            ],\n" +
            "            [\n" +
            "            1560384000000,\n" +
            "            8145.05909812096\n" +
            "            ],\n" +
            "            [\n" +
            "            1560470400000,\n" +
            "            8227.625725590904\n" +
            "            ],\n" +
            "            [\n" +
            "            1560556800000,\n" +
            "            8671.505388660793\n" +
            "            ],\n" +
            "            [\n" +
            "            1560643200000,\n" +
            "            8818.609506932296\n" +
            "            ],\n" +
            "            [\n" +
            "            1560729600000,\n" +
            "            8978.77258780693\n" +
            "            ],\n" +
            "            [\n" +
            "            1560816000000,\n" +
            "            9312.868294808708\n" +
            "            ],\n" +
            "            [\n" +
            "            1560902400000,\n" +
            "            9094.021746079217\n" +
            "            ],\n" +
            "            [\n" +
            "            1560988800000,\n" +
            "            9285.928674251472\n" +
            "            ],\n" +
            "            [\n" +
            "            1561075200000,\n" +
            "            9548.31456526146\n" +
            "            ],\n" +
            "            [\n" +
            "            1561161600000,\n" +
            "            10118.359455626854\n" +
            "            ],\n" +
            "            [\n" +
            "            1561248000000,\n" +
            "            10704.922148115673\n" +
            "            ],\n" +
            "            [\n" +
            "            1561334400000,\n" +
            "            10846.12509964766\n" +
            "            ],\n" +
            "            [\n" +
            "            1561420800000,\n" +
            "            11045.550419814825\n" +
            "            ],\n" +
            "            [\n" +
            "            1561507200000,\n" +
            "            11808.419904075861\n" +
            "            ],\n" +
            "            [\n" +
            "            1561593600000,\n" +
            "            12996.124342788677\n" +
            "            ],\n" +
            "            [\n" +
            "            1561680000000,\n" +
            "            11178.682998186501\n" +
            "            ],\n" +
            "            [\n" +
            "            1561766400000,\n" +
            "            12352.19686591269\n" +
            "            ],\n" +
            "            [\n" +
            "            1561852800000,\n" +
            "            11972.555667220913\n" +
            "            ],\n" +
            "            [\n" +
            "            1561939200000,\n" +
            "            10888.097181679726\n" +
            "            ],\n" +
            "            [\n" +
            "            1562025600000,\n" +
            "            10639.046292232071\n" +
            "            ],\n" +
            "            [\n" +
            "            1562112000000,\n" +
            "            10822.764178376405\n" +
            "            ],\n" +
            "            [\n" +
            "            1562198400000,\n" +
            "            11975.637393818817\n" +
            "            ],\n" +
            "            [\n" +
            "            1562284800000,\n" +
            "            11229.60561912491\n" +
            "            ],\n" +
            "            [\n" +
            "            1562371200000,\n" +
            "            10987.248856214646\n" +
            "            ],\n" +
            "            [\n" +
            "            1562457600000,\n" +
            "            11279.476377672418\n" +
            "            ],\n" +
            "            [\n" +
            "            1562544000000,\n" +
            "            11436.430746923896\n" +
            "            ],\n" +
            "            [\n" +
            "            1562630400000,\n" +
            "            12260.28269787262\n" +
            "            ],\n" +
            "            [\n" +
            "            1562716800000,\n" +
            "            12544.167261709219\n" +
            "            ],\n" +
            "            [\n" +
            "            1562803200000,\n" +
            "            12166.398288868606\n" +
            "            ],\n" +
            "            [\n" +
            "            1562889600000,\n" +
            "            11385.060490564521\n" +
            "            ],\n" +
            "            [\n" +
            "            1562976000000,\n" +
            "            11804.562793905448\n" +
            "            ],\n" +
            "            [\n" +
            "            1563024749000,\n" +
            "            11474.31414589836\n" +
            "            ]\n" +
            "        ]\n" +
            "        }";





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
                    myIntent = new Intent(CryptoDetail.this, MainActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);
                    return true;
                case R.id.navigation_portfolio:
                    myIntent = new Intent(CryptoDetail.this, Portfolio.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);
                    return true;
                case R.id.navigation_all_crypto:
                    myIntent = new Intent(CryptoDetail.this, AllCryptos.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);
                    return true;
            }
            return false;
        }
    };

}
