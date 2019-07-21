package com.example.cointracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Transaction extends AppCompatActivity {

    private ListOfCrypto cryptoList = null;
    //CryptoDataPoints cryptoDetailArray[];
    List<CryptoDataPoints> listCDP = new ArrayList<>();
    int arrayPosition;
    EditText et_quantityPurchased;
    EditText et_amountExchanged;
    TextView tv_pricePerCoin;

    String id;
    double quantityPurchased;
    double pricePerCoin;
    double amountExchanged;
    List<PortfolioData> portfolioDataEntries = new ArrayList<>();

    public class PortfolioData{
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
        listCDP = cryptoList.getListCDP();
        //get the array position of the coin from the intent
        arrayPosition = getIntent().getIntExtra("arrayPosition", -1) ;

        id = cryptoList.getListCDP().get(arrayPosition).id;

        TextView symbol = findViewById(R.id.symbol);
        symbol.setText(cryptoList.getListCDP().get(arrayPosition).symbol);

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

        Type portfolioType = new TypeToken<ArrayList<PortfolioData>>(){}.getType();

        portfolioDataEntries = new Gson().fromJson(text, portfolioType);
        System.out.println("*** json parsed");

        tv_pricePerCoin = findViewById(R.id.price_per_coin);
        et_quantityPurchased = findViewById(R.id.quantity_purchased);
        et_amountExchanged = findViewById(R.id.amount_exchanged);
        et_quantityPurchased.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && et_quantityPurchased.getText().toString() != "" && et_amountExchanged.getText().toString() != ""){
                    String string_quantityPurchased = et_quantityPurchased.getText().toString();
                    quantityPurchased = Double.parseDouble(string_quantityPurchased);
                    if (amountExchanged != 0 && quantityPurchased != 0){
                        pricePerCoin = amountExchanged/quantityPurchased;
                        tv_pricePerCoin.setText(Double.toString(pricePerCoin));
                    }
                }
            }
        });
        et_amountExchanged.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && et_amountExchanged.getText().toString() != "" && et_quantityPurchased.getText().toString() != ""){
                    String string_amountExchanged = et_amountExchanged.getText().toString();
                    amountExchanged = Double.parseDouble(string_amountExchanged);
                    if (amountExchanged != 0 && quantityPurchased != 0){
                        pricePerCoin = amountExchanged/quantityPurchased;
                        tv_pricePerCoin.setText(Double.toString(pricePerCoin));
                    }

                }
            }
        });


    }



    public void saveTransaction(View v){

        boolean coinFound = false;
        if (portfolioDataEntries.isEmpty()){
            //create the first entry
            PortfolioData newEntry = new PortfolioData();
            newEntry.id = id;
            newEntry.totalQuantityOwned = quantityPurchased;
            newEntry.weightedAveragePriceUSD = pricePerCoin;
            portfolioDataEntries.add(newEntry);
            System.out.println("*** first entry.id: " + newEntry.id);
        }else {
            for (int ii = 0; ii < portfolioDataEntries.size(); ii++){
                //System.out.println("*** for each entry.id: " + entry.id);
                if (portfolioDataEntries.get(ii).id.equals(id) ){
                    //update entry with weightedAveragePrice and totalQuantityOwned
                    portfolioDataEntries.get(ii).totalQuantityOwned += quantityPurchased;
                    portfolioDataEntries.get(ii).weightedAveragePriceUSD =
                            (portfolioDataEntries.get(ii).weightedAveragePriceUSD + (pricePerCoin * quantityPurchased))
                                    / portfolioDataEntries.get(ii).totalQuantityOwned;
                    //break;
                    coinFound = true;
                    break;
                }
            }
            if (coinFound == false){
                //create new entry and add to collection
                PortfolioData newEntry = new PortfolioData();
                newEntry.id = id;
                newEntry.totalQuantityOwned = quantityPurchased;
                newEntry.weightedAveragePriceUSD = pricePerCoin;
                portfolioDataEntries.add(newEntry);
                System.out.println("*** new entry.id: " + newEntry.id);
            }

        }

        Gson gson = new Gson();
        String updatedPortfolio = gson.toJson(portfolioDataEntries);

        try {
            System.out.println("*** updating portfolio file...");
            FileOutputStream outputStream;
            String fileContents = updatedPortfolio;
            outputStream = openFileOutput("myPortfolio.txt", Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
            System.out.println("*** " + getFilesDir()+"/"+"myPortfolio.txt"+ " updated!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent i = new Intent(this, Portfolio.class);
        startActivity(i);
    }


}
