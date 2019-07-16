package com.example.cointracker;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.content.Context;
import android.support.annotation.NonNull;


public class AllCryptoAdapter extends RecyclerView.Adapter<AllCryptoAdapter.CryptoViewHolder> {


    class CryptoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final AllCryptoAdapter mAdapter;
        TextView cryptoName;
        TextView ticker;
        TextView currentPrice;
        TextView percentageChange;
        TextView rank;
        TextView ath;
        TextView athChange;


        public CryptoViewHolder(View itemView, AllCryptoAdapter adapter){
            super(itemView);
            cryptoName = itemView.findViewById(R.id.cryptoName);
            currentPrice = itemView.findViewById(R.id.currentPrice);
            percentageChange = itemView.findViewById(R.id.percentageChange);
            ath = itemView.findViewById(R.id.allTimeHigh);
            rank = itemView.findViewById(R.id.rank);
            athChange = itemView.findViewById(R.id.ath_change);
            ticker = itemView.findViewById(R.id.symbol);

            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Context context = v.getContext();

            // Get the position of the item that was clicked.
            int mPosition = getLayoutPosition();
            // Use that to access the affected item in cryptoList.
            double idClicked = cryptoList[mPosition].market_cap_rank; //get(mPosition);

            Intent i = new Intent(v.getContext(), CryptoDetail.class);
            i.putExtra("rank", idClicked);
            v.getContext().startActivity(i);

        }
    }


    //dataset to be displayed
    private CryptoDataPoints[] cryptoList;
    private LayoutInflater mInflator;

    public AllCryptoAdapter(Context context, CryptoDataPoints[] cryptoList){
        mInflator = LayoutInflater.from(context);
        this.cryptoList = cryptoList;
    }

    @NonNull
    @Override
    public AllCryptoAdapter.CryptoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflator.inflate(R.layout.cryptolistitem,parent,false);
        return new CryptoViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull AllCryptoAdapter.CryptoViewHolder holder, int position) {
        CryptoDataPoints currentItem = cryptoList[position];
        holder.cryptoName.setText(currentItem.name);
        holder.ticker.setText(currentItem.symbol);
        holder.rank.setText((int)currentItem.market_cap_rank + ".");

        holder.currentPrice.setText("$"+currentItem.current_price);
        holder.percentageChange.setText(String.format("%.2f", currentItem.price_change_percentage_24h) + "%");
        //conditional formatting of price/ percentage change
        if (currentItem.price_change_percentage_24h < -1) {
            holder.currentPrice.setTextColor(Color.parseColor("#b71c1c"));
            holder.percentageChange.setTextColor(Color.parseColor("#b71c1c"));
        }else if (currentItem.price_change_percentage_24h > 1){
            holder.currentPrice.setTextColor(Color.parseColor("#2e7d32"));
            holder.percentageChange.setTextColor(Color.parseColor("#2e7d32"));
        }

        holder.ath.setText("$"+ currentItem.ath);
        holder.athChange.setText(String.format("%.2f", currentItem.ath_change_percentage) + "%");

    }

    @Override
    public int getItemCount() {
        return cryptoList.length;
    }
}



