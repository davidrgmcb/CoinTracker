package com.example.cointracker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListOfCrypto {
    private ListOfCrypto() {
        CryptoTask task = new CryptoTask("USD");
        task.start();
    }

    public interface Listener {
        void updateUI();
    }

    private List<Listener> mListener = null;
    public void registerListener (Listener listener) {
        if(mListener == null) {
            mListener = new ArrayList<>();
        }
        if(!mListener.contains(listener))
            mListener.add(listener);
    }

    private static ListOfCrypto INSTANCE = null;
    //private CryptoDataPoints[] listCDP;
    List<CryptoDataPoints> listCDP = new ArrayList<>();

    public synchronized static ListOfCrypto getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ListOfCrypto();
        }
        return INSTANCE;
    }

    //public synchronized CryptoDataPoints[] getListCDP() {
      //  return listCDP;
    //}
    public synchronized List<CryptoDataPoints> getListCDP() {
        return listCDP;
    }


    //private synchronized void setListCDP(CryptoDataPoints[] listCDP) {
       // this.listCDP = listCDP;
    //}
    private synchronized void setListCDP(List<CryptoDataPoints> listCDP) {
        this.listCDP = listCDP;
    }


    public synchronized void update(String currency) {
        CryptoTask task = new CryptoTask(currency);
        task.start();
    }

    private class CryptoTask extends Thread {
        String _currency;

        CryptoTask(String currency) {
            _currency = currency;
        }

        @Override
        public synchronized void run() {
            String cryptoResponse = null;
            try {
                cryptoResponse = CryptoDataPoints.getCryptoData(_currency);
            } catch (IOException e) {
                e.printStackTrace();
            }
//
            Type cryptoType = new TypeToken<ArrayList<CryptoDataPoints>>(){}.getType();
            setListCDP((List<CryptoDataPoints>) new Gson().fromJson(cryptoResponse, cryptoType));
            //setListCDP(new Gson().fromJson(cryptoResponse, CryptoDataPoints[].class));
            if(mListener != null) {
                for (Listener l: mListener) {
                    l.updateUI();
                }
            }
        }
    }
}