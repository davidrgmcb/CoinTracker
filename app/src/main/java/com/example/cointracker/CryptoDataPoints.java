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


        /* example json payload:
            [
              {
                "id": "bitcoin",
                "symbol": "btc",
                "name": "Bitcoin",
                "image": "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579",
                "current_price": 8122,
                "market_cap": 144209094606,
                "market_cap_rank": 1,
                "total_volume": 21081040424,
                "high_24h": 8182,
                "low_24h": 7916,
                "price_change_24h": 149.905,
                "price_change_percentage_24h": 1.88045,
                "market_cap_change_24h": 2658576577,
                "market_cap_change_percentage_24h": 1.87818,
                "circulating_supply": 17755975,
                "total_supply": 21000000,
                "ath": 19665,
                "ath_change_percentage": -58.67208,
                "ath_date": "2017-12-16T00:00:00.000Z",
                "roi": null,
                "last_updated": "2019-06-13T05:08:39.076Z"
              },
              {
                "id": "ethereum",
                "symbol": "eth",
                "name": "Ethereum",
                "image": "https://assets.coingecko.com/coins/images/279/large/ethereum.png?1547034048",
                "current_price": 259.486,
                "market_cap": 27627049449,
                "market_cap_rank": 2,
                "total_volume": 9273056063,
                "high_24h": 262.815,
                "low_24h": 246.237,
                "price_change_24h": 11.310269,
                "price_change_percentage_24h": 4.55736,
                "market_cap_change_24h": 1204130956,
                "market_cap_change_percentage_24h": 4.55715,
                "circulating_supply": 106468129.8741,
                "total_supply": null,
                "ath": 1448,
                "ath_change_percentage": -82.05735,
                "ath_date": "2018-01-13T00:00:00.000Z",
                "roi": {
                  "times": 41.70589626806308,
                  "currency": "btc",
                  "percentage": 4170.589626806308
                },
                "last_updated": "2019-06-13T05:08:38.789Z"
              }
            ]
        */


    //coinGecko api is public, no key required
    String baseUrl = "https://api.coingecko.com/api/v3/";

    //endpoints
    String markets = "coins/markets?vs_currency="; //for getting list of all crypto data
    //params
    //String currency; //assign one of Currencies enumerated values, required param
    String order = "&order="; //assign one of Sorting enumerated values, optional param
    String per_page = "&per_page=250"; //up to 250 coins can be returned, optional param

    //using square OkHttp library for simple callouts
    OkHttpClient client = new OkHttpClient();

    String getCryptoData(String _currency) throws IOException {
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
