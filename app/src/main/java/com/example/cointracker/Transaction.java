package com.example.cointracker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Transaction extends AppCompatActivity {

    private ListOfCrypto cryptoList = null;
    CryptoDataPoints cryptoDetailArray[];
    int arrayPosition;

    String id;
    double quantityPurchased;
    double pricePerCoin;
    double amountExchanged;
    List<PortfolioData> portfolioDataEntries = new ArrayList<>();

    class PortfolioData{
        String id;
        double totalQuantityOwned;
        double weightedAveragePriceUSD;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cryptoList = cryptoList.getInstance();
        cryptoDetailArray = cryptoList.getListCDP();
        //get the array position of the coin by subtracting 1 from the crypto rank passed in
        arrayPosition = (int)getIntent().getDoubleExtra("arrayPosition", -1) ;

        id = cryptoList.getListCDP()[arrayPosition].id;

        quantityPurchased = 20; //just for testing. should get value from textedit/user input
        amountExchanged = 100;
        pricePerCoin = amountExchanged/quantityPurchased;


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
        System.out.println("****"+text);

        portfolioDataEntries = (List<PortfolioData>) new Gson().fromJson(text, PortfolioData.class);


        for (PortfolioData entry: portfolioDataEntries){
            System.out.println("***" + entry.id);
            if (entry.id == id){
                //update entry with weightedAveragePrice and totalQuantityOwned
                entry.totalQuantityOwned += quantityPurchased;
                entry.weightedAveragePriceUSD =
                        (entry.weightedAveragePriceUSD + (pricePerCoin * quantityPurchased))
                                / entry.totalQuantityOwned;
            }else{
                //create new entry and add to collection
                PortfolioData newEntry = new PortfolioData();
                newEntry.id = id;
                newEntry.totalQuantityOwned = quantityPurchased;
                newEntry.weightedAveragePriceUSD = pricePerCoin;
                portfolioDataEntries.add(newEntry);
            }
        }

//
    }



}
