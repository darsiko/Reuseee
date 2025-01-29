package com.example.reuse.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reuse.R;
import com.example.reuse.adapter.MyProductsAdapter;
import com.example.reuse.models.Product;

import java.util.ArrayList;
import java.util.List;

public class MyProductsScreen extends Fragment implements MyProductsAdapter.OnItemClickListener {
    private RecyclerView recyclerViewMyProducts;
    private MyProductsAdapter adapter;
    private List<Product> productList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_products_screen, container, false);

        LinearLayout backtoProfileButton = view.findViewById(R.id.backToOggettiAcquistati);
        recyclerViewMyProducts = view.findViewById(R.id.my_products);
        recyclerViewMyProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Initialize product list and add the received product
        productList = new ArrayList<>();

        //Retrieve the purchased product from the Bundle
        Bundle args = getArguments();
        if(args!=null){
            Product purchasedProduct = args.getParcelable("purchased_product");
            if(purchasedProduct!=null){
                productList.add(purchasedProduct);
            }
        }

        //Set the adapter with the updated list
        adapter = new MyProductsAdapter(getContext(), productList, this);
        recyclerViewMyProducts.setAdapter(adapter);

        backtoProfileButton.setOnClickListener(view1 -> getParentFragmentManager().popBackStack());

        return view;
    }

    @Override
    public void onItemClick(Product product) {
        //Handle item click, if needed
    }
}