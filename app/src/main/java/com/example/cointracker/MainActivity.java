package com.example.cointracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private ListOfCrypto cryptoList = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent myIntent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return false;
                case R.id.navigation_portfolio:
                    myIntent = new Intent(MainActivity.this, Portfolio.class);
                    startActivity(myIntent);
                    return true;
                case R.id.navigation_all_crypto:
                    myIntent = new Intent(MainActivity.this, AllCryptos.class);
                    startActivity(myIntent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        cryptoList = cryptoList.getInstance();

    }

    public void display(View view) {
        Toast.makeText(this, cryptoList.getListCDP()[1].name, Toast.LENGTH_LONG).show();

        //go to all cryptos activity
        //Intent myIntent = new Intent(MainActivity.this, AllCryptos.class);
        //MainActivity.this.startActivity(myIntent);

        cryptoList.update("USD", new WeakReference<Activity>(this));
        //go to crypto detail activity
        //Intent myIntent = new Intent(MainActivity.this, CryptoDetail.class);
        //MainActivity.this.startActivity(myIntent);
    }

    static void update() {

    }
}

