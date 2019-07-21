package com.example.cointracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ListOfCrypto.Listener{
    private ListOfCrypto cryptoList = null;
    public String portfolioFilename = "myPortfolio.txt";
    private List<CryptoDataPoints> sortList = null;
    ListView gainList;
    ListView lossList;

    class SortByGain implements Comparator<CryptoDataPoints>
    {
        public int compare(CryptoDataPoints a, CryptoDataPoints b)
        {
            return (int)(a.percentGainLoss - b.percentGainLoss);
        }
    }

    class SortByLoss implements Comparator<CryptoDataPoints>
    {
        public int compare(CryptoDataPoints a, CryptoDataPoints b)
        {
            return (int)(b.percentGainLoss - a.percentGainLoss);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_home);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        cryptoList = ListOfCrypto.getInstance();
        cryptoList.registerListener(this);

        //check if portfolio file exists, if not, create it
        File file = new File(getFilesDir()+"/"+portfolioFilename);
        if(file.exists())
            System.out.println("*** " + file + " file exists!");
        else
            try {
                System.out.println("*** creating new file...");
                FileOutputStream outputStream;
                String fileContents = "[]";
                outputStream = openFileOutput(portfolioFilename, Context.MODE_PRIVATE);
                outputStream.write(fileContents.getBytes());
                outputStream.close();
                System.out.println("*** " + getFilesDir()+"/"+portfolioFilename+ " created!");
            } catch (Exception e) {
                e.printStackTrace();
            }
//
        /*
        System.out.println("*** deleting file...");
        file.delete();
        if(file.exists())
            System.out.println("*** " + file + " file exists!");
        else
            System.out.println("*** file does not exist");
            */
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_home);
    }

    public void display(View view) {
        //Toast.makeText(this, cryptoList.getListCDP()[1].name, Toast.LENGTH_SHORT).show();

        //go to all cryptos activity
        //Intent myIntent = new Intent(MainActivity.this, AllCryptos.class);
        //MainActivity.this.startActivity(myIntent);

        cryptoList.update("USD");
        //go to crypto detail activity
        //Intent myIntent = new Intent(MainActivity.this, Detail.class);
        //MainActivity.this.startActivity(myIntent);
    }

    @Override
    public void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sortList = new ArrayList<>(cryptoList.getListCDP());
                Collections.sort(sortList, new SortByGain());
                List<CryptoDataPoints> temp = new ArrayList<>();
                for (int i = 0; i < 3; i++)
                {
                    temp.add(sortList.get(i));
                }
                ArrayAdapter gainAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, temp);
                gainList.setAdapter(gainAdapter);
                Collections.sort(sortList, new SortByLoss());
                temp.clear();
                for (int i = 0; i < 3; i++)
                {
                    temp.add(sortList.get(i));
                }
                ArrayAdapter lossAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, temp);
                lossList.setAdapter(lossAdapter);
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent myIntent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_portfolio:
                    myIntent = new Intent(MainActivity.this, Portfolio.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);
                    return true;
                case R.id.navigation_all_crypto:
                    myIntent = new Intent(MainActivity.this, AllCryptos.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);
                    return true;
            }
            return false;
        }
    };

}

