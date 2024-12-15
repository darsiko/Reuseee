package com.example.reuse.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reuse.R;
import com.example.reuse.models.Product;
import com.example.reuse.models.User;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;
import java.util.Objects;

public class ProductBroughtAdapter extends RecyclerView.Adapter<ProductBroughtAdapter.ProductViewHolder> {
    private FirebaseAuth auth;
    private Context context;

    private List<Product> productList;
    private OnItemClickListener onItemClickListener;

    public ProductBroughtAdapter(Context context, List<Product> productList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.productList = productList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_prod_brought_products, parent, false);
        auth= FirebaseAuth.getInstance();
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Get the product at the current position
        Product product = productList.get(position);
        String sellerId = product.getIdVenditore();

        // Use the callback to set user details once loaded
        new User(sellerId, new User.UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                holder.userName.setText(user.getUsername());
            }

            @Override
            public void onError(Exception e) {
                holder.userName.setText("Unknown Seller");
                Log.e("ProductAdapter", "Error loading user data: ", e);
            }
        });

        holder.productName.setText(product.getNome());

        // Set product price
        if (product.getPrezzo() > 0) {
            holder.productPrice.setText(String.format("$%.2f", product.getPrezzo())); // Format price as a currency
        } else {
            holder.productPrice.setText("Price Unavailable");
        }
        holder.userStatus.setText(productList.size()+" products online");

        holder.editProduct.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), "Item: " + product.getNome(), Toast.LENGTH_SHORT).show();
            onItemClickListener.onEditProductClick(product);
        });
        // If you have more views in the ViewHolder, bind them here
    }

    public interface OnItemClickListener {
          void onEditProductClick(Product product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage, userAvatar, editProduct;
        TextView productName, productPrice, userName, userStatus;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            userAvatar = itemView.findViewById(R.id.sellerImage);
            userName = itemView.findViewById(R.id.sellerName);
            userStatus = itemView.findViewById(R.id.sellerStatus);
            editProduct = itemView.findViewById(R.id.editProduct);
        }
    }
}
