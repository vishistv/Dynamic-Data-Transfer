package com.example.vishistvarugeese.dynamic_data_transfer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Vishist Varugeese on 27-03-2018.
 */

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.ViewHolder> {

    private List<ListItem_RecyclerView_Data> listItems;
    private Context context;

    public RecyclerViewDataAdapter(List<ListItem_RecyclerView_Data> listItems, Context context) {

        this.listItems = listItems;
        this.context = context;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_items_data, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewDataAdapter.ViewHolder holder, int position) {
        ListItem_RecyclerView_Data listItem = listItems.get(position);

        holder.txtTitle.setText(listItem.getTitle());
        holder.txtValue.setText(listItem.getValue());

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtTitle;
        private TextView txtValue;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.title);
            txtValue = (TextView) itemView.findViewById(R.id.value);
        }
    }
}
