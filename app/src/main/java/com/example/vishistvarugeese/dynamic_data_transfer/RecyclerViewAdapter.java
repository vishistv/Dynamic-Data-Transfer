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
 * Created by Vishist Varugeese on 19-12-2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<com.example.vishistvarugeese.dynamic_data_transfer.ListItem_RecyclerView_Admin> listItems;
    private Context context;

    public RecyclerViewAdapter(List<com.example.vishistvarugeese.dynamic_data_transfer.ListItem_RecyclerView_Admin> listitems, Context context) {
        this.listItems = listitems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        com.example.vishistvarugeese.dynamic_data_transfer.ListItem_RecyclerView_Admin listItem = listItems.get(position);

        holder.recentLoginName.setText(listItem.getRecentLoginName());
        holder.recentLoginCpf.setText(listItem.getRecentLoginCpf());
        holder.recentLoginType.setText(listItem.getRecentLoginType());


        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            date = df.parse(listItem.getRecentLoginTime());
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
            String loginTime = sdf.format(date);

            holder.recentLoginTime.setText(loginTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView recentLoginName;
        private TextView recentLoginTime;
        private TextView recentLoginCpf;
        private TextView recentLoginType;

        public ViewHolder(View itemView) {
            super(itemView);
            recentLoginName = (TextView) itemView.findViewById(R.id.recentLoginName);
            recentLoginTime = (TextView) itemView.findViewById(R.id.recentLoginTime);
            recentLoginCpf = (TextView) itemView.findViewById(R.id.recentLoginCpf);
            recentLoginType = (TextView) itemView.findViewById(R.id.recentLoginType);
        }
    }
}
