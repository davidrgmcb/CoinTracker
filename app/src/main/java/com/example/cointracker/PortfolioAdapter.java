package com.example.cointracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.PortfolioViewHolder>{

//public class AllCryptoAdapter extends RecyclerView.Adapter<AllCryptoAdapter.CryptoViewHolder> {


    class PortfolioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final PortfolioAdapter mAdapter;
        TextView cryptoName;
        TextView ticker;
        TextView currentPrice;
        TextView percentageChange;
        TextView rank;
        TextView ath;
        TextView athChange;


        public PortfolioViewHolder(View itemView, PortfolioAdapter adapter){
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
            double idClicked = cryptoList.get(mPosition).market_cap_rank; //[mPosition].market_cap_rank; //get(mPosition);

            Intent i = new Intent(v.getContext(), CryptoDetail.class);
            i.putExtra("rank", idClicked);
            v.getContext().startActivity(i);


        }
    }


    //dataset to be displayed
    //private CryptoDataPoints[] cryptoList;
    private List<CryptoDataPoints> cryptoList = new ArrayList<>();
    private LayoutInflater mInflator;

    public PortfolioAdapter(Context context, List<CryptoDataPoints> cryptoList){
        mInflator = LayoutInflater.from(context);
        this.cryptoList = cryptoList;
    }

    @NonNull
    @Override
    public PortfolioAdapter.PortfolioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflator.inflate(R.layout.cryptolistitem,parent,false);
        return new PortfolioViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PortfolioAdapter.PortfolioViewHolder holder, int position) {
        CryptoDataPoints currentItem = cryptoList.get(position);//[position];
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
        return cryptoList.size();//length; //was length when array, but changed to list
    }
}



