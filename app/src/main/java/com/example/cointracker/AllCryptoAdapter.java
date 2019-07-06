package com.example.cointracker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import com.example.android.common.logger.Log;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;

public class AllCryptoAdapter extends RecyclerView.Adapter<AllCryptoAdapter.CryptoViewHolder> {

    class CryptoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView cryptoItemView;
        final AllCryptoAdapter mAdapter;

        public CryptoViewHolder(View itemView, AllCryptoAdapter adapter){
            super(itemView);
            cryptoItemView = itemView.findViewById(R.id.cryptoItem);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Get the position of the item that was clicked.
            int mPosition = getLayoutPosition();
            // Use that to access the affected item in cryptoList.
            String element = cryptoList[mPosition].name; //get(mPosition);
            // Change the word in the mWordList.
            cryptoList[mPosition].name = "Clicked + " + element; //set(mPosition, "Clicked! " + element);
            // Notify the adapter, that the data has changed so it can
            // update the RecyclerView to display the data.
            mAdapter.notifyDataSetChanged();
        }
    }

    //private final LinkedList<String> mWordList;
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
        String mCurrent = cryptoList[position].name; //get(position);
        holder.cryptoItemView.setText(mCurrent);

    }

    @Override
    public int getItemCount() {
        return cryptoList.length;
    }
}




///////////////////////copied from RecyclerView sampler app///////////////////////
/***************************************************************************

  //Provide views to RecyclerView with data from mDataSet.

public class AllCryptoAdapter extends RecyclerView.Adapter<AllCryptoAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    private String[] mDataSet;

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)

     // Provide a reference to the type of views that you are using (custom ViewHolder)

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //private final TextView textView;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            //textView = (TextView) v.findViewById(R.id.textView);
        }

        //public TextView getTextView() {
          //  return textView;
        //}
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)
*********************************************************************/
    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
/***************************************************************************

 public AllCryptoAdapter(String[] dataSet) {
        mDataSet = dataSet;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        //Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getTextView().setText(mDataSet[position]);
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}

*********************************************************************/