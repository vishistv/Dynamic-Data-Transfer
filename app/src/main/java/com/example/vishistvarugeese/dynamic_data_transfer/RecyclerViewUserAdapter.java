package com.example.vishistvarugeese.dynamic_data_transfer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Vishist Varugeese on 11-01-2018.
 */

public class RecyclerViewUserAdapter extends RecyclerView.Adapter<RecyclerViewUserAdapter.ViewHolder> {

    private List<com.example.vishistvarugeese.dynamic_data_transfer.ListItem_RecyclerView_User> listItems;
    private Context context;

    public RecyclerViewUserAdapter(List<com.example.vishistvarugeese.dynamic_data_transfer.ListItem_RecyclerView_User> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    public void updateDataList(List<com.example.vishistvarugeese.dynamic_data_transfer.ListItem_RecyclerView_User> newDatas) {
        listItems.clear();
        listItems.addAll(newDatas);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewUserAdapter.ViewHolder holder, int position) {
        com.example.vishistvarugeese.dynamic_data_transfer.ListItem_RecyclerView_User listItem = listItems.get(position);

        holder.txtUserName.setText(listItem.getTxtUserName());
        holder.txtUserCpf.setText(listItem.getTxtUserCpf());
        holder.txtAccountType.setText(listItem.getTxtAccountType());
        holder.txtPhNumber.setText(listItem.getTxtPhoneNumber());

        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            date = df.parse(listItem.getTxtCreatedDate());
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, EEEE, h:mm a");
            String loginTime = sdf.format(date);

            holder.txtCreatedDate.setText(loginTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtUserName;
        private TextView txtUserCpf;
        private TextView txtAccountType;
        private TextView txtPhNumber;
        private TextView txtCreatedDate;


        public ViewHolder(View itemView) {
            super(itemView);
            txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
            txtUserCpf = (TextView) itemView.findViewById(R.id.txtUserCpf);
            txtAccountType = (TextView) itemView.findViewById(R.id.txtAccountType);
            txtPhNumber = (TextView) itemView.findViewById(R.id.txtPhNumber);
            txtCreatedDate = (TextView) itemView.findViewById(R.id.txtCreatedDate);
        }
    }


}

