package com.example.cointracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

public class AllCryptos extends AppCompatActivity implements ListOfCrypto.Listener {
    private ListOfCrypto cryptoList = null;
    private RecyclerView mRecyclerView;
    private AllCryptoAdapter mAdapter;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent myIntent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    myIntent = new Intent(AllCryptos.this, MainActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);
                    return true;
                case R.id.navigation_portfolio:
                    myIntent = new Intent(AllCryptos.this, Portfolio.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);
                    return true;
                case R.id.navigation_all_crypto:
                    return true;
            }
            return false;
        }
    };

//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_cryptos);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_all_crypto);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        cryptoList = ListOfCrypto.getInstance();
        cryptoList.registerListener(this);

        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview10);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new AllCryptoAdapter(this, cryptoList.getListCDP());
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_all_crypto);
    }

    @Override
    public void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}