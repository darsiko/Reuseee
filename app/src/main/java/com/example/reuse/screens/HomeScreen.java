package com.example.reuse.screens;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.renderscript.Sampler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.reuse.R;
import com.example.reuse.adapter.ProductAdapter;
import com.example.reuse.models.Product;
import com.example.reuse.models.Tutorial;
import com.example.reuse.models.User;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeScreen extends Fragment implements ProductAdapter.OnItemClickListener  {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;
    private DatabaseReference databaseReference;

    public HomeScreen() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);

        // Initialize RecyclerViews after the view is inflated
        recyclerView = view.findViewById(R.id.productList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        // Initialize product list with sample data
        productList = new ArrayList<>();

        loadProducts();

        adapter = new ProductAdapter(getContext(), productList, this);
        recyclerView.setAdapter(adapter);

        //animazione pop up filtri
        ImageButton menuButton = view.findViewById(R.id.menuButton);
        LinearLayout filterContainer = view.findViewById(R.id.filterContainer);
        filterContainer.post(new Runnable() {
            @Override
            public void run() {
                float filterContainerHeight = filterContainer.getHeight();
                filterContainer.setVisibility(View.GONE);
                menuButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(filterContainer.getVisibility() == View.GONE){
                            filterContainer.setVisibility(View.VISIBLE);
                            filterContainer.setTranslationY(-filterContainerHeight);
                            ObjectAnimator animation = ObjectAnimator.ofFloat(filterContainer,
                                    "translationY",
                                    -filterContainerHeight,
                                    0);
                            ValueAnimator va = ValueAnimator.ofInt(0, (int)filterContainerHeight);
                            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(@NonNull ValueAnimator animated) {
                                    int value = (int) animated.getAnimatedValue();
                                    ViewGroup.LayoutParams layoutParams = filterContainer.getLayoutParams();
                                    layoutParams.height = value;
                                    filterContainer.setLayoutParams(layoutParams);
                                }
                            });
                            animation.setDuration(1000);
                            va.setDuration(1000);
                            animation.start();
                            va.start();
                        }
                        else{
                            ObjectAnimator animation = ObjectAnimator.ofFloat(filterContainer,
                                    "translationY",
                                    0,
                                    -filterContainerHeight);
                            ValueAnimator va = ValueAnimator.ofInt((int)filterContainerHeight, 0);
                            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(@NonNull ValueAnimator animated) {
                                    int value = (int) animated.getAnimatedValue();
                                    ViewGroup.LayoutParams layoutParams = filterContainer.getLayoutParams();
                                    layoutParams.height = value;
                                    filterContainer.setLayoutParams(layoutParams);
                                }
                            });
                            animation.setDuration(1000);
                            va.setDuration(1000);
                            animation.start();
                            va.start();
                            animation.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    filterContainer.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                });
            }
        });

        //animazione barButton e setting dell'attività del bottone (nella ricerca basterà controllare il parametro boolean barButtonActive
        Button barButton = view.findViewById(R.id.barattoButton);
        barButton.setBackgroundColor(Color.RED);
        final boolean[] barButtonActive = new boolean[1];
        barButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDark = (getContext().getResources().getConfiguration().uiMode
                        & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
                if(!barButtonActive[0]){
                    if(isDark) barButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary));
                    else barButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.dark));
                    barButtonActive[0] = true;
                }
                else{
                    barButton.setBackgroundColor(Color.RED);
                    barButtonActive[0] = false;
                }
            }
        });

        //barra del range di prezzo
        EditText min = view.findViewById(R.id.minPriceSetter);
        EditText max = view.findViewById(R.id.maxPriceSetter);
        RangeSlider rangePrezzi = view.findViewById(R.id.rangePrezziSlider);
        final boolean[] isUpdating = {false};
        final float[] minVal = new float[1];
        final float[] maxVal = new float[1];
        final float[] minAbs = new float[1];
        final float[] maxAbs = new float[1];
        minVal[0] = rangePrezzi.getValues().get(0);
        maxVal[0] = rangePrezzi.getValues().get(1);
        minAbs[0] = rangePrezzi.getValues().get(0);
        maxAbs[0] = rangePrezzi.getValues().get(1);
        rangePrezzi.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                if(!isUpdating[0]){
                    isUpdating[0] = true;
                    minVal[0] = slider.getValues().get(0);
                    maxVal[0] = slider.getValues().get(1);
                    min.setText(String.valueOf((int) minVal[0]));
                    max.setText(String.valueOf((int) maxVal[0]));
                    isUpdating[0] = false;
                }
            }
        });
        min.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(!isUpdating[0]){
                    String text = s.toString();
                    if(!text.isEmpty()){
                        try{
                            isUpdating[0] = true;
                            minVal[0] = Float.parseFloat(text);
                            if(minVal[0]>=rangePrezzi.getValueFrom()){
                                if(minVal[0]<=rangePrezzi.getValues().get(1)) rangePrezzi.setValues(minVal[0], rangePrezzi.getValues().get(1));
                                else rangePrezzi.setValues(rangePrezzi.getValues().get(1), rangePrezzi.getValues().get(1));
                            }
                            else rangePrezzi.setValues(rangePrezzi.getValueFrom(), rangePrezzi.getValues().get(1));
                        } catch (NumberFormatException e){
                            e.printStackTrace();
                        } finally {
                            isUpdating[0] = false;
                        }
                    }
                    else{
                        minVal[0] = minAbs[0];
                        rangePrezzi.setValues(minVal[0], rangePrezzi.getValues().get(1));
                    }
                }
            }
        });
        max.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(!isUpdating[0]){
                    String text = s.toString();
                    if(!text.isEmpty()){
                        try{
                            isUpdating[0] = true;
                            maxVal[0] = Float.parseFloat(text);
                            if(maxVal[0]<=rangePrezzi.getValueTo()){
                                if(maxVal[0]>=rangePrezzi.getValues().get(0)) rangePrezzi.setValues(rangePrezzi.getValues().get(0), maxVal[0]);
                                else rangePrezzi.setValues(rangePrezzi.getValues().get(0), rangePrezzi.getValues().get(0));
                            }
                            else rangePrezzi.setValues(rangePrezzi.getValues().get(0), rangePrezzi.getValueTo());
                        } catch (NumberFormatException e){
                            e.printStackTrace();
                        } finally {
                            isUpdating[0] = false;
                        }
                    }
                    else{
                        maxVal[0] = maxAbs[0];
                        rangePrezzi.setValues(rangePrezzi.getValues().get(0), maxVal[0]);
                    }
                }
            }
        });
        EditText searchInput = view.findViewById(R.id.editTextText);
        final String[] search = new String[1];
        search[0] = "";
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                search[0]=s.toString();
            }
        });

        //Ricerca
        Button searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchProducts(search[0], barButtonActive[0], minVal[0], maxVal[0]);
            }
        });

        return view;  // Return the view you inflated
    }

    @Override
    public void onResume(){
        super.onResume();
        loadProducts();
    }
    private void loadProducts(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Product product = snapshot.getValue(Product.class);
                    if(product!=null) productList.add(product);
                }
                adapter.updateList(productList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ProductBoughtScreen", "Error fetching data: " + error.getMessage());
                Toast.makeText(getContext(), "Failed to load products.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void searchProducts(String query, boolean baratto, float min, float max) {
        List<Product> filteredProducts = new ArrayList<>();
        if(query.isEmpty()){
            if(baratto){
                for(Product prod : productList){
                    if(prod.isBaratto() && prod.getPrezzo()>=min && prod.getPrezzo()<=max){
                        filteredProducts.add(prod);
                    }
                }
            }
            else{
                for (Product prod : productList) {
                    if (prod.getPrezzo()>=min && prod.getPrezzo()<=max) {
                        filteredProducts.add(prod);
                    }
                }
            }
            adapter.updateList(filteredProducts);
        }
        else{
            if(baratto){
                for(Product prod : productList){
                    if(prod.getNome().toLowerCase().contains(query.toLowerCase()) && prod.isBaratto() && prod.getPrezzo()>=min && prod.getPrezzo()<=max){
                        filteredProducts.add(prod);
                    }
                }
            }
            else{
                for (Product prod : productList) {
                    if (prod.getNome().toLowerCase().contains(query.toLowerCase()) && prod.getPrezzo()>=min && prod.getPrezzo()<=max) {
                        filteredProducts.add(prod);
                    }
                }
            }
            adapter.updateList(filteredProducts);
        }
    }
    @Override
    public void onItemClick(Product product) {
        Bundle bundle = new Bundle();

        // Fetch user details asynchronously
        new User(product.getIdVenditore(), new User.UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                // Populate the bundle with user and product details
                bundle.putString("prodId", product.getId());
                bundle.putString("sellerId", product.getIdVenditore());
                bundle.putBoolean("baratto", product.isBaratto());
                bundle.putString("venditore", user.getUsername());
                bundle.putString("status", user.getProductsForSale().size() + " prodotti online");
                bundle.putString("nome", product.getNome());
                bundle.putString("descrizione", product.getDescrizione());
                bundle.putString("prezzo", String.valueOf(product.getPrezzo()));
                bundle.putString("idOrdine", product.getIdOrdine());
// Fetch user profile image URL
                String userProfileImageUrl = user.getImageUrl();
                bundle.putString("userProfileImage", userProfileImageUrl);

                // Get the image URL from Firebase Storage
                StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(product.getImageUrl());
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {

                    String imageUrl = uri.toString(); // This is the HTTP URL
                    System.out.println("Download URL: " + imageUrl);
                    bundle.putString("imageProd", imageUrl);

                    // Navigate to the detail fragment with the populated bundle
                    navigateToDetailFragment(bundle);
                }).addOnFailureListener(e -> {
                    System.out.println("Failed to get download URL: " + e.getMessage());
                    e.printStackTrace();

                    // Fallback in case of failure to get the image URL
                    bundle.putString("imageProd", "default_placeholder_url");
                    navigateToDetailFragment(bundle);
                });
            }

            @Override
            public void onError(Exception e) {
                // Handle any errors during user data loading
                System.out.println("Error loading user data: " + e.getMessage());
                e.printStackTrace();
            }
        });

    }
    // Navigate to the detail fragment
    private void navigateToDetailFragment(Bundle bundle) {
        DetailProdScreen productDetailFragment = new DetailProdScreen();
        productDetailFragment.setArguments(bundle);

        // Perform the fragment transaction
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, productDetailFragment);
        transaction.addToBackStack(null); // Add to back stack if you want to go back to this fragment
        transaction.commit();
    }

}
