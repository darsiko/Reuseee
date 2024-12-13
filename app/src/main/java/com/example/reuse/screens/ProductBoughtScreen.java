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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.reuse.R;
import com.example.reuse.adapter.ProductAdapter;
import com.example.reuse.adapter.ProductBroughtAdapter;
import com.example.reuse.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductBoughtScreen extends Fragment implements ProductBroughtAdapter.OnItemClickListener {
    private RecyclerView recyclerViewBrought;
    private ProductBroughtAdapter adapter;
    private List<Product> productList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_bought_screen, container, false);

        LinearLayout backtoProfileButton = view.findViewById(R.id.backToOggettiInVendita);
        LinearLayout addProd = view.findViewById(R.id.addProduct);

        addProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProductScreen addProductScreen = new AddProductScreen();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, addProductScreen);
                transaction.addToBackStack(null);  // Add to back stack if you want to go back to this fragment
                transaction.commit();
            }
        });

        backtoProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });
        recyclerViewBrought = view.findViewById(R.id.listBrought);

        // Set LayoutManager for both RecyclerViews

        recyclerViewBrought.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Initialize product list with sample data
        productList = new ArrayList<>();
        //rimosso productList.add(new Product(R.drawable.shoes, "Sneakers Verdi", "$100", "Melissa Peters", "5 products online", R.drawable.user, List.of("costoso", "nuovo", "usabile")));
        //rimosso productList.add(new Product(R.drawable.shoes, "Sneakers Neri", "$120", "John Doe", "3 products online", R.drawable.user, List.of("costoso", "nuovo", "usabile")));
        // Add more products as needed...

        // Set the adapter for both RecyclerViews
        adapter = new ProductBroughtAdapter(getContext(), productList, this);
        recyclerViewBrought.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;
    }



    @Override
    public void onEditProductClick(Product product) {
        EditProductScreen editProductScreen = new EditProductScreen();
        Bundle bundle = new Bundle();
        bundle.putString("name", product.getNome());
        //rimosso bundle.putString("price", product.getPrezzo());
        //rimosso bundle.putInt("imageResId", product.getImageResId());
        //rimosso bundle.putInt("userAvatarResId", product.getUserAvatarResId());
        //rimosso bundle.putStringArrayList("tags", new ArrayList<>(product.getTags()));
        editProductScreen.setArguments(bundle);


        // You can now pass the product data to a new fragment or activity, e.g.
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, editProductScreen);
        transaction.addToBackStack(null);
        transaction.commit();  // Add to back stack if you want to go back to this fragment
    }
}