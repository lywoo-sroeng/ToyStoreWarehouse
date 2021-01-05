package com.example.toy_store_warehouse;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class InStockFragment extends Fragment implements StockListAdapter.OnItemListener {
    ArrayList<Toy> figures = new ArrayList<>();
    ArrayList<Toy> dolls = new ArrayList<>();

    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in_stock, container, false);

        initRecycleListView(view, R.id.figure_in_stock_list, figures, "Figure");

        initRecycleListView(view, R.id.doll_in_stock_list, dolls, "Doll");

        return view;
    }

    private void initRecycleListView(View view, int recycleViewId, ArrayList<Toy> toys, String category) {
        RecyclerView recycleView = view.findViewById(recycleViewId);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        recycleView.setLayoutManager(layoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference(category);
        Query query = databaseReference.orderByChild("qty").startAt(1);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                toys.clear();
                for (DataSnapshot sn : snapshot.getChildren()) {
                    if (Toy.isFigure(category)) {
                        toys.add(sn.getValue(Figure.class));
                    } else if (Toy.isDoll(category)) {
                        toys.add(sn.getValue(Doll.class));
                    }
                }
                Collections.reverse(toys);
                StockListAdapter stockListAdapter = new StockListAdapter(toys, InStockFragment.this);
                recycleView.setAdapter(stockListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onItemClick(Toy toy) {
        Intent detailIntent = new Intent(this.getContext(), ToyDetailActivity.class);
        detailIntent.putExtra("toy", toy);
        startActivity(detailIntent);
    }
}