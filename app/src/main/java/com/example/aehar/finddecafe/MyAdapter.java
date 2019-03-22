package com.example.aehar.finddecafe;

/**
 * Created by aehar on 3/21/2019.
 */
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.CardView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private List<ListItem> listItems;
    private Context context;

    public MyAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        ListItem listItem = listItems.get(i);
        viewHolder.tvName.setText(listItem.getName());
        viewHolder.tvVicinity.setText(listItem.getVicinity());
        viewHolder.tvRating.setText(listItem.getRating()+"");
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvName;
        public TextView tvVicinity;
        public TextView tvRating;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvVicinity = (TextView) itemView.findViewById(R.id.tvVicinity);
            tvRating = (TextView) itemView.findViewById(R.id.tvRating);
        }
    }

}
