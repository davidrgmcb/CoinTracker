package com.example.cointracker;

import android.content.Context;
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

import java.io.InputStream;

public class CryptoDetail extends AppCompatActivity implements ListOfCrypto.Listener {
    private ListOfCrypto cryptoList = null;
    //CryptoDataPoints cryptoDetailArray[];
    int arrayPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_detail);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        cryptoList = ListOfCrypto.getInstance();
        cryptoList.registerListener(this);
        //cryptoDetailArray = cryptoList.getListCDP();
        //get the array position of the coin by subtracting 1 from the crypto rank passed in
        arrayPosition = (int)getIntent().getDoubleExtra("id", -1) - 1;

        TextView name = findViewById(R.id.cryptoName);
        name.setText("$"+cryptoList.getListCDP()[arrayPosition].name);

        TextView currentPrice = findViewById(R.id.currentPrice);
        currentPrice.setText("$"+cryptoList.getListCDP()[arrayPosition].current_price);


        //load crypto image
        new DownloadImageTask((ImageView) findViewById(R.id.imageView))
                .execute(cryptoList.getListCDP()[arrayPosition].image);


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
                    startActivity(myIntent);
                    return true;
                case R.id.navigation_portfolio:
                    myIntent = new Intent(CryptoDetail.this, Portfolio.class);
                    startActivity(myIntent);
                    return true;
                case R.id.navigation_all_crypto:
                    myIntent = new Intent(CryptoDetail.this, AllCryptos.class);
                    startActivity(myIntent);
                    return true;
            }
            return false;
        }
    };

}
