package com.example.reuse.screens;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reuse.R;
import com.example.reuse.adapter.ProductAdapter;
import com.example.reuse.models.Product;
import com.example.reuse.models.User;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends Fragment implements ProductAdapter.OnItemClickListener {

    private RecyclerView recyclerViewRecents, recyclerViewLastAdded;
    private ProductAdapter adapterRecents, adapterLastAdded;
    private List<Product> productListRecents, productListLastAdded;
    private DatabaseReference databaseReference;
    private boolean filterByBaratto = false;
    private boolean isUpdating = false;

    private EditText minPrice, maxPrice;
    private RangeSlider priceRangeSlider;
    private EditText editTextFilter;
    private Button barButton;

    public HomeScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_screen, container, false);

        // Initialize UI components
        recyclerViewRecents = rootView.findViewById(R.id.horizontal_recycler_view_recents);
        recyclerViewLastAdded = rootView.findViewById(R.id.horizontal_recycler_view_lastadded);
        editTextFilter = rootView.findViewById(R.id.editTextText);
        priceRangeSlider = rootView.findViewById(R.id.rangePrezziSlider);
        minPrice = rootView.findViewById(R.id.minPriceSetter);
        maxPrice = rootView.findViewById(R.id.maxPriceSetter);
        barButton = rootView.findViewById(R.id.barattoButton);
        ImageButton menuButton = rootView.findViewById(R.id.menuButton);
        LinearLayout filterContainer = rootView.findViewById(R.id.filterContainer);

        // RecyclerView setup
        recyclerViewRecents.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewLastAdded.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        productListRecents = new ArrayList<>();
        productListLastAdded = new ArrayList<>();
        adapterRecents = new ProductAdapter(getContext(), productListRecents, this);
        adapterLastAdded = new ProductAdapter(getContext(), productListLastAdded, this);
        recyclerViewRecents.setAdapter(adapterRecents);
        recyclerViewLastAdded.setAdapter(adapterLastAdded);

        // Firebase setup
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");

        // Fetch products
        fetchData();

        // Setup UI actions
        setupPriceRangeSlider();
        setupFilterTextListener();
        setupBarButton();
        setupFilterAnimation(menuButton, filterContainer);

        return rootView;
    }

    // Fetch data from Firebase
    private void fetchData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productListRecents.clear();
                productListLastAdded.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    if (product != null) {
                        if (product.getPrezzo() % 2 == 0) {
                            productListLastAdded.add(product);
                        } else {
                            productListRecents.add(product);
                        }
                    }
                }
                adapterRecents.notifyDataSetChanged();
                adapterLastAdded.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("HomeScreen", "Error fetching data: " + databaseError.getMessage());
                Toast.makeText(getContext(), "Failed to load products.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Setup price range slider
    private void setupPriceRangeSlider() {
        priceRangeSlider.addOnChangeListener((slider, value, fromUser) -> {
            if (!isUpdating) {
                isUpdating = true;
                float minVal = slider.getValues().get(0);
                float maxVal = slider.getValues().get(1);
                minPrice.setText(String.valueOf((int) minVal));
                maxPrice.setText(String.valueOf((int) maxVal));
                isUpdating = false;
                filterProducts();
            }
        });

        setupEditTextSync(minPrice, priceRangeSlider, true);
        setupEditTextSync(maxPrice, priceRangeSlider, false);
    }

    private void setupEditTextSync(EditText editText, RangeSlider slider, boolean isMin) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!isUpdating && !s.toString().isEmpty()) {
                    try {
                        isUpdating = true;
                        float value = Float.parseFloat(s.toString());
                        if (isMin) {
                            slider.setValues(Math.max(slider.getValueFrom(), value), slider.getValues().get(1));
                        } else {
                            slider.setValues(slider.getValues().get(0), Math.min(slider.getValueTo(), value));
                        }
                        filterProducts();
                    } catch (NumberFormatException e) {
                        Log.e("RangeSlider", "Invalid input: " + s.toString());
                    } finally {
                        isUpdating = false;
                    }
                }
            }
        });
    }

    // Setup filter by text
    private void setupFilterTextListener() {
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterProducts();
            }
        });
    }

    private void filterProducts() {
        String searchText = editTextFilter.getText().toString().trim().toLowerCase();
        double minPriceValue = 0, maxPriceValue = Double.MAX_VALUE;

        try {
            if (!minPrice.getText().toString().isEmpty()) {
                minPriceValue = Double.parseDouble(minPrice.getText().toString());
            }
            if (!maxPrice.getText().toString().isEmpty()) {
                maxPriceValue = Double.parseDouble(maxPrice.getText().toString());
            }
        } catch (NumberFormatException e) {
            Log.e("HomeScreen", "Invalid price input.");
            return;
        }

        List<Product> filteredRecents = new ArrayList<>();
        List<Product> filteredLastAdded = new ArrayList<>();

        for (Product product : productListRecents) {
            if (matchesFilter(product, searchText, minPriceValue, maxPriceValue)) {
                filteredRecents.add(product);
            }
        }

        for (Product product : productListLastAdded) {
            if (matchesFilter(product, searchText, minPriceValue, maxPriceValue)) {
                filteredLastAdded.add(product);
            }
        }

        adapterRecents.updateList(filteredRecents);
        adapterLastAdded.updateList(filteredLastAdded);
    }

    private boolean matchesFilter(Product product, String searchText, double minPrice, double maxPrice) {
        boolean matchesText = product.getNome().toLowerCase().contains(searchText);
        boolean matchesBaratto = !filterByBaratto || product.isBaratto();
        double productPrice = product.getPrezzo();
        boolean matchesPrice = productPrice >= minPrice && productPrice <= maxPrice;

        return matchesText && matchesBaratto && matchesPrice;
    }

    // Setup bar button toggle
    private void setupBarButton() {
        barButton.setBackgroundColor(Color.RED);
        barButton.setOnClickListener(v -> {
            filterByBaratto = !filterByBaratto;
            barButton.setBackgroundColor(filterByBaratto ? ContextCompat.getColor(getContext(), R.color.dark) : Color.RED);
            filterProducts();
        });
    }

    // Setup filter animation
    private void setupFilterAnimation(ImageButton menuButton, LinearLayout filterContainer) {
        filterContainer.setVisibility(View.GONE);
        menuButton.setOnClickListener(v -> {
            boolean isVisible = filterContainer.getVisibility() == View.VISIBLE;
            filterContainer.setVisibility(isVisible ? View.GONE : View.VISIBLE);
        });
    }

    @Override
    public void onItemClick(Product product) {
        Bundle bundle = new Bundle();
        new User(product.getIdVenditore(), new User.UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                bundle.putString("venditore", user.getUsername());
                bundle.putString("nome", product.getNome());
                bundle.putString("descrizione", product.getDescrizione());
                bundle.putString("immagine", product.getImageUrl());
                bundle.putDouble("prezzo", product.getPrezzo());
                bundle.putBoolean("baratto", product.isBaratto());
               // bundle.putString("status", user.getProductsCount());  // Assuming this method provides product count

                navigateToDetailFragment(bundle);
            }

            @Override
            public void onError(Exception e) {
                Log.e("HomeScreen", "Error fetching user: " + e.getMessage());
            }
        });
    }

    private void navigateToDetailFragment(Bundle bundle) {
        DetailProdScreen detailFragment = new DetailProdScreen();
        detailFragment.setArguments(bundle);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (isUpdating) {
            filterProducts();  // Ensure data is re-filtered when resuming
        }
    }

}
