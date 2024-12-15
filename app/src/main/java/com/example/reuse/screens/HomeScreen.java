package com.example.reuse.screens;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.renderscript.Sampler;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.reuse.R;
import com.example.reuse.adapter.ProductAdapter;
import com.example.reuse.models.Product;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        //rimosso productList.add(new Product(R.drawable.shoes, "Sneakers Verdi", "$100", "Melissa Peters", "5 products online", R.drawable.user, List.of("costoso", "nuovo", "usabile")));
        //rimasso productList.add(new Product(R.drawable.shoes, "Sneakers Neri", "$120", "John Doe", "3 products online", R.drawable.user, List.of("costoso", "nuovo", "usabile")));   // Add more products as needed...

        // Set the adapter for both RecyclerViews
        adapter = new ProductAdapter(getContext(), productList, this);
        recyclerViewlastAdded.setAdapter(adapter);
        recyclerViewrecents.setAdapter(adapter);

        //animazione pop up filtri
        ImageButton menuButton = rootView.findViewById(R.id.menuButton);
        LinearLayout filterContainer = rootView.findViewById(R.id.filterContainer);
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
        Button barButton = rootView.findViewById(R.id.barattoButton);
        barButton.setBackgroundColor(Color.RED);
        barButton.setOnClickListener(new View.OnClickListener() {
            boolean barButtonActive = false;
            @Override
            public void onClick(View v) {
                if(!barButtonActive){
                    barButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.dark));
                    barButtonActive = true;
                }
                else{
                    barButton.setBackgroundColor(Color.RED);
                    barButtonActive = false;
                }
            }
        });

        //barra del range di prezzo
        EditText min = rootView.findViewById(R.id.minPriceSetter);
        EditText max = rootView.findViewById(R.id.maxPriceSetter);
        RangeSlider rangePrezzi = rootView.findViewById(R.id.rangePrezziSlider);

        final boolean[] isUpdating = {false};

        rangePrezzi.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                if(!isUpdating[0]){
                    isUpdating[0] = true;
                    float minVal = slider.getValues().get(0);
                    float maxVal = slider.getValues().get(1);
                    min.setText(String.valueOf((int) minVal));
                    max.setText(String.valueOf((int) maxVal));
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
                            float minVal = Float.parseFloat(text);
                            if(minVal>=rangePrezzi.getValueFrom()){
                                if(minVal<=rangePrezzi.getValues().get(1)) rangePrezzi.setValues(minVal, rangePrezzi.getValues().get(1));
                                else rangePrezzi.setValues(rangePrezzi.getValues().get(1), rangePrezzi.getValues().get(1));
                            }
                            else rangePrezzi.setValues(rangePrezzi.getValueFrom(), rangePrezzi.getValues().get(1));
                        } catch (NumberFormatException e){
                            e.printStackTrace();
                        } finally {
                            isUpdating[0] = false;
                        }
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
                            float maxVal = Float.parseFloat(text);
                            if(maxVal<=rangePrezzi.getValueTo()){
                                if(maxVal>=rangePrezzi.getValues().get(0)) rangePrezzi.setValues(rangePrezzi.getValues().get(0), maxVal);
                                else rangePrezzi.setValues(rangePrezzi.getValues().get(0), rangePrezzi.getValues().get(0));
                            }
                            else rangePrezzi.setValues(rangePrezzi.getValues().get(0), rangePrezzi.getValueTo());
                        } catch (NumberFormatException e){
                            e.printStackTrace();
                        } finally {
                            isUpdating[0] = false;
                        }


                    }
                }
            }
        });

        return rootView;  // Return the view you inflated
    }

    @Override
    public void onItemClick(Product product) {
        // Handle the product click here
        // For example, navigate to the product details page or display a Toast
        Toast.makeText(getContext(), "Clicked on: " + product.getNome(), Toast.LENGTH_SHORT).show();

        // You can now pass the product data to a new fragment or activity, e.g.
        Bundle bundle = new Bundle();
        bundle.putString("product_name", product.getNome());
        //rimosso bundle.putString("product_price", product.getPrezzo());
        //rimosso bundle.putInt("product_image", product.getImageResId()); // For ImageView
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
