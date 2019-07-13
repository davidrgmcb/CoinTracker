package com.example.cointracker;

import android.app.Activity;
import android.widget.Toast;

import com.google.gson.Gson;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class ListOfCrypto {
    private ListOfCrypto() {
        CryptoTask task = new CryptoTask("USD");
        task.start();
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

    public synchronized void update(String currency, WeakReference<Activity> currentUI) {
        CryptoTask task = new CryptoTask(currency, currentUI);
        task.start();
    }

    private class CryptoTask extends Thread {
        String _currency;
        WeakReference<Activity> _currentUI = null;

        CryptoTask(String currency) {
            _currency = currency;
        }

        CryptoTask(String currency, WeakReference<Activity> currentUI){
            _currency = currency;
            _currentUI = currentUI;
        }

        @Override
        public synchronized void run() {
            String cryptoResponse = null;
            try {
                cryptoResponse = CryptoDataPoints.getCryptoData(_currency);
            } catch (IOException e) {
                e.printStackTrace();
            }

            setListCDP(new Gson().fromJson(cryptoResponse, CryptoDataPoints[].class));
            if (_currentUI != null)
            {
                _currentUI.get().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch(_currentUI.get().getLocalClassName())
                        {
                            case "MainActivity":

                                break;
                            case "AllCryptos":
                                Toast.makeText(_currentUI.get(), "Even finished", Toast.LENGTH_SHORT).show();
                                break;
                                default:
                                    break;
                        }
                    }
                });
            }
        }
    }
}
