package com.example.reuse.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.reuse.R;
import com.example.reuse.adapter.MyProductsAdapter;
import com.example.reuse.adapter.ProductAdapter;
import com.example.reuse.adapter.ProductBroughtAdapter;
import com.example.reuse.models.Product;

import java.util.ArrayList;
import java.util.List;


public class MyProductsScreen extends Fragment implements MyProductsAdapter.OnItemClickListener {
    private RecyclerView recyclerViewMyProducts;
    private MyProductsAdapter adapter;
    private List<Product> productList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_products_screen, container, false);

        LinearLayout backtoProfileButton = view.findViewById(R.id.backToOggettiAcquistati);

        backtoProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });
        recyclerViewMyProducts = view.findViewById(R.id.my_products);


        // Set LayoutManager for both RecyclerViews

        recyclerViewMyProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Initialize product list with sample data
        productList = new ArrayList<>();
        productList.add(new Product(R.drawable.shoes, "Sneakers Verdi", "$100", "Melissa Peters", "5 products online", R.drawable.user, List.of("costoso", "nuovo", "usabile")));
        productList.add(new Product(R.drawable.shoes, "Sneakers Neri", "$120", "John Doe", "3 products online", R.drawable.user, List.of("costoso", "nuovo", "usabile")));
        // Add more products as needed...

        // Set the adapter for both RecyclerViews
        adapter = new MyProductsAdapter(getContext(), productList, this);
        recyclerViewMyProducts.setAdapter(adapter);

        // Inflate the layout for this fragment
       return view;
    }

    @Override
    public void onItemClick(Product product) {

    }
}