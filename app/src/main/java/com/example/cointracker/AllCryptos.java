package com.example.cointracker;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class AllCryptos extends AppCompatActivity {
    private TextView mTextMessage;

    CoinRecyclerViewAdapter adapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_activity_all_cryptos);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_activity_portfolio);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_cryptos);

        //Junk data for testing
        ArrayList<String> dummyCoinData = new ArrayList<String>();
        dummyCoinData.add("Bitcoin: $20,000, Week High: $20,314");
        dummyCoinData.add("Ethereum: $14,000, Week High: $14,000");
        dummyCoinData.add("LiteCoin: $5000, Week High: $5200");

        RecyclerView recyclerView = findViewById(R.id.rv_allCrypto);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CoinRecyclerViewAdapter(this, dummyCoinData);
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
