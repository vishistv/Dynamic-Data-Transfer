package com.example.vishistvarugeese.dynamic_data_transfer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Vishist Varugeese on 28-03-2018.
 */

public class RecyclerViewDataUserAdapter extends RecyclerView.Adapter<RecyclerViewDataUserAdapter.ViewHolder> {

    private List<ListItem_RecyclerView_Data_User> listItems;
    private Context context;

    public RecyclerViewDataUserAdapter(List<ListItem_RecyclerView_Data_User> listItems, Context context) {

        this.listItems = listItems;
        this.context = context;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewDataUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_items_data_users, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewDataUserAdapter.ViewHolder holder, int position) {
        ListItem_RecyclerView_Data_User listItem = listItems.get(position);

        holder.txtTitle.setText(listItem.getTitleUsers());
        holder.txtValue.setText(listItem.getValueUsers());

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

            txtTitle = (TextView) itemView.findViewById(R.id.titleUsers);
            txtValue = (TextView) itemView.findViewById(R.id.valueUsers);
        }
    }
}