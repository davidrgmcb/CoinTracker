package com.example.cointracker;

public class APICalloutsMock {

    String response = "{\n" +
            "    \"status\": {\n" +
            "        \"timestamp\": \"2019-06-13T02:01:46.172Z\",\n" +
            "        \"error_code\": 0,\n" +
            "        \"error_message\": null,\n" +
            "        \"elapsed\": 6,\n" +
            "        \"credit_count\": 1\n" +
            "    },\n" +
            "    \"data\": [\n" +
            "        {\n" +
            "            \"id\": 1,\n" +
            "            \"name\": \"Bitcoin\",\n" +
            "            \"symbol\": \"BTC\",\n" +
            "            \"slug\": \"bitcoin\",\n" +
            "            \"circulating_supply\": 17755725,\n" +
            "            \"total_supply\": 17755725,\n" +
            "            \"max_supply\": 21000000,\n" +
            "            \"date_added\": \"2013-04-28T00:00:00.000Z\",\n" +
            "            \"num_market_pairs\": 7569,\n" +
            "            \"tags\": [\n" +
            "                \"mineable\"\n" +
            "            ],\n" +
            "            \"platform\": null,\n" +
            "            \"cmc_rank\": 1,\n" +
            "            \"last_updated\": \"2019-06-13T02:01:31.000Z\",\n" +
            "            \"quote\": {\n" +
            "                \"USD\": {\n" +
            "                    \"price\": 8138.15568171,\n" +
            "                    \"volume_24h\": 18470684997.4654,\n" +
            "                    \"percent_change_1h\": -0.322765,\n" +
            "                    \"percent_change_24h\": 2.77256,\n" +
            "                    \"percent_change_7d\": 3.88825,\n" +
            "                    \"market_cap\": 144498854291.63028,\n" +
            "                    \"last_updated\": \"2019-06-13T02:01:31.000Z\"\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    ]\n" +
            "}";
}
