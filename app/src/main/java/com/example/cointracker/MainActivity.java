package com.example.cointracker;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;
import com.google.gson.Gson;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;

    //This is a test to see if portfolio button works.
    public int portfolioButtonWorks = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    public MainActivity() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    CryptoDataPoints cryptoDataPoints = new CryptoDataPoints();
    //String urlForecast = "https://api.openweathermap.org/data/2.5/forecast?q=";
    String cryptoResponse = cryptoDataPoints.getCryptoData("USD" );
        //System.out.println("weather forecast url: " + urlForecast + city + format + apiKey);

    cryptoDataPoints = new Gson().fromJson(cryptoResponse, CryptoDataPoints.class);
    //System.out.println("Forecast unformatted:\n" + forecastResponse);
        //forecast.displayForecast();

}

