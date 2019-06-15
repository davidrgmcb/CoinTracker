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
        getCrypto();
    }

    void getCrypto(){
        CryptoTask task = new CryptoTask("USD");
        task.start();

    }

    private class CryptoTask extends Thread {
        String _currency;
        CryptoTask(String currency) {
            _currency = currency;
        }

        @Override
        public void run() {
            CryptoDataPoints cryptoDataPoints = new CryptoDataPoints();
            String cryptoResponse = null;
            try {
                cryptoResponse = cryptoDataPoints.getCryptoData("USD" );
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(cryptoResponse);

            CryptoDataPoints[] cryptoDataPoints2 = new Gson().fromJson(cryptoResponse, CryptoDataPoints[].class);
            System.out.println(cryptoDataPoints2[0].id + cryptoDataPoints2[0].current_price
            + cryptoDataPoints2[1].id + cryptoDataPoints2[1].current_price);
        }
    }


}

