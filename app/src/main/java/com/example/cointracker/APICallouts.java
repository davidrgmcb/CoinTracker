package com.example.cointracker;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APICallouts { // maybe this class is not needed?? moved code to CryptoDataPoints

    enum Currencies {USD,BTC,ETH}
    enum Sorting {gecko_desc, gecko_asc, market_cap_asc, market_cap_desc, volume_asc, volume_desc}


    //will use public api coinGecko, no api key
    String baseUrl = "https://api.coingecko.com/api/v3/";

    //endpoints
    String markets = "coins/markets?vs_currency="; //for getting list of all crypto data
    //params
    String currency; //assign one of Currencies enumerated values, required
    String order = "&order="; //assign one of Sorting enumerated values, optional
    String per_page = "&per_page=250"; //up to 250 coins can be returned


    APICallouts(){}
    OkHttpClient client = new OkHttpClient();

    String getMarketData(String currency) throws IOException {
        String url = baseUrl + markets + currency;
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }

    }

}
