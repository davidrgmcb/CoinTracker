package com.example.cointracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;


public class MainActivity extends AppCompatActivity implements ListOfCrypto.Listener{
    private ListOfCrypto cryptoList = null;
    public String portfolioFilename = "myPortfolio.txt";
    public String getPortfolioFilename(){
        return portfolioFilename;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
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


    public void display(View view) {
        //Toast.makeText(this, cryptoList.getListCDP()[1].name, Toast.LENGTH_SHORT).show();

        //go to all cryptos activity
        //Intent myIntent = new Intent(MainActivity.this, AllCryptos.class);
        //MainActivity.this.startActivity(myIntent);

        cryptoList.update("USD");
        //go to crypto detail activity
        //Intent myIntent = new Intent(MainActivity.this, CryptoDetail.class);
        //MainActivity.this.startActivity(myIntent);
    }

    @Override
    public void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

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

}

