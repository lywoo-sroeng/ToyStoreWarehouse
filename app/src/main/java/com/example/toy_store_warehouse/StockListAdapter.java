package com.example.toy_store_warehouse;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.ViewHolder> {
    ArrayList<Toy> toyArraysList;
    OnItemListener onItemListener;

    Context context;

    public StockListAdapter(ArrayList<Toy> toyArraysList, OnItemListener onItemListener) {
        this.toyArraysList = toyArraysList;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.toy_list_item, parent, false);
        return new ViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Resources res = context.getResources();

        Toy toy = toyArraysList.get(position);
        holder.txtName.setText(toy.getName());
        holder.txtQty.setText(res.getString(R.string.d_qty, toy.getQty()));
        holder.txtPrice.setText(res.getString(R.string.d_price, toy.getPrice()));
        Picasso.get().load(toy.getImgUri()).into(holder.imgViewToy);
    }

    @Override
    public int getItemCount() {
        return toyArraysList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtName;
        TextView txtQty;
        TextView txtPrice;
        ImageView imgViewToy;
        OnItemListener onItemListener;

        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            imgViewToy = itemView.findViewById(R.id.img_view_toy);
            txtQty = itemView.findViewById(R.id.txt_qty);
            txtName = itemView.findViewById(R.id.txt_name);
            txtPrice = itemView.findViewById(R.id.txt_price);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onItemListener.onItemClick(toyArraysList.get(position));
        }
    }

    public interface OnItemListener {
        void onItemClick(Toy toy);
    }
}
