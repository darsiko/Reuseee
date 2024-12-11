package com.example.reuse.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.reuse.R;
import com.example.reuse.adapter.ProductAdapter;
import com.example.reuse.models.Product;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends Fragment implements ProductAdapter.OnItemClickListener  {
    private RecyclerView recyclerViewrecents;
    private RecyclerView recyclerViewlastAdded;
    private ProductAdapter adapter;
    private List<Product> productList;
    public HomeScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home_screen, container, false);

        // Initialize RecyclerViews after the view is inflated
        recyclerViewrecents = rootView.findViewById(R.id.horizontal_recycler_view_recents);
        recyclerViewlastAdded = rootView.findViewById(R.id.horizontal_recycler_view_lastadded);

        // Set LayoutManager for both RecyclerViews
        recyclerViewrecents.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewlastAdded.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Initialize product list with sample data
        productList = new ArrayList<>();
        productList.add(new Product(R.drawable.shoes, "Sneakers Verdi", "$100", "Melissa Peters", "5 products online", R.drawable.user, List.of("costoso", "nuovo", "usabile")));
        productList.add(new Product(R.drawable.shoes, "Sneakers Neri", "$120", "John Doe", "3 products online", R.drawable.user, List.of("costoso", "nuovo", "usabile")));   // Add more products as needed...

        // Set the adapter for both RecyclerViews
        adapter = new ProductAdapter(getContext(), productList, this);
        recyclerViewlastAdded.setAdapter(adapter);
        recyclerViewrecents.setAdapter(adapter);

        return rootView;  // Return the view you inflated
    }

    @Override
    public void onItemClick(Product product) {
        // Handle the product click here
        // For example, navigate to the product details page or display a Toast
        Toast.makeText(getContext(), "Clicked on: " + product.getName(), Toast.LENGTH_SHORT).show();

        // You can now pass the product data to a new fragment or activity, e.g.
        Bundle bundle = new Bundle();
        bundle.putString("product_name", product.getName());
        bundle.putString("product_price", product.getPrice());
        bundle.putInt("product_image", product.getImageResId()); // For ImageView
        // Add other product details to the bundle

        DetailProdScreen productDetailFragment = new DetailProdScreen();
        productDetailFragment.setArguments(bundle);

        // Perform the fragment transaction
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, productDetailFragment);
        transaction.addToBackStack(null);  // Add to back stack if you want to go back to this fragment
        transaction.commit();
    }
}
