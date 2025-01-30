package com.example.reuse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reuse.R;
import com.example.reuse.models.Product;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ProductsUtente2ExchangeAdapter extends RecyclerView.Adapter<ProductsUtente1ExchangeAdapter.ProductViewHolder> {
    private StorageReference storageRef;
    private Context context;
    private List<Product> productList = new ArrayList<>();
    private ProductsUtente1ExchangeAdapter.OnItemClickListener onItemClickListener;

    // Constructor with custom OnItemClickListener
    public ProductsUtente2ExchangeAdapter(Context context, List<Product> newProductList, ProductsUtente1ExchangeAdapter.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.productList.addAll(newProductList);
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ProductsUtente1ExchangeAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.item_exchange, parent, false);
        return new ProductsUtente1ExchangeAdapter.ProductViewHolder(view);
    }
    public void updateList(List<Product> newProductList) {
        this.productList.clear(); // Always clear before updating
        if (newProductList != null) {
            this.productList.addAll(newProductList);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ProductsUtente1ExchangeAdapter.ProductViewHolder holder, int position) {
        // Get the product from the list at the given position
        Product product = productList.get(position);

        holder.productName.setText(product.getNome());


        // Set product price
        if (product.getPrezzo() > 0) {
            holder.productPrice.setText(String.format("$%.2f", product.getPrezzo())); // Format price as a currency
        } else {
            holder.productPrice.setText("Price Unavailable");
        }

        // Set the click listener for the item view
        //holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Define the custom interface for item clicks
    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        // Declare the views inside each item
        ImageView productImage, userAvatar;
        TextView productName, productPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productNamee);
            productPrice = itemView.findViewById(R.id.productPricee);
        }
    }
}
