package com.example.cointracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

/** Big thanks to StackOverflow https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
 * for this construction pattern I've used to help learn recyclerview.
 */
public class CoinRecyclerViewAdapter extends RecyclerView.Adapter<CoinRecyclerViewAdapter.ViewHolder> {

    private List<String> mData; //Pass in formatted strings to the recycler for ease.
    private LayoutInflater mInflater;
    private AdapterView.OnItemClickListener mClickListener;

    CoinRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.all_coins_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String currentCoin = mData.get(position);
        holder.currentText.setText(currentCoin);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView currentText;

        ViewHolder(View itemView) {
            super(itemView);
            currentText = itemView.findViewById(R.id.tv_allCrypto);
            itemView.setOnClickListener(this);
        }
        //Click methods not yet implemented
        @Override
        public void onClick(View view) {
            //if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    String getItem(int id) {
        return mData.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        //this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
