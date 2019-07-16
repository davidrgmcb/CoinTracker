package com.example.cointracker;

import com.google.gson.Gson;
import java.io.IOException;
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
        mListener.add(listener);
    }

    private static ListOfCrypto INSTANCE = null;
    private CryptoDataPoints[] listCDP;

    public synchronized static ListOfCrypto getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ListOfCrypto();
        }
        return INSTANCE;
    }

    public synchronized CryptoDataPoints[] getListCDP() {
        return listCDP;
    }

    private synchronized void setListCDP(CryptoDataPoints[] listCDP) {
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
            setListCDP(new Gson().fromJson(cryptoResponse, CryptoDataPoints[].class));
            if(mListener != null) {
                for (Listener l: mListener) {
                    l.updateUI();
                }
            }
        }
    }
}