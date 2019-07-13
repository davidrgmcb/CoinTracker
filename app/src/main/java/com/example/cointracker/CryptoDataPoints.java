package com.example.cointracker;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CryptoDataPoints {

    //data points from CoinGeckoAPI: https://www.coingecko.com/api/documentations/v3#/

    String
            id,
            symbol,
            name,
            image,
            ath_date,
            last_updated;
    double
            current_price,
            market_cap,
            market_cap_rank,
            total_volume,                       // i.e. 24h volume
            high_24h,
            low_24h,
            price_change_24h,
            price_change_percentage_24h,
            market_cap_change_24h,
            market_cap_change_percentage_24h,
            ath,                                // ath == all time high
            ath_change_percentage;

//

    //coinGecko api is public, no key required
    static String baseUrl = "https://api.coingecko.com/api/v3/";

    //endpoints
    static String markets = "coins/markets?vs_currency="; //for getting list of all crypto data

    //params
    //String currency; //assign one of Currencies enumerated values, required param
    String order = "&order="; //assign one of Sorting enumerated values, optional param
    String per_page = "&per_page=250"; //up to 250 coins can be returned, optional param


    //using square OkHttp library for api callouts
    static OkHttpClient client = new OkHttpClient();

    static String getCryptoData(String _currency) throws IOException {
        String url = baseUrl + markets + _currency;
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }catch (IOException e){
            return "error";
        }
    }

}
