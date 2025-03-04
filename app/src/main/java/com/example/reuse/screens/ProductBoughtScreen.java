package com.example.reuse.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductBoughtScreen extends Fragment implements ProductBroughtAdapter.OnItemClickListener {
    private RecyclerView recyclerViewBrought;
    private ProductBroughtAdapter adapter;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_bought_screen, container, false);

        LinearLayout backtoProfileButton = view.findViewById(R.id.backToOggettiInVendita);
        LinearLayout addProd = view.findViewById(R.id.addProduct);
        auth= FirebaseAuth.getInstance();
        addProd.setOnClickListener(v -> {
            AddProductScreen addProductScreen = new AddProductScreen();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, addProductScreen);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        backtoProfileButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        recyclerViewBrought = view.findViewById(R.id.listBrought);
        recyclerViewBrought.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Initialize product list
        List<Product> productList = new ArrayList<>();
        adapter = new ProductBroughtAdapter(getContext(), productList, this);
        recyclerViewBrought.setAdapter(adapter);

        // Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");

        // Fetch products from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear(); // Clear old data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    if (product != null && product.getIdVenditore().equals(auth.getUid())) {
                        product.setId(snapshot.getKey());
                        productList.add(product);
                    }
                }
                adapter.notifyDataSetChanged(); // Notify adapter about the data change
                // Log.d("ProductBoughtScreen", "Products fetched: " + productList.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ProductBoughtScreen", "Error fetching data: " + databaseError.getMessage());
                Toast.makeText(getContext(), "Failed to load products.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onEditProductClick(Product product) {
        EditProductScreen editProductScreen = new EditProductScreen();
        Bundle bundle = new Bundle();
        bundle.putString("id", product.getId());
        bundle.putString("imageUrl", product.getImageUrl());
        bundle.putString("descrizione", product.getDescrizione());
        bundle.putString("name", product.getNome());
        bundle.putString("prezzo",""+ product.getPrezzo());
        bundle.putBoolean("barattabile", product.isBaratto());
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

    @Override
    public void onDeleteProductClick(Product product){
        String a=product.getId();
        System.out.println("ohno!"+a);
        if(product.getId()=="" || product.getId()==null){
            Toast.makeText(getContext(), "Failed to find product", Toast.LENGTH_SHORT).show();
        }else{
            //RemoveProduct
            DatabaseReference productRef=FirebaseDatabase
                    .getInstance()
                    .getReference("Products")
                    .child(product.getId());
            productRef.removeValue();

            //updateUserlistProduct
            String idVenditore = product.getIdVenditore();
            String productId = product.getId();
            /*
            DatabaseReference userProductRef=FirebaseDatabase
                    .getInstance()
                    .getReference("Users")
                    .child(idVenditore)
                    .child("productsForSale")
                    .child(productId);

             */
            DatabaseReference userRef = FirebaseDatabase
                    .getInstance()
                    .getReference("Users")
                    .child(idVenditore);

            userRef.get().addOnCompleteListener(task -> {

                Map<String, Object> updates = new HashMap<>();
                //gather the updated product list
                List<String> myUserProdList = new ArrayList<>();

                for(DataSnapshot d : task.getResult().child("productsForSale").getChildren()){
                    String itemId = d.getValue(String.class);
                    if(itemId!=null && !itemId.equals(productId)){
                        myUserProdList.add(itemId);
                    }
                }


                updates.put("productsForSale", myUserProdList);
                userRef.updateChildren(updates);
            });
        }
    }
}