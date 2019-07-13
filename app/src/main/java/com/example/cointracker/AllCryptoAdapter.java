package com.example.cointracker;

import android.content.Intent;
import android.os.Bundle;
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
        public final TextView cryptoName;
        TextView cryptoPrice;
        TextView percentageChange;
        TextView rank;
        TextView ath;


        public CryptoViewHolder(View itemView, AllCryptoAdapter adapter){
            super(itemView);
            cryptoName = itemView.findViewById(R.id.cryptoName);
            cryptoPrice = itemView.findViewById(R.id.cryptoPrice);
            percentageChange = itemView.findViewById(R.id.percentageChange);
            ath = itemView.findViewById(R.id.allTimeHigh);
            rank = itemView.findViewById(R.id.rank);
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
            System.out.println("****************"+idClicked);

            // Change the word in the mWordList.
            //cryptoList[mPosition].name = "Clicked + " + idClicked; //set(mPosition, "Clicked! " + element);
            // Notify the adapter, that the data has changed so it can
            // update the RecyclerView to display the data.
            //mAdapter.notifyDataSetChanged();


                // first parameter is the context, second is the class of the activity to launch
               // Intent i = new Intent(context, CryptoDetail.class);
                // put "extras" into the bundle for access in the second activity
                //i.putExtra("username", "foobar");
                //i.putExtra("in_reply_to", "george");
                //i.putExtra("code", 400);
                // brings up the second activity
                //startActivity(i);


            Intent i = new Intent(v.getContext(), CryptoDetail.class);
            i.putExtra("id", idClicked);
            v.getContext().startActivity(i);

            //CryptoDetail.start(this, idClicked);
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
        holder.cryptoName.setText(currentItem.name);//mCurrent);
        holder.cryptoPrice.setText("$"+currentItem.current_price);
        holder.percentageChange.setText(currentItem.price_change_percentage_24h + "%");
        holder.rank.setText("#"+currentItem.market_cap_rank);
        holder.ath.setText("$"+ currentItem.ath);

    }

    @Override
    public int getItemCount() {
        return cryptoList.length;
    }
}



